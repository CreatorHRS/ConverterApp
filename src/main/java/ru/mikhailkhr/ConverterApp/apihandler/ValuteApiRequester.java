package ru.mikhailkhr.ConverterApp.apihandler;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.mikhailkhr.ConverterApp.entity.Valute;

/**
 * Service that call a api to get Vlutes
 * @author mikhailkhr
 *
 */
@Service
public class ValuteApiRequester {
	final String uri = "http://www.cbr.ru/scripts/XML_daily.asp";

	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	
	
	public ValuteApiRequester() {
	}

	/**
	 * Method that gets all today valutes using a central bank api
	 * @return list of valutes
	 */
	public List<Valute> getVaulter() {
		List<Valute> list = new ArrayList<>();
		Document doc = null;
		/*
		 * making a call
		 */
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(uri);
			doc.getDocumentElement().normalize();
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * prepare the elements to parse 
		 */
		NodeList elements = doc.getElementsByTagName("Valute");
		String dateString = doc.getElementsByTagName("ValCurs").item(0).getAttributes().getNamedItem("Date").getTextContent();
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		final LocalDate date = LocalDate.parse(dateString, dtf);
		for (int vaultIndex = 0; vaultIndex < elements.getLength(); vaultIndex++) {
			NodeList vaultValues = elements.item(vaultIndex).getChildNodes();
			Valute valute = new Valute();
			
			valute.setDate(date);
			for (int valuesIndex = 0; valuesIndex < vaultValues.getLength(); valuesIndex++) {
				Node valueNode = vaultValues.item(valuesIndex);
				
				switch (valueNode.getNodeName()) {
				case Valute.TAG_NAME_CHAR_CODE: {
					valute.setCharCode(valueNode.getTextContent());
					break;
				}
				case Valute.TAG_NAME_NUM_CODE: {
					valute.setNumCode(valueNode.getTextContent());
					break;
				}
				case Valute.TAG_NAME_NOMINAL: {
					valute.setNominal(Integer.parseInt(valueNode.getTextContent()));
					break;
				}
				case Valute.TAG_NAME_NAME: {
					valute.setName(valueNode.getTextContent());
					break;
				}
				case Valute.TAG_NAME_VALUE: {
					NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
					Number number = null;
					try {
						number = format.parse(valueNode.getTextContent());
					} catch (ParseException e) {
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
			list.add(valute);
		}
		return list;
	}

}
