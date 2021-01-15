package com.cmpe275.project.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "PropertyDetails", uniqueConstraints = {
		@UniqueConstraint(columnNames={"ownerId", "propertyName"})
})
public class Property implements Serializable {
	private static final long serialVersionUID = 1L;	
	
	public Property() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "propertyId")
	private int propertyId;

	@Column(name = "ownerId")
	private int ownerId;
	
	@Column(name = "ownerName")
	private String ownerName;
	
	@Column(name = "ownerEmail")
	private String ownerEmail;
	
	@Column(name = "propertyName")
	private String propertyName;
	
	@Column(name = "propertyPhone")
	private String propertyPhone;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "zip")
	private String zip;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "propertyImage")
	private String propertyImage;
	
	@Column(name = "sharingType")
	private String sharingType;
	
	@Column(name = "propertyType")
	private String propertyType;
	
	@Column(name = "totalSpace")
	private int totalSpace;
	
	@Column(name = "parkingAvailability")
	private String parkingAvailability;
	
	@Column(name = "freeParking")
	private String freeParking;
	
	@Column(name = "parkingFee")
	private int parkingFee;
	
	@Column(name = "wifi")
	private String wifi;
	
	@Column(name = "minPrice")
	private int minPrice;
	
	@Column(name = "maxPrice")
	private int maxPrice;

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyPhone() {
		return propertyPhone;
	}

	public void setPropertyPhone(String propertyPhone) {
		this.propertyPhone = propertyPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getPropertyImage() {
		return propertyImage;
	}

	public void setPropertyImage(String propertyImage) {
		this.propertyImage = propertyImage;
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

	public int getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}
	
		
}
