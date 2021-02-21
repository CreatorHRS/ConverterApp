package ru.mikhailkhr.ConverterApp.apihandler;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.mikhailkhr.ConverterApp.entity.ValuteValue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Service that call a api to get Vlutes
 *
 * @author mikhailkhr
 */
@Service
public class ValuteApiRequester
{
	final String uri = "http://www.cbr.ru/scripts/XML_daily.asp";

	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	public ValuteApiRequester()
	{
	}

	/**
	 * Get valute value by date
	 *
	 * @param date
	 * @return list of valute values
	 */
	public List<ValuteValue> getAllValuteByDate(LocalDate date)
	{
		List<ValuteValue> list = new ArrayList<>();
		Document doc = null;
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		// making a call
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(uri + "?date_req=" + date.format(dtf));
			doc.getDocumentElement().normalize();
		} catch(SAXException | IOException e)
		{
			e.printStackTrace();
		} catch(ParserConfigurationException e)
		{
			e.printStackTrace();
		}

		// prepare the elements to parse
		NodeList elements = doc.getElementsByTagName("Valute");

		list.add(new ValuteValue(1.0, 1, date, 643));
		for(int vaultIndex = 0; vaultIndex < elements.getLength(); vaultIndex++)
		{
			NodeList vaultValues = elements.item(vaultIndex).getChildNodes();
			ValuteValue valute = new ValuteValue();

			valute.setDate(date);
			for(int valuesIndex = 0; valuesIndex < vaultValues.getLength(); valuesIndex++)
			{
				Node valueNode = vaultValues.item(valuesIndex);

				switch(valueNode.getNodeName())
				{
					case ValuteValue.TAG_NAME_NUM_CODE:
					{
						valute.setValuteNumCode(Integer.parseInt(valueNode.getTextContent()));
						break;
					}
					case ValuteValue.TAG_NAME_NOMINAL:
					{
						valute.setNominal(Integer.parseInt(valueNode.getTextContent()));
						break;
					}
					case ValuteValue.TAG_NAME_VALUE:
					{
						NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
						Number number;
						try
						{
							number = format.parse(valueNode.getTextContent());
						} catch(ParseException e)
						{
							number = Double.valueOf(0.0);
							e.printStackTrace();
						}
						valute.setValue(number.doubleValue());
						break;
					}

					default:
						break;
				}

			}
			valute.setDate(date);
			list.add(valute);
		}

		return list;
	}

	/**
	 * Get valute value by list of dates
	 *
	 * @param dates
	 * @return list of valute values
	 */
	public List<ValuteValue> getAllValuteByDate(List<LocalDate> dates)
	{
		List<ValuteValue> finalList = new LinkedList<ValuteValue>();
		for(LocalDate date : dates)
		{
			finalList.addAll(getAllValuteByDate(date));
		}
		return finalList;
	}

}
