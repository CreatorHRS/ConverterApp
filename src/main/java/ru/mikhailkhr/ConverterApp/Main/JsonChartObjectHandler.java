package ru.mikhailkhr.ConverterApp.Main;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class JsonChartObjectHandler {
	
	public Map<String, Object> getJsonChart(LocalDate dateFrom, LocalDate dateTo, String charFrom, String cahrTo, String unit)
{	
//	    List<Object> dataList = new LinkedList<Object>();
//		List<Object> datasets = new LinkedList<Object>();
//		Map<String, Object> dataset = new LinkedHashMap<String, Object>();
//		dataset.put("backgroundColor", "rgba(255, 99, 132, 0.5)");
//		dataset.put("borderColor", "rgb(255, 99, 132)");
//		dataset.put("type", "line");
//		dataset.put("fill", false);
//		dataset.put("lineTension", 0);
//		datasets.add(dataset);
//		
//		options
//		Map<String, Object> result = new LinkedHashMap<String, Object>();
//		result.put("data", datasets);
		
		
		
		
		Map<String, Object> jsonData = new LinkedHashMap<String, Object>();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		Map<String, Object> options = new LinkedHashMap<String, Object>();
		List<Object> datasets = new LinkedList<Object>();
		Map<String, Object> dataset = new LinkedHashMap<String, Object>();
		dataset.put("backgroundColor", "rgba(255, 99, 132, 0.5)");
		dataset.put("borderColor", "rgb(255, 99, 132)");
		dataset.put("type", "line");
		dataset.put("fill", false);
		dataset.put("lineTension", 0);
		List<Object> chartData =  new LinkedList<Object>();
		dataset.put("data", chartData);
		datasets.add(dataset);
		data.put("datasets", datasets);
		jsonData.put("data", data);
		jsonData.put("options", options);
		return null;
		
	}
	
}
