package ru.mikhailkhr.ConverterApp.entity;

import java.time.LocalDate;

/**
 * Class that represent valute 
 * @author mikhailkhr
 *
 */
public class Valute {
	
	public final static String TAG_NAME_NUM_CODE = "NumCode";
	public final static String TAG_NAME_CHAR_CODE = "CharCode";
	public final static String TAG_NAME_NOMINAL = "Nominal";
	public final static String TAG_NAME_NAME = "Name";
	public final static String TAG_NAME_VALUE = "Value";
	private String numCode;
	private String charCode;
	private String  name;
	private double value;
	private int nominal;
	private LocalDate date;
	private String id;
	public Valute() {
		super();
	}
	public Valute(String numCode, String charCode, String name, double value, int nominal,LocalDate date, String id) {
		this.numCode = numCode;
		this.charCode = charCode;
		this.name = name;
		this.value = value;
		this.nominal = nominal;
		this.date = date;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumCode() {
		return numCode;
	}
	public void setNumCode(String numCode) {
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
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getNominal() {
		return nominal;
	}
	public void setNominal(int nominal) {
		this.nominal = nominal;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
}
