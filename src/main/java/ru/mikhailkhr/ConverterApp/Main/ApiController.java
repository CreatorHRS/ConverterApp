package ru.mikhailkhr.ConverterApp.Main;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
	@GetMapping("/data.json")
	public Map<String, Object> getDataJson()
	{
		Map map = new LinkedHashMap();
		map.put("gegsgdf", false);
		map.put("Hello", 122);
		return map;
		
	}
}
