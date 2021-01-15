package com.cmpe275.project.Service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.project.Entity.Property;
import com.cmpe275.project.Entity.User;
import com.cmpe275.project.Repository.PropertyDao;

@Service
@Transactional
public class PropertyService {
	
	@Autowired
	PropertyDao propertyDao;
	
	public List<Property> getPropertyDetails(int propertyId){
		return this.propertyDao.getPropertyDetailsById(propertyId);
	}
	
	public void updatePropertyDetails(int final_maxPrice, int final_minPrice,int propertyId){
		System.out.println("inside property service");
		this.propertyDao.updateProperty(final_maxPrice, final_minPrice,propertyId);
	}
	
	public List<Property> getPropertyByOwnerIdPropertyName(int ownerId, String propertyName){
		return this.propertyDao.getByOwnerIdPropertyName(ownerId,propertyName);
	}
	
	public List<Property> getSearchProperties(String city, String zip, int maxPrice,int minPrice, String sharingType, String propertyType,
			 String wifi, String description){
		return this.propertyDao.SearchProperties(city, zip, maxPrice, minPrice, sharingType, propertyType, wifi,description);
	}
	
	public List<Property> getOwnerProperties(int ownerId){
		return this.propertyDao.getOwnerProps(ownerId);
	}
	
}
