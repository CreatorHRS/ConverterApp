package ru.mikhailkhr.ConverterApp.Main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ru.mikhailkhr.ConverterApp.JDBC.HistoryEntryJdbcDao;
import ru.mikhailkhr.ConverterApp.JDBC.ValuteJdbcDao;
import ru.mikhailkhr.ConverterApp.entity.HistoryEntry;
import ru.mikhailkhr.ConverterApp.entity.Valute;
import ru.mikhailkhr.ConverterApp.security.SecurityUser;

/**
 * Class that do the convertion
 * @author mikhailkhr
 *
 */
@Service
public class ValuteConverter {
	
	/**
	 * Convert the value by char codes and date of last update vlute date 
	 * @param convertFromCharCode {@code String} char code form convert
	 * @param convertToCharCode   {@code String} char code to convert
	 * @param number              {@code double} value to convert
	 * @param date                {@code LocalDate} date of last update valutes 
	 * @return
	 */
	public double convertValultes(double fromValue,  double fromNominal, double toValue, double toNominal, double number) 
	{	
		// calculate the convert to value
		double tempVal = number * fromValue * fromNominal;
		double result = tempVal / toNominal / toValue;
		result = Math.round(result * 100.0)/100.0;
		// create history entry
		//HistoryEntry historyEntry = new HistoryEntry(convertFromCharCode, convertToCharCode, number, result, LocalTime.now(), LocalDate.now(),fromId, toId);
		
		// put history entry to database
		//historyEntryJdbcDao.insertHistoryEntry(historyEntry, user_id);
		return result;
	}
}