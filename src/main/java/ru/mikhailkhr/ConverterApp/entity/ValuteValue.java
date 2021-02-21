package ru.mikhailkhr.ConverterApp.entity;

import java.time.LocalDate;

public class ValuteValue
{
	public final static String TAG_NAME_NUM_CODE = "NumCode";
	public final static String TAG_NAME_NOMINAL = "Nominal";
	public final static String TAG_NAME_VALUE = "Value";
	public final static String KEY_NAME_FROM = "from";
	public final static String KEY_NAME_TO = "to";

	private double value;
	private int nominal;
	private LocalDate date;
	private int id;
	private int valuteNumCode;

	@Override
	public String toString()
	{
		return "Valute '" + valuteNumCode + "' nominal =" + nominal + ", value = " + value;
	}

	public ValuteValue(double value, int nominal, LocalDate date, int valuteNumCode, int id)
	{
		this.value = value;
		this.nominal = nominal;
		this.date = date;
		this.id = id;
		this.valuteNumCode = valuteNumCode;
	}

	public ValuteValue(double value, int nominal, LocalDate date, int valuteNumCode)
	{
		this.value = value;
		this.nominal = nominal;
		this.date = date;
		this.valuteNumCode = valuteNumCode;
	}

	public ValuteValue()
	{
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	public int getNominal()
	{
		return nominal;
	}

	public void setNominal(int nominal)
	{
		this.nominal = nominal;
	}

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getValuteNumCode()
	{
		return valuteNumCode;
	}

	public void setValuteNumCode(int valuteNumCode)
	{
		this.valuteNumCode = valuteNumCode;
	}

}
