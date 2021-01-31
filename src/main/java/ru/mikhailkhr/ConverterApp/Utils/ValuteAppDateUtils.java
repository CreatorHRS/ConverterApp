package ru.mikhailkhr.ConverterApp.Utils;

import java.time.LocalDate;

public class ValuteAppDateUtils {
	public static LocalDate datePlusUnits(LocalDate date, Unit unit, int unitsToAdd) {
		LocalDate newDate = null;
		switch (unit) {
		case DAY: {
			newDate = date.plusDays(unitsToAdd);
			break;
		}
		case WEEK: {
			newDate = date.plusWeeks(unitsToAdd);
			break;
		}
		case MONTH: {
			newDate = date.plusMonths(unitsToAdd);
		}
		}

		return newDate;
	}
	
	public static LocalDate datePlusUnit(LocalDate date, Unit unit) {
		return datePlusUnits(date, unit, 1);
	}
}
