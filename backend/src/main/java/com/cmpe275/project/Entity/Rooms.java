package com.cmpe275.project.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
//@Table(name = "rooms", uniqueConstraints = {
//		@UniqueConstraint(columnNames={"roomNumber", "propertyId"})
//})
@Table(name = "Rooms")
public class Rooms implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roomId")
	private int roomId;
	
	@Column(name="propertyId")
	private int propertyId;
	
	@Column(name = "roomSpace")
	private int roomSpace;
	
	@Column(name = "bath")
	private String bath;
	
	@Column(name = "shower")
	private String shower;
	
	@Column(name = "weekendPrice")
	private int weekendPrice;
	
	@Column(name = "weekdayPrice")
	private int weekdayPrice;
	
	@Column(name = "always")
	private String always;
	
	@Column(name = "monday")
	private String monday;
	
	@Column(name = "tuesday")
	private String tuesday;
	
	@Column(name = "wednesday")
	private String wednesday;
	
	@Column(name = "thursday")
	private String thursday;
	
	@Column(name = "friday")
	private String friday;
	
	@Column(name = "saturday")
	private String saturday;
	
	@Column(name = "sunday")
	private String sunday;

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public int getRoomSpace() {
		return roomSpace;
	}

	public void setRoomSpace(int roomSpace) {
		this.roomSpace = roomSpace;
	}

	public String getBath() {
		return bath;
	}

	public void setBath(String bath) {
		this.bath = bath;
	}

	public String getShower() {
		return shower;
	}

	public void setShower(String shower) {
		this.shower = shower;
	}

	public int getWeekendPrice() {
		return weekendPrice;
	}

	public void setWeekendPrice(int weekendPrice) {
		this.weekendPrice = weekendPrice;
	}

	public int getWeekdayPrice() {
		return weekdayPrice;
	}

	public void setWeekdayPrice(int weekdayPrice) {
		this.weekdayPrice = weekdayPrice;
	}

	public String getAlways() {
		return always;
	}

	public void setAlways(String always) {
		this.always = always;
	}

	public String getMonday() {
		return monday;
	}

	public void setMonday(String monday) {
		this.monday = monday;
	}

	public String getTuesday() {
		return tuesday;
	}

	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
	}

	public String getWednesday() {
		return wednesday;
	}

	public void setWednesday(String wednesday) {
		this.wednesday = wednesday;
	}

	public String getThursday() {
		return thursday;
	}

	public void setThursday(String thursday) {
		this.thursday = thursday;
	}

	public String getFriday() {
		return friday;
	}

	public void setFriday(String friday) {
		this.friday = friday;
	}

	public String getSaturday() {
		return saturday;
	}

	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}

	public String getSunday() {
		return sunday;
	}

	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

}
