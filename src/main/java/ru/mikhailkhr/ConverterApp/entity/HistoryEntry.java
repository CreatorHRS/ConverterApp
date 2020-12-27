package ru.mikhailkhr.ConverterApp.entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Class that represent the history entry
 * @author mikhailkhr
 *
 */
public class HistoryEntry {

	private String fromCharCode;
	private String toCharCode;
	private double fromValue;
	private double toValue;
	private LocalTime time;
	private LocalDate date;
	private String fromId;
	private String toId;
	
	public HistoryEntry(String fromCharCode, String toCharCode, double fromValue, double toValue, LocalTime time,
			LocalDate date, String fromId, String toId) {
		this.fromCharCode = fromCharCode;
		this.toCharCode = toCharCode;
		this.fromValue = fromValue;
		this.toValue = toValue;
		this.time = time;
		this.date = date;
		this.fromId = fromId;
		this.toId = toId;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public HistoryEntry() {
	}
	public String getFromCharCode() {
		return fromCharCode;
	}
	public void setFromCharCode(String fromCharCode) {
		this.fromCharCode = fromCharCode;
	}
	public String getToCharCode() {
		return toCharCode;
	}
	public void setToCharCode(String toCharCode) {
		this.toCharCode = toCharCode;
	}
	public double getFromValue() {
		return fromValue;
	}
	public void setFromValue(double fromValue) {
		this.fromValue = fromValue;
	}
	public double getToValue() {
		return toValue;
	}
	public void setToValue(double toValue) {
		this.toValue = toValue;
	}
	public LocalTime getTime() {
		return time;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
	
}
