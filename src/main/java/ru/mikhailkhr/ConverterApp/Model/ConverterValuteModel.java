package ru.mikhailkhr.ConverterApp.Model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mikhailkhr.ConverterApp.JDBC.HistoryEntryJdbcDao;
import ru.mikhailkhr.ConverterApp.JDBC.ValuteJdbcDao;
import ru.mikhailkhr.ConverterApp.JDBC.ValuteValuesJdbcDao;
import ru.mikhailkhr.ConverterApp.Main.ValuteConverter;
import ru.mikhailkhr.ConverterApp.apihandler.ValuteApiRequester;
import ru.mikhailkhr.ConverterApp.entity.HistoryEntry;
import ru.mikhailkhr.ConverterApp.entity.ValuteValue;

@Service
public class ConverterValuteModel {
	@Autowired
	ValuteValuesJdbcDao valuteValuesJdbcDao;
	@Autowired
	ValuteConverter convertintValutes;
	@Autowired
	HistoryEntryJdbcDao historyEntryJdbcDao;
	@Autowired
	ValuteApiRequester valuteApiRequester;
	
	@Autowired
	JsonChartObjectHandler jsonChartObjectHandler; 
	
	
	public Double convert(int numCodeFrom, int numCodeTo, double number, int userId) {
		
	
			
			Map<String, ValuteValue> info = valuteValuesJdbcDao.getFromToValueInformation(numCodeFrom, numCodeTo);
			if(info.isEmpty()) 
			{
				List<ValuteValue> valutesValuesList = valuteApiRequester.getAllValuteByDate(LocalDate.now());
				valuteValuesJdbcDao.putAllValute(valutesValuesList);
				info = valuteValuesJdbcDao.getFromToValueInformation(numCodeFrom, numCodeTo);
			}
			ValuteValue fromValueValue = info.get(ValuteValue.KEY_NAME_FROM);
			ValuteValue toValuteValue = info.get(ValuteValue.KEY_NAME_TO);
			double fromValue = fromValueValue.getValue();
			double fromNominal = fromValueValue.getNominal();
			double toValue = toValuteValue.getValue();
			double toNominal = toValuteValue.getNominal();
			int fromValuteValueId = fromValueValue.getId();
			int toValuteValueId = toValuteValue.getId();

			// Tries to find propriate values by char code

			

			double result = convertintValutes.convertValultes(fromValue, fromNominal, toValue, toNominal, number);

			// create history entry
			HistoryEntry historyEntry = new HistoryEntry(number, result, LocalTime.now(),
					LocalDate.now(), fromValuteValueId,toValuteValueId , userId);

			// put history entry to database
			historyEntryJdbcDao.insertHistoryEntry(historyEntry);
		return result;
	}
}
