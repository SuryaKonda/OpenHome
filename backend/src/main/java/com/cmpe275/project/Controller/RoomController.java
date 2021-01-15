package com.cmpe275.project.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.project.Entity.BookingDetails;
import com.cmpe275.project.Entity.Property;
import com.cmpe275.project.Entity.Rooms;
import com.cmpe275.project.Entity.User;
import com.cmpe275.project.Repository.BookingDao;
import com.cmpe275.project.Repository.PropertyDao;
import com.cmpe275.project.Repository.RoomDao;
import com.cmpe275.project.Service.BookingService;
import com.cmpe275.project.Service.PropertyService;
import com.cmpe275.project.Service.RoomService;
import com.cmpe275.project.Service.TimeService;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class RoomController {
	@Autowired
	PropertyService propertyService;
	@Autowired
	RoomService roomService;
	
	@Autowired(required = true)
	PropertyDao propertyDao;
	@Autowired
	BookingService bookingService;
	
	@Autowired(required = true)
	RoomDao roomDao;
	
	@Autowired(required = true)
	BookingDao bookingDao;
	@Autowired
	TimeService timeservice;

	@PostMapping("/addRoom")
	public ResponseEntity<Object> addPropertyRoom(@RequestBody Map<String, String> body){
		System.out.println("Inside addPropertyRoom post");
		
		int propertyId = Integer.parseInt(body.get("propertyId"));
		int roomSpace = Integer.parseInt(body.get("roomSpace"));
		String bath = body.get("bath");
		String shower = body.get("shower");
		int weekendPrice = Integer.parseInt(body.get("weekendPrice"));
		int weekdayPrice = Integer.parseInt(body.get("weekdayPrice"));
		String always = body.get("always");
		String monday = body.get("monday");
		String tuesday = body.get("tuesday");
		String wednesday = body.get("wednesday");
		String thursday = body.get("thursday");
		String friday = body.get("friday");
		String saturday = body.get("saturday");
		String sunday = body.get("sunday");
		
		Rooms room = new Rooms();
		
		room.setPropertyId(propertyId);
		room.setRoomSpace(roomSpace);
		room.setBath(bath);
		room.setShower(shower);
		room.setWeekendPrice(weekendPrice);
		room.setWeekdayPrice(weekdayPrice);
		room.setAlways(always);
		room.setMonday(monday);
		room.setTuesday(tuesday);
		room.setWednesday(wednesday);
		room.setThursday(thursday);
		room.setFriday(friday);
		room.setSaturday(saturday);
		room.setSunday(sunday);
		
		try {
			room = roomDao.save(room);
			System.out.println("Room saved!!!!");
			
			List<Property> propertyDetails = propertyService.getPropertyDetails(propertyId);
			System.out.println("propertyDetails"+propertyDetails.get(0).getMaxPrice());
			System.out.println("-------------");
			int maximumPrice = propertyDetails.get(0).getMaxPrice();
			int minimumPrice = propertyDetails.get(0).getMinPrice();
			int final_maxPrice = maximumPrice;
			int final_minPrice = minimumPrice;
			
			if(maximumPrice < weekendPrice) {
				if(maximumPrice < weekdayPrice) {
					if(weekendPrice > weekdayPrice) {
						final_maxPrice = weekendPrice;
					}else {
						final_maxPrice = weekdayPrice;
					}
				}else {
					final_maxPrice = weekendPrice;
				}
			}else {
				if(maximumPrice < weekdayPrice) {
					final_maxPrice = weekdayPrice;
				}
			}
			//minPrice
			
			if(minimumPrice > weekendPrice) {
				if(minimumPrice > weekdayPrice) {
					if(weekendPrice < weekdayPrice) {
						final_minPrice = weekendPrice;
					}else {
						final_minPrice = weekdayPrice;
					}
				}else {
					final_minPrice = weekendPrice;
				}
			}else {
				if(minimumPrice > weekdayPrice) {
					final_minPrice = weekdayPrice;
				}
			}
			
			Property property = new Property();
			property.setPropertyId(propertyId);
			property.setMaxPrice(final_maxPrice);
			property.setMinPrice(final_minPrice);
			
			try{
				//property = propertyDao.save(property);
				propertyService.updatePropertyDetails(final_maxPrice, final_minPrice,propertyId);
				return new ResponseEntity<>("Room saved!", HttpStatus.OK);
			}catch(Exception e) {
				System.out.println(e);
				return new ResponseEntity<>("could not update prices !", HttpStatus.BAD_REQUEST);
			}
			
		}catch(Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("could not save room!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/getOwnerRooms")
	public ResponseEntity<Object> getOwnerRooms(@RequestBody Rooms body) throws ParseException{
		System.out.println("Inside getOwnerRooms");
		int propertyId = body.getPropertyId();
		System.out.println("property ID is "+propertyId);
		List<Rooms> room = roomDao.getByPropertyIdRooms(propertyId);
		if(room!=null && room.size() > 0) {
			return new ResponseEntity<>(room, HttpStatus.OK);	
	} else{
		return new ResponseEntity<>("Rooms not found", HttpStatus.NOT_FOUND);
	}
	}
	
	@PostMapping("/getAvailableRooms")
	public ResponseEntity<Object> getAvailableRooms(@RequestBody BookingDetails req)throws ParseException, java.text.ParseException{
		
		int propertyId = req.getPropertyId();
			
		//This code is to find the days of week in the booking window
		
		String startDate = req.getBookingStartDate();
		System.out.println("startDate"+startDate);
		String endDate = req.getBookingEndDate();
		System.out.println("endDate"+endDate);
		
		String s_test = startDate+" "+ "15:00:00";
		String e_test = endDate+" "+ "03:00:00";
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		
		Date start_date = formatter.parse(s_test);
		Date end_date = formatter.parse(e_test);
		
		Calendar start_days = Calendar.getInstance();
		Calendar end_days = Calendar.getInstance();
		
		Set<Integer> day_of_week = new HashSet<>();
		Map<String,String> days_db = new HashMap<>();
		
		String sun="false";String mon="false";String tue="false";String wed="false";String thurs="false";String fri="false";String sat="false";
		
		start_days.setTime(start_date);
		end_days.setTime(end_date);
		
		while(start_days.before(end_days)) {
			day_of_week.add(start_days.get(Calendar.DAY_OF_WEEK));
			start_days.add(Calendar.DATE, 1);
			
		}
		
		day_of_week.add(start_days.get(Calendar.DAY_OF_WEEK));
		
		for(Integer AD:day_of_week)
			System.out.print("*"+AD);
		
			if(day_of_week.contains(1)) {
			sun="true";
			
			days_db.put("sunday","true");
		}
		if(day_of_week.contains(2)) {
			days_db.put("monday","true");
		}
		if(day_of_week.contains(3)) {
			days_db.put("tuesday","true");
		}
		if(day_of_week.contains(4)) {
			days_db.put("wednesday","true");
		}
		if(day_of_week.contains(5)) {
			days_db.put("thursday","true");
		}
		if(day_of_week.contains(6)) {
			days_db.put("friday","true");
		}
		if(day_of_week.contains(7)) {
			days_db.put("saturday","true");
		}
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&");
		System.out.println(sun);
		System.out.println(mon);
		System.out.println(tue);
		System.out.println(wed);
		System.out.println(thurs);
		System.out.println(fri);
		System.out.println(sat);
		
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&");
		
		//--------------------------------------------------------------------
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		start.setTime(start_date);
		end.setTime(end_date);
		
		//List<Rooms> rooms_list = roomService.findRoomsByPropertyId(propertyId);
		
		List<Rooms> rooms_list = roomDao.findRoomsByPropertyId(propertyId);
		
		
		Set<Integer> set_room = new HashSet<>();
		List<Integer> not_available_rooms_id = new ArrayList<>(); 
		List<Integer> available_rooms_id = new ArrayList<>();
		List<Rooms>final_room = new ArrayList<>();
		
		for(Rooms room:rooms_list){
			System.out.println("roomId:"+room.getRoomId());
			set_room.add(room.getRoomId());	
		}
		
		for(Integer set_r:set_room)
			System.out.println("Set Room:"+set_r);
		
		
		List<BookingDetails> bookingDetails = bookingService.findBookingDetailsByPropertyId(propertyId);
		System.out.println("size:"+bookingDetails.size());
		
		
		Calendar start_b = Calendar.getInstance();
		Calendar end_b = Calendar.getInstance();
		
		for(int j=0;j<bookingDetails.size();j++) {
			
			String tempStartDate = bookingDetails.get(j).getBookingStartDate();
			System.out.println("startDate"+tempStartDate);
			String tempEndDate = bookingDetails.get(j).getBookingEndDate();
			System.out.println("endDate"+tempEndDate);
			
			String start_test = tempStartDate+" "+ "15:00:00";
			String end_test = tempEndDate+" "+ "03:00:00";
			
			//SimpleDateFormat  = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
			
			Date temp_start_date = formatter.parse(start_test);
			Date temp_end_date = formatter.parse(end_test);
			
				System.out.println("ROOOOMMM"+bookingDetails.get(j).getRoomId());
				start_b.setTime(temp_start_date);
				end_b.setTime(temp_end_date);
				
				
				System.out.println("+++++++++++++++++++++");
				
				System.out.println(start.before(start_b));
				System.out.println(start.after(end_b));
				
				System.out.println(end.before(start_b));
				System.out.println(end.after(end_b));
				
				System.out.println("==========================");
				
				System.out.println((start.before(start_b)||start.after(end_b)) && (end.before(start_b)||end.after(end_b)));
				
				
				boolean flag=(start.before(start_b)||start.after(end_b)) && (end.before(start_b)||end.after(end_b));
				
				if(!flag) {
					System.out.println("Status"+bookingDetails.get(j).getBookingStatus());
					String status = bookingDetails.get(j).getBookingStatus();
					String expected = "cancel";
					System.out.println(status);
					if(!status.equals(expected)) {
						System.out.println("SSSSSSSSSSSSSSSS");
					not_available_rooms_id.add(bookingDetails.get(j).getRoomId());
					
					}
				}	
		}
		
		for(int i=0;i<not_available_rooms_id.size();i++) {
			System.out.println(not_available_rooms_id.get(i));
		}
		
		for(int p=0;p<not_available_rooms_id.size();p++) {
			System.out.println("Na"+not_available_rooms_id.get(p));
			set_room.remove(not_available_rooms_id.get(p));
		}
		
		for(Integer set_r:set_room)
			System.out.println("Set Room:"+set_r);
		
		
		for(Integer set_r:set_room) {
			System.out.println("inside set_r");
			
			System.out.println(set_r);
			
		     Rooms temp_room = roomDao.findRoomsByRoomId(set_r);
		     
		     Map<String,String> temp_map = new HashMap<>();
		     
			temp_map.put("sunday",temp_room.getSunday());
			temp_map.put("monday",temp_room.getMonday());
			temp_map.put("tuesday",temp_room.getTuesday());
			temp_map.put("wednesday",temp_room.getWednesday());
			temp_map.put("thursday",temp_room.getThursday());
			temp_map.put("friday",temp_room.getFriday());
			temp_map.put("saturday",temp_room.getSaturday());
			
			boolean flag = true;
			for(Map.Entry<String,String> entries : days_db.entrySet()){
				
				String key = entries.getKey();
				String value1 = entries.getValue();
				String value2 = temp_map.get(key);
				
				if(!value1.equalsIgnoreCase(value2)) {
					flag = false;
					break;
				}	
			}
			if(flag) {
				available_rooms_id.add(set_r);
			}
		}
		if(available_rooms_id.size()>0) {
			
			for(int i=0;i<available_rooms_id.size();i++) {
				
				//System.out.println("Final!"+available_rooms_id.get(i));
				Rooms room = roomDao.findRoomsByRoomId(available_rooms_id.get(i));
				final_room.add(room);
			}
			return new ResponseEntity<>(final_room,HttpStatus.OK);		
		}else {
			return new ResponseEntity<>("No Rooms Available",HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/updateRoom")
	public ResponseEntity<Object> updateRoom(@RequestBody Rooms req) throws java.text.ParseException{
		System.out.println("Inside updateRoom post");
		
		int roomId = req.getRoomId();
		System.out.println("roomId"+roomId);
		
		//Set<BookingDetails> bookingDetails = bookingService.findBookingByRoomId(roomId);
		
		List<BookingDetails> bookingDetails = bookingService.findBookingByRoomId(roomId);
		
		System.out.println("size"+bookingDetails.size());
		
		Set<BookingDetails> cancelledBooking  = new HashSet<>();
		
		Rooms room = roomDao.findRoomsByRoomId(roomId);
		System.out.println("roomDetails"+room.getSunday());
				
		if(room.getSunday().equalsIgnoreCase("true") && req.getSunday().equalsIgnoreCase("false")) {
			
			
			System.out.println("**************");
			Map<String,String> days_map = new HashMap<>();
			
			//iterate through each booking to find which falls on sunday
			for(BookingDetails eachBooking:bookingDetails) {
				
				if(!cancelledBooking.contains(eachBooking)) {

				System.out.println("bookingDetails size"+bookingDetails.size()+"bookingID"+eachBooking.getBookingId());
				String start_date = eachBooking.getBookingStartDate();
				String end_date = eachBooking.getBookingEndDate();
				int roomBookingId = eachBooking.getBookingId();
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
				
				Date s_date = formatter.parse(start_date);
				Date e_date = formatter.parse(end_date);
				
				Calendar start = Calendar.getInstance();
				
				Calendar end = Calendar.getInstance();
				
				start.setTime(s_date);
				end.setTime(e_date);
				
				
				///------------------------check for current date-------------------------------
				Date date = new Date();
				date = date.from(timeservice.getCurrentTime().toInstant());
				//number of days between current day and bookingStartDay--------------------------
				int days = (int) ((s_date.getTime()-date.getTime())/86400000);
				System.out.println("days"+days);
				while(start.before(end)) {
					
					if(start.get(Calendar.DAY_OF_WEEK)==1) {
						days_map.put("sunday","true");
						break;	
					}else {
						days_map.put("sunday","false");
					}
					start.add(Calendar.DATE, 1);
				}
				
				if(days_map.get("sunday").equalsIgnoreCase("true")){
					
					if(days>7) {
						//cancel the booking full refund 
						
						System.out.println(">>>>>>7");
						bookingDao.hostCancelBooking("cancel", 0, roomBookingId);
						cancelledBooking.add(eachBooking);
						System.out.println(cancelledBooking.size());
					}
					else {
						System.out.println("<<<<<<<< 7");
						//cancel booking with 15% penalty on total price
						int refund_amount = (int) (eachBooking.getTotalPrice()*(.15));
						bookingDao.hostCancelBooking("cancel", refund_amount, roomBookingId);
						cancelledBooking.add(eachBooking);
					}
				}
			}
			}	
		}
		
	//check for monday-------------------------------------------------
		
if(room.getMonday().equalsIgnoreCase("true") && req.getMonday().equalsIgnoreCase("false")) {
			
			System.out.println("**************");
			Map<String,String> days_map = new HashMap<>();
			
			//iterate through each booking to find which falls on sunday
			for(BookingDetails eachBooking:bookingDetails) {
				
				if(!cancelledBooking.contains(eachBooking)) {

				System.out.println("bookingDetails size"+bookingDetails.size()+"bookingID"+eachBooking.getBookingId());
				System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
				String start_date = eachBooking.getBookingStartDate();
				String end_date = eachBooking.getBookingEndDate();
				int roomBookingId = eachBooking.getBookingId();
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
				
				Date s_date = formatter.parse(start_date);
				Date e_date = formatter.parse(end_date);
				
				Calendar start = Calendar.getInstance();
				
				Calendar end = Calendar.getInstance();
				
				start.setTime(s_date);
				end.setTime(e_date);
				
				
				///------------------------check for current date-------------------------------
				Date date = new Date();
				date = date.from(timeservice.getCurrentTime().toInstant());
				//number of days between current day and bookingStartDay--------------------------
				int days = (int) ((s_date.getTime()-date.getTime())/86400000);
				System.out.println("days"+days);
				
				
				while(start.before(end)) {
					
					if(start.get(Calendar.DAY_OF_WEEK)==2) {
						days_map.put("monday","true");
						break;	
					}else {
						days_map.put("monday","false");
					}
					start.add(Calendar.DATE, 1);
				}
				
				if(days_map.get("monday").equalsIgnoreCase("true")){
					
					if(days>7) {
						//cancel the booking full refund 
						
						System.out.println(">>>>>>7");
						bookingDao.hostCancelBooking("cancel", 0, roomBookingId);
						System.out.println("remove check %%%%%%%%%%%%%%%%%%");
						System.out.println(bookingDetails.size());
						cancelledBooking.add(eachBooking);
						System.out.println(cancelledBooking.size());
						
					}
					else {
						
						System.out.println("<<<<<<<< 7");
						//cancel booking with 15% penalty on total price
						int refund_amount = (int) (eachBooking.getTotalPrice()*(.15));
						bookingDao.hostCancelBooking("cancel", refund_amount, roomBookingId);
						cancelledBooking.add(eachBooking);
						
					}
				}
					
			}
			}	
		}

System.out.println("++++++++++++++++++++++++outside monday");

	// check for tuesday--------------------------------
if(room.getTuesday().equalsIgnoreCase("true") && req.getTuesday().equalsIgnoreCase("false")) {
	
	
	System.out.println("**************");
	Map<String,String> days_map = new HashMap<>();
	
	//iterate through each booking to find which falls on sunday
	for(BookingDetails eachBooking:bookingDetails) {
		
		if(!cancelledBooking.contains(eachBooking)) {

		System.out.println("bookingDetails size"+bookingDetails.size()+"bookingID"+eachBooking.getBookingId());
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		String start_date = eachBooking.getBookingStartDate();
		String end_date = eachBooking.getBookingEndDate();
		int roomBookingId = eachBooking.getBookingId();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		
		Date s_date = formatter.parse(start_date);
		Date e_date = formatter.parse(end_date);
		
		Calendar start = Calendar.getInstance();
		
		Calendar end = Calendar.getInstance();
		
		start.setTime(s_date);
		end.setTime(e_date);
		
		
		///------------------------check for current date-------------------------------
		Date date = new Date();
		date = date.from(timeservice.getCurrentTime().toInstant());
		//number of days between current day and bookingStartDay--------------------------
		int days = (int) ((s_date.getTime()-date.getTime())/86400000);
		System.out.println("days"+days);
		
		
		while(start.before(end)) {
			
			if(start.get(Calendar.DAY_OF_WEEK)==3) {
				days_map.put("tuesday","true");
				break;	
			}else {
				days_map.put("tuesday","false");
			}
			start.add(Calendar.DATE, 1);
		}
		
		if(days_map.get("tuesday").equalsIgnoreCase("true")){
			
			if(days>7) {
				//cancel the booking full refund 
				
				System.out.println(">>>>>>7");
				bookingDao.hostCancelBooking("cancel", 0, roomBookingId);
				System.out.println("remove check %%%%%%%%%%%%%%%%%%");
				System.out.println(bookingDetails.size());
				cancelledBooking.add(eachBooking);
				System.out.println(cancelledBooking.size());
				
			}
			else {
				
				System.out.println("<<<<<<<< 7");
				//cancel booking with 15% penalty on total price
				int refund_amount = (int) (eachBooking.getTotalPrice()*(.15));
				bookingDao.hostCancelBooking("cancel", refund_amount, roomBookingId);
				cancelledBooking.add(eachBooking);
				
			}
		}
			
	}
	}	
}
//---------------check for wednesday--------------
if(room.getWednesday().equalsIgnoreCase("true") && req.getWednesday().equalsIgnoreCase("false")) {
	
	
	System.out.println("**************");
	Map<String,String> days_map = new HashMap<>();
	
	//iterate through each booking to find which falls on sunday
	for(BookingDetails eachBooking:bookingDetails) {
		
		if(!cancelledBooking.contains(eachBooking)) {

		System.out.println("bookingDetails size"+bookingDetails.size()+"bookingID"+eachBooking.getBookingId());
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		String start_date = eachBooking.getBookingStartDate();
		String end_date = eachBooking.getBookingEndDate();
		int roomBookingId = eachBooking.getBookingId();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		
		Date s_date = formatter.parse(start_date);
		Date e_date = formatter.parse(end_date);
		
		Calendar start = Calendar.getInstance();
		
		Calendar end = Calendar.getInstance();
		
		start.setTime(s_date);
		end.setTime(e_date);
		
		
		///------------------------check for current date-------------------------------
		Date date = new Date();
		date = date.from(timeservice.getCurrentTime().toInstant());
		//number of days between current day and bookingStartDay--------------------------
		int days = (int) ((s_date.getTime()-date.getTime())/86400000);
		System.out.println("days"+days);
		
		
		while(start.before(end)) {
			
			if(start.get(Calendar.DAY_OF_WEEK)==4) {
				days_map.put("wednesday","true");
				break;	
			}else {
				days_map.put("wednesday","false");
			}
			start.add(Calendar.DATE, 1);
		}
		
		if(days_map.get("wednesday").equalsIgnoreCase("true")){
			
			if(days>7) {
				//cancel the booking full refund 
				
				System.out.println(">>>>>>7");
				bookingDao.hostCancelBooking("cancel", 0, roomBookingId);
				System.out.println("remove check %%%%%%%%%%%%%%%%%%");
				System.out.println(bookingDetails.size());
				cancelledBooking.add(eachBooking);
				System.out.println(cancelledBooking.size());
				
			}
			else {
				
				System.out.println("<<<<<<<< 7");
				//cancel booking with 15% penalty on total price
				int refund_amount = (int) (eachBooking.getTotalPrice()*(.15));
				bookingDao.hostCancelBooking("cancel", refund_amount, roomBookingId);
				cancelledBooking.add(eachBooking);
				
			}
		}
			
	}
	}	
}
//-------------------check for thursday--------------------
if(room.getThursday().equalsIgnoreCase("true") && req.getThursday().equalsIgnoreCase("false")) {
	
	
	System.out.println("**************");
	Map<String,String> days_map = new HashMap<>();
	
	//iterate through each booking to find which falls on sunday
	for(BookingDetails eachBooking:bookingDetails) {
		
		if(!cancelledBooking.contains(eachBooking)) {

		System.out.println("bookingDetails size"+bookingDetails.size()+"bookingID"+eachBooking.getBookingId());
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		String start_date = eachBooking.getBookingStartDate();
		String end_date = eachBooking.getBookingEndDate();
		int roomBookingId = eachBooking.getBookingId();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		
		Date s_date = formatter.parse(start_date);
		Date e_date = formatter.parse(end_date);
		
		Calendar start = Calendar.getInstance();
		
		Calendar end = Calendar.getInstance();
		
		start.setTime(s_date);
		end.setTime(e_date);
		
		
		///------------------------check for current date-------------------------------
		Date date = new Date();
		date = date.from(timeservice.getCurrentTime().toInstant());
		//number of days between current day and bookingStartDay--------------------------
		int days = (int) ((s_date.getTime()-date.getTime())/86400000);
		System.out.println("days"+days);
		
		
		while(start.before(end)) {
			
			if(start.get(Calendar.DAY_OF_WEEK)==5) {
				days_map.put("thursday","true");
				break;	
			}else {
				days_map.put("thursday","false");
			}
			start.add(Calendar.DATE, 1);
		}
		
		if(days_map.get("thursday").equalsIgnoreCase("true")){
			
			if(days>7) {
				//cancel the booking full refund 
				
				System.out.println(">>>>>>7");
				bookingDao.hostCancelBooking("cancel", 0, roomBookingId);
				System.out.println("remove check %%%%%%%%%%%%%%%%%%");
				System.out.println(bookingDetails.size());
				cancelledBooking.add(eachBooking);
				System.out.println(cancelledBooking.size());
				
			}
			else {
				
				System.out.println("<<<<<<<< 7");
				//cancel booking with 15% penalty on total price
				int refund_amount = (int) (eachBooking.getTotalPrice()*(.15));
				bookingDao.hostCancelBooking("cancel", refund_amount, roomBookingId);
				
				cancelledBooking.add(eachBooking);
				
			}
		}
			
	}
	}	
}
	
///////////check for friday-----------

if(room.getFriday().equalsIgnoreCase("true") && req.getFriday().equalsIgnoreCase("false")){
	
	
	Map<String,String> days_map = new HashMap<>();
	
	//iterate through each booking to find which falls on sunday
	for(BookingDetails eachBooking:bookingDetails) {
		
		if(!cancelledBooking.contains(eachBooking)) {

		System.out.println("bookingDetails size"+bookingDetails.size()+"bookingID"+eachBooking.getBookingId());
		String start_date = eachBooking.getBookingStartDate();
		String end_date = eachBooking.getBookingEndDate();
		int roomBookingId = eachBooking.getBookingId();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		
		Date s_date = formatter.parse(start_date);
		Date e_date = formatter.parse(end_date);
		
		Calendar start = Calendar.getInstance();
		
		Calendar end = Calendar.getInstance();
		
		start.setTime(s_date);
		end.setTime(e_date);
		
		
		///------------------------check for current date-------------------------------
		Date date = new Date();
		date = date.from(timeservice.getCurrentTime().toInstant());
		//number of days between current day and bookingStartDay--------------------------
		int days = (int) ((s_date.getTime()-date.getTime())/86400000);
		System.out.println("days"+days);
		
		
		while(start.before(end)) {
			
			if(start.get(Calendar.DAY_OF_WEEK)==6) {
				days_map.put("friday","true");
				break;	
			}else {
				days_map.put("friday","false");
			}
			start.add(Calendar.DATE, 1);
		}
		
		if(days_map.get("friday").equalsIgnoreCase("true")){
			
			if(days>7) {
				//cancel the booking full refund 
				bookingDao.hostCancelBooking("cancel", 0, roomBookingId);
				System.out.println(bookingDetails.size());
				cancelledBooking.add(eachBooking);
				System.out.println(cancelledBooking.size());
			}
			else {
				//cancel booking with 15% penalty on total price
				int refund_amount = (int) (eachBooking.getTotalPrice()*(.15));
				bookingDao.hostCancelBooking("cancel", refund_amount, roomBookingId);
				cancelledBooking.add(eachBooking);
			}
		}
	}
	}	
}
//----------check for saturday---------------

if(room.getSaturday().equalsIgnoreCase("true") && req.getSaturday().equalsIgnoreCase("false")){
	
	
	System.out.println("**************");
	Map<String,String> days_map = new HashMap<>();
	
	//iterate through each booking to find which falls on sunday
	for(BookingDetails eachBooking:bookingDetails) {
		
		if(!cancelledBooking.contains(eachBooking)) {

		System.out.println("bookingDetails size"+bookingDetails.size()+"bookingID"+eachBooking.getBookingId());
		String start_date = eachBooking.getBookingStartDate();
		String end_date = eachBooking.getBookingEndDate();
		int roomBookingId = eachBooking.getBookingId();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		
		Date s_date = formatter.parse(start_date);
		Date e_date = formatter.parse(end_date);
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		start.setTime(s_date);
		end.setTime(e_date);
		
		///------------------------check for current date-------------------------------
		Date date = new Date();
		date = date.from(timeservice.getCurrentTime().toInstant());
		//number of days between current day and bookingStartDay--------------------------
		int days = (int) ((s_date.getTime()-date.getTime())/86400000);
		System.out.println("days"+days);
		
		
		while(start.before(end)) {
			
			if(start.get(Calendar.DAY_OF_WEEK)==7) {
				days_map.put("saturday","true");
				break;	
			}else {
				days_map.put("saturday","false");
			}
			start.add(Calendar.DATE, 1);
		}
		
		if(days_map.get("saturday").equalsIgnoreCase("true")){
			
			if(days>7) {
				//cancel the booking full refund 
				
				System.out.println(">>>>>>7");
				bookingDao.hostCancelBooking("cancel", 0, roomBookingId);
				System.out.println("remove check %%%%%%%%%%%%%%%%%%");
				System.out.println(bookingDetails.size());
				cancelledBooking.add(eachBooking);
				System.out.println(cancelledBooking.size());
				
			}
			else {
				
				System.out.println("<<<<<<<< 7");
				//cancel booking with 15% penalty on total price
				int refund_amount = (int) (eachBooking.getTotalPrice()*(.15));
				bookingDao.hostCancelBooking("cancel", refund_amount, roomBookingId);
				cancelledBooking.add(eachBooking);
			}
		}
	}
	}	
}

		for(BookingDetails cancelled:cancelledBooking) {
			System.out.println(cancelled);
			//System.out.println("size"+);
		}
		
		System.out.println("Final Size"+cancelledBooking);
		
		int weekendPrice = req.getWeekendPrice();
		int weekdayPrice = req.getWeekdayPrice();
		String sunday = req.getSunday();
		String monday = req.getMonday();
		String tuesday = req.getTuesday();
		String wednesday = req.getWednesday();
		String thursday = req.getThursday();
		String friday = req.getFriday();
		String saturday = req.getSaturday();

		roomService.updateRoom(weekendPrice, weekdayPrice, sunday, monday, tuesday, wednesday, 
							thursday, friday, saturday,roomId);
		
		return new ResponseEntity<>("Room details updated",HttpStatus.OK);
	}
	
}
