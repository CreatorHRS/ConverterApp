package ru.mikhailkhr.ConverterApp.Utils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Unit {
	DAY(0),
	WEEK(1),
	MONTH(2);
	
	private final int number;

    private final static Map<Integer, Unit> map =
    		Arrays.stream(Unit.values()).collect(Collectors.toMap(unit -> unit.number, unit -> unit));
    
    private Unit(int number) {
    	this.number = number;
    }
    
    /**
     * Get unit by unit number;
     * @param number
     * @return
     */
    public static Unit valueOf(int number) {
        return map.get(number);
    }
	
}
