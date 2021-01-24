package ru.mikhailkhr.ConverterApp.entity;

import java.time.LocalDate;

/**
 * Class that represent valute 
 * @author mikhailkhr
 *
 */
public class Valute {
	private int numCode;
	private String charCode;
	private String  name;
	public Valute() {
	}
	public Valute(int numCode, String charCode, String name) {
		this.numCode = numCode;
		this.charCode = charCode;
		this.name = name;
	}
	public int getNumCode() {
		return numCode;
	}
	public void setNumCode(int numCode) {
		this.numCode = numCode;
	}
	public String getCharCode() {
		return charCode;
	}
	public void setCharCode(String charCode) {
		this.charCode = charCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
