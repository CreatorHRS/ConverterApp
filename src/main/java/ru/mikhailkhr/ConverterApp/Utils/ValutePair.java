package ru.mikhailkhr.ConverterApp.Utils;

import ru.mikhailkhr.ConverterApp.entity.ValuteValue;

public class ValutePair
{

	private ValuteValue valuteFrom;
	private ValuteValue valuteTo;

	public ValutePair(ValuteValue valuteFrom, ValuteValue valuteTo)
	{
		this.valuteFrom = valuteFrom;
		this.valuteTo = valuteTo;
	}

	public ValutePair()
	{
	}

	public ValuteValue getValuteFrom()
	{
		return valuteFrom;
	}

	public void setValuteFrom(ValuteValue valuteFrom)
	{
		this.valuteFrom = valuteFrom;
	}

	public ValuteValue getValuteTo()
	{
		return valuteTo;
	}

	public void setValuteTo(ValuteValue valuteTo)
	{
		this.valuteTo = valuteTo;
	}

}
