package com.cmpe275.project.Service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.cmpe275.project.Entity.BookingDetails;
import com.cmpe275.project.Entity.Payments;
import com.cmpe275.project.Entity.Rooms;
import com.cmpe275.project.Repository.RoomDao;

@Service
@Transactional
public class RoomService {
	
	
	@Autowired
	RoomDao roomDao;
	
	public List<Rooms> findRoomsByRoomId(int roomId){
		return this.roomDao.findRoomsById(roomId);
	}
	
	public ArrayList<Rooms> getOwnerRooms(int propertyId){
		return this.roomDao.findRoomsByPropertyId(propertyId);
	}
	
	public void updateRoom(int weekendPrice,int weekdayPrice,String sunday,String monday,String tuesday,String wednesday, 
			String thursday,String friday,String saturday,int roomId){
		 this.roomDao.updateRoomByRoomId(weekendPrice, weekdayPrice, sunday, monday, tuesday, wednesday, thursday, friday, saturday,roomId);
	}
	

}
