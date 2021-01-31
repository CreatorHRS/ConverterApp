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
import ru.mikhailkhr.ConverterApp.Utils.ValutePair;
import ru.mikhailkhr.ConverterApp.entity.HistoryEntry;
import ru.mikhailkhr.ConverterApp.entity.Valute;
import ru.mikhailkhr.ConverterApp.entity.ValuteValue;
import ru.mikhailkhr.ConverterApp.security.SecurityUser;

/**
 * Class that do the convertion
 * @author mikhailkhr
 *
 */
@Service
public class ValuteConverter {
	
	/**
	 * Convert the values
	 * @param convertFromCharCode {@code String} char code form convert
	 * @param convertToCharCode   {@code String} char code to convert
	 * @param number              {@code double} value to convert
	 * @param date                {@code LocalDate} date of last update valutes 
	 * @return
	 */
	public double convertValultes(double fromValue,  double fromNominal, double toValue, double toNominal, double number) 
	{	
		// calculate the convert to value
		double tempVal = number * (fromValue  / fromNominal);
		double result = tempVal / (toValue / toNominal);
		result = Math.round(result * 100.0)/100.0;
		
		return result;
	}
	
	/**
	 * Convert the values
	 * @param valutePair   {@code ValutePair} pair of valute
	 * @param number              {@code double} value to convert
	 * @return
	 */
	public double convertValultes(ValutePair valutePair, double number) 
	{	
		ValuteValue valueForm = valutePair.getValuteFrom();
		ValuteValue valueTo = valutePair.getValuteTo();
		
		return convertValultes(valueForm.getValue(), valueForm.getNominal(), valueTo.getValue(), valueTo.getNominal(), number);
	}
	
	/**
	 * Convert the values 
	 * @param valutePair   {@code ValutePair} pair of valute
	 * @return
	 */
	public double convertValultes(ValutePair valutePair) 
	{	
		ValuteValue valueForm = valutePair.getValuteFrom();
		ValuteValue valueTo = valutePair.getValuteTo();
		System.out.println("valueForm = " + valueForm);
		System.out.println("valueTo = " + valueTo);
		
		return convertValultes(valueForm.getValue(), valueForm.getNominal(), valueTo.getValue(), valueTo.getNominal(), 1);
	}
}