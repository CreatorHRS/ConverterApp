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

	@Autowired
	ValuteJdbcDao valuteJdbcDao;
	@Autowired
	HistoryEntryJdbcDao historyEntryJdbcDao;
	
	/**
	 * Convert the value by char codes and date of last update vlute date 
//	 * @param convertFromCharCode {@code String} char code form convert
	 * @param convertToCharCode   {@code String} char code to convert
	 * @param number              {@code double} value to convert
	 * @param date                {@code LocalDate} date of last update valutes 
	 * @return
	 */
	public double convertValultes(String convertFromCharCode,  String convertToCharCode, double number, LocalDate date) 
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		String user_id = userDetails.getId();
		List<Valute> list =  valuteJdbcDao.getAllValuteByDate(date);
		
		double fromValue = 0.0;
		double fromNominal = 0.0;
		double toValue = 0.0;
		double toNominal = 0.0;
		String fromId = "";
		String toId = "";
		/*
		 * if value if RUB user default values
		 */
		if("RUB".equals(convertFromCharCode)) 
		{
			
			fromNominal = 1.0;
			fromValue = 1.0;
			fromId = "1";
		}
		/*
		 * if value if RUB user default values
		 */
		if("RUB".equals(convertToCharCode)) 
		{
			toNominal = 1.0;
			toValue = 1.0;
			toId = "1";
		}
		/*
		 * Trys to find propriate values by char code
		 */
		for (Valute valute : list) {
			if(valute.getCharCode().equals(convertFromCharCode)) 
			{
				fromNominal = (double) valute.getNominal();
				fromValue = valute.getValue();
				fromId = valute.getId();
			}
			if(valute.getCharCode().equals(convertToCharCode)) 
			{
				toNominal = (double) valute.getNominal();
				toValue = valute.getValue();
				toId = valute.getId();
			}
		}
		
		/*
		 * calculate the convert to value
		 */
		double tempVal = number * fromValue * fromNominal;
		double result = tempVal / toNominal / toValue;
		
		/*
		 * create history entry
		 */
		HistoryEntry historyEntry = new HistoryEntry(convertFromCharCode, convertToCharCode, number, result, LocalTime.now(), LocalDate.now(),fromId, toId);
		
		/*
		 * put history entry to database
		 */
		historyEntryJdbcDao.insertHistoryEntry(historyEntry, user_id);
		return result;
	}
}