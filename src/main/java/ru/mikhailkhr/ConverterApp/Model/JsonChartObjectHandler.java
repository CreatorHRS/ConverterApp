package ru.mikhailkhr.ConverterApp.Model;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.mikhailkhr.ConverterApp.Utils.Unit;


@Service
public class JsonChartObjectHandler {

	@Autowired
	ChartValueDataModel chartValueDataModel;
	
	public Map<String, Object> getJsonChart(LocalDate dateFrom, LocalDate dateTo, int numCodeFrom, int numCodeTo, Unit unit)
	{	
		
		
		Map<String, Object> jsonData = new LinkedHashMap<String, Object>();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		jsonData.put("data", data);
		List<Object> datasets = new LinkedList<Object>();
		data.put("datasets", datasets);
		Map<String, Object> dataset = new LinkedHashMap<String, Object>();
		datasets.add(dataset);
		dataset.put("backgroundColor", "rgba(255, 99, 132, 0.5)");
		dataset.put("borderColor", "rgb(255, 99, 132)");
		dataset.put("type", "line");
		dataset.put("fill", false);
		dataset.put("lineTension", 0);
		dataset.put("pointRadius", 0);
		dataset.put("label", "USD - RUB");
		List<Map<String, Object>> chartData =  chartValueDataModel.getChartData(dateFrom, dateTo, numCodeFrom, numCodeTo, unit);
		dataset.put("data", chartData);
		
		
		Map<String, Object> options = new LinkedHashMap<String, Object>();
		jsonData.put("options", options);
		options.put("responsive", true);
		Map<String,Object> hover = new LinkedHashMap<String, Object>();
		options.put("hover", hover);
		hover.put("mode", "nearest");
		hover.put("intersect", false);
		Map<String,Object> animation = new LinkedHashMap<String, Object>();
		options.put("animation", animation);
		animation.put("duration", 1500);
		Map<String,Object> legend = new LinkedHashMap<String, Object>();
		options.put("legend", legend);
		legend.put("display", false);
		Map<String,Object> scales = new LinkedHashMap<String, Object>();
		options.put("scales", scales);
		List<Object> xAxes = new LinkedList<Object>();
		scales.put("xAxes", xAxes);
		Map<String,Object> xAxe0 = new LinkedHashMap<String, Object>();
		xAxes.add(xAxe0);
		xAxe0.put("display", true);
		xAxe0.put("type", "time");
		xAxe0.put("distribution", "series");
		Map<String,Object> ticks = new LinkedHashMap<String, Object>();
		xAxe0.put("ticks", ticks);
		Map<String,Object> major = new LinkedHashMap<String, Object>();
		ticks.put("major", major);
		major.put("enabled", true);
		major.put("fontStyle", "bold");
		ticks.put("source", "data");
		ticks.put("maxRotation", 0);
		ticks.put("autoSkip", true);
		ticks.put("autoSkipPadding", 25);
		List<Object> yAxes = new LinkedList<Object>();
		scales.put("yAxes", yAxes);
		Map<String,Object> yAxe0 = new LinkedHashMap<String, Object>();
		yAxes.add(yAxe0);
		yAxe0.put("display", true);
		Map<String,Object> scaleLabel = new LinkedHashMap<String, Object>();
		yAxe0.put("scaleLabel", scaleLabel);
		scaleLabel.put("display", "true");
		scaleLabel.put("labelString", "Value");
		Map<String,Object> tooltips = new LinkedHashMap<String, Object>();
		options.put("tooltips", tooltips);
		tooltips.put("intersect", false);
		tooltips.put("mode", "index");
		Map<String,Object> callbacks = new LinkedHashMap<String, Object>();
		tooltips.put("callbacks", callbacks);
		
		
		
		
		
		
		
		
		return jsonData;
		
	}
	
}
