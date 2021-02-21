package ru.mikhailkhr.ConverterApp.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mikhailkhr.ConverterApp.JDBC.ValuteValuesJdbcDao;
import ru.mikhailkhr.ConverterApp.Main.ValuteConverter;
import ru.mikhailkhr.ConverterApp.Utils.Unit;
import ru.mikhailkhr.ConverterApp.Utils.ValuteAppDateUtils;
import ru.mikhailkhr.ConverterApp.Utils.ValutePair;
import ru.mikhailkhr.ConverterApp.apihandler.ValuteApiRequester;
import ru.mikhailkhr.ConverterApp.entity.ValuteValue;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChartValueDataModel
{

	@Autowired
	ValuteValuesJdbcDao valuteValuesJdbcDao;
	@Autowired
	ValuteApiRequester valuteApiRequester;
	@Autowired
	ValuteConverter valuteConverter;

	public List<Map<String, Object>> getChartData(LocalDate dateFrom, LocalDate dateTo, int numCodeFrom, int numCodeTo, Unit unit)
	{
		List<ValutePair> valuteValues = valuteValuesJdbcDao.getFromToValueInformation(dateFrom, dateTo, numCodeFrom, numCodeTo);
		List<LocalDate> missingDates = new ArrayList<LocalDate>();

		missingDates = getValuteValueMissingDates(valuteValues, dateFrom, dateTo, unit);

		// get all valute values that was missing
		List<ValuteValue> apiRequestedValuteValues = new ArrayList<ValuteValue>();
		if(missingDates.size() > 0)
		{
			apiRequestedValuteValues = valuteApiRequester.getAllValuteByDate(missingDates);
			valuteValuesJdbcDao.putAllValute(apiRequestedValuteValues);
		}
		/*
		 * TODO: Make a function that use a list from valuteApiRequster to get missing
		 * values date. It a more faster than make a second major request to database
		 */
		valuteValues = valuteValuesJdbcDao.getFromToValueInformation(dateFrom, dateTo, numCodeFrom, numCodeTo);
		List<Map<String, Object>> chartData = new ArrayList<Map<String, Object>>();
		LocalDate compareDate = dateFrom.plusDays(0);
		for(ValutePair pair : valuteValues)
		{
			LocalDate chartEntryLocalDate = pair.getValuteFrom().getDate();
			if(compareDate.equals(chartEntryLocalDate))
			{
				Map<String, Object> chartDataEntry = new HashMap<String, Object>();
				ZonedDateTime zonedDataForChartEntry = LocalDateTime.of(chartEntryLocalDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault());
				chartDataEntry.put("t", zonedDataForChartEntry.toInstant().toEpochMilli());
				Double chartValue = valuteConverter.convertValultes(pair);
				chartDataEntry.put("y", chartValue);
				chartData.add(chartDataEntry);

				compareDate = ValuteAppDateUtils.datePlusUnit(compareDate, unit);
			}
		}
		return chartData;

	}

	private List<LocalDate> getValuteValueMissingDates(List<ValutePair> valuteValues, LocalDate dateFrom, LocalDate dateTo, Unit unit)
	{
		int valutePairIndex = 0;
		LocalDate compareDate = dateFrom.plusDays(0);
		List<LocalDate> missingDates = new ArrayList<LocalDate>();
		// Find all valute value dates that missing in the database
		while(!dateTo.isBefore(compareDate))
		{
			if(valutePairIndex < valuteValues.size())
			{
				LocalDate valuteValueDate = valuteValues.get(valutePairIndex).getValuteFrom().getDate();
				if(compareDate.equals(valuteValueDate))
				{
					valutePairIndex++;
					compareDate = ValuteAppDateUtils.datePlusUnit(compareDate, unit);
				} else if(compareDate.isBefore(valuteValueDate))
				{
					missingDates.add(compareDate);
					compareDate = ValuteAppDateUtils.datePlusUnit(compareDate, unit);
				} else if(compareDate.isAfter(valuteValueDate))
				{
					valutePairIndex++;
				}
			} else
			{
				missingDates.add(compareDate);
				compareDate = ValuteAppDateUtils.datePlusUnit(compareDate, unit);
			}
		}
		return missingDates;
	}

	private List<LocalDate> getMissingDates(List<ValutePair> valuteValues, LocalDate dateFrom, LocalDate dateTo, Unit unit)
	{
		int valutePairIndex = 0;
		List<LocalDate> missingDates = new ArrayList<LocalDate>();
		// Find all valute value dates that missing in the database
		for(LocalDate compareDate = dateFrom.plusDays(0); !compareDate.isAfter(dateTo); compareDate = ValuteAppDateUtils.datePlusUnit(compareDate, unit))
		{
			if(valuteValues.size() > valutePairIndex)
			{
				LocalDate valuteValueDate = valuteValues.get(valutePairIndex).getValuteFrom().getDate();
				if(compareDate.equals(valuteValueDate))
				{

					valutePairIndex++;
				} else
				{
					missingDates.add(compareDate);
				}
			} else
			{
				missingDates.add(compareDate);
			}
		}
		return missingDates;
	}

}
