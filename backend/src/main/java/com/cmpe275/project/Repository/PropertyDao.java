package com.cmpe275.project.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cmpe275.project.Entity.Property;
import com.cmpe275.project.Entity.User;

@SuppressWarnings("rawtypes")
@Repository
//@Qualifier("property")
@Transactional
public interface PropertyDao extends JpaRepository<Property, Integer>, JpaSpecificationExecutor {
	
	@Query(value = "select * from property_details where property_id = ?1", nativeQuery = true)
	ArrayList<Property> getPropertyDetailsById(int propertyId);
	
	@Query(value = "select * from property_details where owner_id = ?1 and property_name = ?2", nativeQuery = true)
	ArrayList<Property> getByOwnerIdPropertyName(int ownerId, String propertyName);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE property_details SET max_price = ?1, min_price = ?2 where property_Id = ?3", nativeQuery = true)
	void updateProperty(int final_maxPrice, int final_minPrice,int propertyId );
	
//	@Query(value = "from Property as pd where pd.city =:city AND pd.zip = :zip AND "
//				+"(:maxPrice is null or pd.maxPrice >= :maxPrice) OR (:minPrice is null or pd.minPrice <= :minPrice) OR"
//			+"(:sharingType is null or pd.sharingType = :sharingType) OR (:propertyType is null or pd.propertyType = :propertyType) OR "
//			+ "(:wifi is null or pd.wifi = :wifi) OR (:description is null or pd.description LIKE '%:description%')")
	@Query(value = "from Property as pd where pd.city =:city AND pd.zip = :zip AND "
			+"(:maxPrice is null or pd.maxPrice <= :maxPrice) AND (:minPrice is null or pd.minPrice >= :minPrice) AND"
		+"(:sharingType is null or pd.sharingType = :sharingType) AND (:propertyType is null or pd.propertyType = :propertyType) AND "
		+ "(:wifi is null or pd.wifi = :wifi) AND (:description is null or pd.description LIKE '%:description%')")
	ArrayList<Property> SearchProperties(String city, String zip,int maxPrice, int minPrice, String sharingType, String propertyType,
							String wifi, String description);
	
	@Query(value = "select * from property_details where owner_id = ?1", nativeQuery = true)
	ArrayList<Property> getOwnerProps(int ownerId);
	
	Property findPropertyByPropertyId(int propertyId);
	
	@Modifying
	@Transactional
	@Query(value="DELETE from property_details WHERE property_id=?1",nativeQuery=true)
	void deletePropertyByPropertyId(int propertyId);
	
	@Modifying
	@Query(value = "UPDATE property_details SET property_name = ?1, description = ?2, property_phone = ?3, property_type = ?4, sharing_type = ?5,"
			+ " parking_availability = ?6, free_parking = ?7, parking_fee = ?8, wifi = ?9 where property_Id = ?10", nativeQuery = true)
	void updateCompleteProperty(String propertyName, String description, String propertyPhone, 
			String propertyType, String sharingType, String parkingAvailability, String freeParking, int parkingFee, String wifi, int propertyId);
	
}
