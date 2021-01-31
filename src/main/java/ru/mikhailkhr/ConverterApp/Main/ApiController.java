package ru.mikhailkhr.ConverterApp.Main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.mikhailkhr.ConverterApp.Model.JsonChartObjectHandler;
import ru.mikhailkhr.ConverterApp.Utils.Unit;

@RestController
public class ApiController {
	@Autowired
	JsonChartObjectHandler jsonChartObjectHandler;
	@GetMapping("/data")
	public Map<String, Object> getDataJson(HttpServletRequest request)
	{
		//2021-01-12
		DateTimeFormatter chartDataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String dateFromStr = request.getParameter("date_from");
		String dateToStr = request.getParameter("date_to");
		String valuteNumCodeFromStr = request.getParameter("valute_from");
		String valuteNumCodeToStr = request.getParameter("valute_to");
		String unitStr = request.getParameter("unit");
		
		LocalDate dateFrom = null;
		LocalDate dateTo = null;
		int valuteNumCodeFrom = 0; 
		int valuteNumCodeTo = 0;
		Unit unit = null;
		
		System.out.println("dateFromStr = " + dateFromStr);
		System.out.println("dateToStr = " +dateToStr);
		System.out.println("valuteNumCodeFromStr = " +valuteNumCodeFromStr);
		System.out.println("valuteNumCodeToStr = " +valuteNumCodeToStr);
		System.out.println("unitStr = " +unitStr);
		
		if(dateFromStr == null) {
			dateFrom = LocalDate.of(2000, 1, 1);
		}else {
			dateFrom = LocalDate.parse(dateFromStr, chartDataFormatter);
		}
		if(dateToStr == null) {
			dateTo = LocalDate.of(2021, 1, 1);
		}else {
			dateTo = LocalDate.parse(dateToStr, chartDataFormatter);
		}
		if(valuteNumCodeFromStr == null) {
			valuteNumCodeFrom = 840;
		}else {
			valuteNumCodeFrom = Integer.parseInt(valuteNumCodeFromStr);
		}
		if(valuteNumCodeToStr == null) {
			valuteNumCodeTo = 643;
		}else {
			valuteNumCodeTo = Integer.parseInt(valuteNumCodeToStr);
		}
		if(unitStr == null) {
				unit = Unit.valueOf(2);
		}else {
			unit = Unit.valueOf(Integer.parseInt(unitStr));
		}
		
		return jsonChartObjectHandler.getJsonChart(dateFrom, dateTo, valuteNumCodeFrom, valuteNumCodeTo, unit);
		
	}
}
