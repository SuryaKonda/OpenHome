package com.cmpe275.project.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cmpe275.project.Entity.BookingDetails;
import com.cmpe275.project.Entity.Rooms;

public interface RoomDao extends JpaRepository<Rooms,Integer> {
	
	@Query(value = "select * from rooms where room_id = ?1", nativeQuery = true)
	ArrayList<Rooms> findRoomsById(int roomId);
	
	Rooms findRoomsByRoomId(int roomId);
	
	ArrayList<Rooms> findRoomsByPropertyId(int propertyId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE rooms SET weekend_price =?1,weekday_price=?2, sunday=?3, monday=?4,tuesday =?5, "
			+ "wednesday =?6,thursday=?7, friday=?8, saturday=?9 WHERE room_id=?10", nativeQuery = true)
	void updateRoomByRoomId(int weekendPrice,int weekdayPrice,String sunday,String monday,String tuesday,String wednesday, 
			String thursday,String friday,String saturday,int roomId);
	
	@Query(value = "select * from rooms where property_id = ?1", nativeQuery = true)
	ArrayList<Rooms> getByPropertyIdRooms(int propertyId);
	
	
}
