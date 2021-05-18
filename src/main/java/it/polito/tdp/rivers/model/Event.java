package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		TRACIMAZIONE, //C>Q
		AUMENTO_LIV, //f_in>f_out
		DIMINUZIONE_LIV, //f_in<f_out
		IRRIGAZIONE_CAMPI, //f_out=10*f_out_min
	};
	
	private LocalDate time;
	private EventType type;
	private double f_in;
	private double f_out;
	
	public Event(EventType type, LocalDate time, double fIn, double fOut) {
		super();
		this.type = type;
		this.time = time;
		this.f_in=fIn;
		this.f_out=fOut;
	}

	public LocalDate getTime() {
		return time;
	}

	public void setTime(LocalDate time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public double getF_in() {
		return f_in;
	}

	public void setF_in(double f_in) {
		this.f_in = f_in;
	}

	public double getF_out() {
		return f_out;
	}

	public void setF_out(double f_out) {
		this.f_out = f_out;
	}
	
	public int compareTo(Event other) {
		return this.time.compareTo(other.time);
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + ", f_in=" + f_in + ", f_out=" + f_out + "]";
	}

}
