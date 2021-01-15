package com.cmpe275.project.Entity;

import javax.persistence.Column;

public class NewSearch {

	
	private String sharingType;
	
	
	private String propertyType;
	private int totalSpace;
	private String parkingAvailability;
	private String freeParking;
	private int parkingFee;
	private String wifi;
	private int maxPrice;
	private int minPrice;
	private String description;
	private String city;
	private String zip;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSharingType() {
		return sharingType;
	}

	public void setSharingType(String sharingType) {
		this.sharingType = sharingType;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public int getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(int totalSpace) {
		this.totalSpace = totalSpace;
	}

	public String getParkingAvailability() {
		return parkingAvailability;
	}

	public void setParkingAvailability(String parkingAvailability) {
		this.parkingAvailability = parkingAvailability;
	}

	public String getFreeParking() {
		return freeParking;
	}

	public void setFreeParking(String freeParking) {
		this.freeParking = freeParking;
	}

	public int getParkingFee() {
		return parkingFee;
	}

	public void setParkingFee(int parkingFee) {
		this.parkingFee = parkingFee;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}

	public int getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}
}
