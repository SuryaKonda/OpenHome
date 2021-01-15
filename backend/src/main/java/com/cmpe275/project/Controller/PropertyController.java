package com.cmpe275.project.Controller;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.project.Entity.BookingDetails;
import com.cmpe275.project.Entity.NewSearch;
import com.cmpe275.project.Entity.Property;
import com.cmpe275.project.Entity.Rooms;
import com.cmpe275.project.Entity.User;
import com.cmpe275.project.Repository.BookingDao;
import com.cmpe275.project.Repository.PropertyDao;
import com.cmpe275.project.Repository.RoomDao;
import com.cmpe275.project.Repository.UserDao;
import com.cmpe275.project.Service.BookingService;
import com.cmpe275.project.Service.EmailVerification;
import com.cmpe275.project.Service.PropertyService;
import com.cmpe275.project.Service.RoomService;
import com.cmpe275.project.Service.TimeService;
import com.cmpe275.project.Service.UserService;


@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class PropertyController {

	@Autowired
	PropertyDao propertyDao;
	@Autowired
	RoomDao roomDao;
	
	@Autowired
	PropertyService propertyService;
	@Autowired
	RoomService roomService;
	@Autowired
	TimeService timeservice;
	
	@Autowired
	BookingService bookingService;
	
	@Autowired
	BookingDao bookingDao;
	
	@Autowired
	EmailVerification emailVerification;
	
	@PersistenceContext
    EntityManager entityManager;
	
	@PostMapping("/addProperty")
	public ResponseEntity<Object> addProperty(@RequestBody Property req){
		System.out.println("Inside addProperty!!!!!");
//		String userEmail = "nidhi.tatturaravindakumar@sjsu.edu";
		
		int ownerId = req.getOwnerId();
		String ownerName = req.getOwnerName();
		String ownerEmail = req.getOwnerEmail();
		String propertyName = req.getPropertyName();
		String propertyPhone = req.getPropertyPhone();
		String address = req.getAddress();
		String city = req.getCity();
		String state = req.getState();
		String zip = req.getZip();
		String description = req.getDescription();
		String propertyImage = req.getPropertyImage();
		String parkingAvailability = req.getParkingAvailability();
		String freeParking = req.getFreeParking();
		int parkingFee = req.getParkingFee();
		String wifi = req.getWifi();
		int totalSpace = req.getTotalSpace();
		String sharingType = req.getSharingType();
		String propertyType = req.getPropertyType();
		int maxPrice = req.getMaxPrice();
		int minPrice = req.getMinPrice();
		
		Property property = new Property();
		
		property.setOwnerId(ownerId);
		property.setOwnerName(ownerName);
		property.setOwnerEmail(ownerEmail);
		property.setPropertyName(propertyName);
		property.setPropertyPhone(propertyPhone);
		property.setAddress(address);
		property.setCity(city);
		property.setState(state);
		property.setZip(zip);
		property.setDescription(description);
		property.setPropertyImage(propertyImage);
		property.setParkingAvailability(parkingAvailability);
		property.setFreeParking(freeParking);
		property.setParkingFee(parkingFee);
		property.setWifi(wifi);
		property.setTotalSpace(totalSpace);
		property.setSharingType(sharingType);
		property.setPropertyType(propertyType);
		property.setMaxPrice(maxPrice);
		property.setMinPrice(minPrice);
		
		try {
			property = propertyDao.save(property);
			//emailVerification.sendPropertyAddEmail(userEmail);
			emailVerification.addProperty(ownerEmail);
			System.out.println("property saved!!!!");
			return new ResponseEntity<>("property saved!", HttpStatus.OK);
		}catch(DataIntegrityViolationException e) {
			return new ResponseEntity<>("Please enter different property name", HttpStatus.OK);
		}catch(ParseException e) {
			return new ResponseEntity<>("Please save property again", HttpStatus.BAD_REQUEST);			
		}		
	}
	
	
	@PostMapping("/getOwnerProperty")
	public ResponseEntity<Object> getOwnerProperty(@RequestBody Property req) throws ParseException{
		System.out.println("In getOwnerProperty post");
		
		int ownerId = req.getOwnerId();
		List<Property> propertyDetails = propertyService.getOwnerProperties(ownerId);
		if(propertyDetails.size() != 0) {
			System.out.println(propertyDetails.size());
			return new ResponseEntity<>(propertyDetails,HttpStatus.OK);
		}else {
			return new ResponseEntity<>("The owner has no property",HttpStatus.OK);
		}
	}
	
	@GetMapping("/getAvailableRooms")
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
		System.out.println(sun);
		System.out.println(mon);
		System.out.println(tue);
		System.out.println(wed);
		System.out.println(thurs);
		System.out.println(fri);
		System.out.println(sat);
		
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
			return new ResponseEntity<>("No Rooms Available",HttpStatus.OK);
		}
	}
	
	
	@PostMapping("/deleteProperty")
	public ResponseEntity<Object> deleteProperty(@RequestBody Property req) throws java.text.ParseException{
		System.out.println("Inside deleteProperty");
		
		int propertyId = req.getPropertyId();
		System.out.println("pid"+propertyId);
		String ownerEmail = req.getOwnerEmail();
		
		//ArrayList<BookingDetails> bookingDetails = bookingDao.findBookingDetailsByPropertyId(propertyId);
		ArrayList<BookingDetails> bookingDetails = bookingService.findBookingDetailsByPID(propertyId);
		
		System.out.println(bookingDetails.size());
		
		for(BookingDetails eachBooking:bookingDetails) {
			

			System.out.println("bookingDetails size"+bookingDetails.size()+"bookingID"+eachBooking.getBookingId());
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			String start_date = eachBooking.getBookingStartDate();
			String end_date = eachBooking.getBookingEndDate();
			int roomBookingId = eachBooking.getBookingId();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
			
			Date s_date = formatter.parse(start_date);
			//Date e_date = formatter.parse(end_date);
			
			Calendar start = Calendar.getInstance();
			//Calendar end = Calendar.getInstance();
			
			start.setTime(s_date);
			//end.setTime(e_date);
			
			
			///------------------------check for current date-------------------------------
			Date date = new Date();
			Calendar current = Calendar.getInstance();
			date = date.from(timeservice.getCurrentTime().toInstant());
			//number of days between current day and bookingStartDay--------------------------
			int days = (int) ((s_date.getTime()-date.getTime())/86400000);
			System.out.println("days"+days);
			
			if(days>7) {
				//bookingStartDate <7 from currentDate
				System.out.println(">>>>>>7");
				bookingDao.hostCancelBooking("cancel", 0, roomBookingId);
				System.out.println("remove check %%%%%%%%%%%%%%%%%%");
			}
			else {
				System.out.println("<<<<<<<< 7");
				//cancel booking with 15% penalty on total price
				int refund_amount = (int) (eachBooking.getTotalPrice()*(.15));
				bookingDao.hostCancelBooking("cancel", refund_amount, roomBookingId);
			}
		}
		
		propertyDao.deletePropertyByPropertyId(propertyId);
		emailVerification.deleteProperty(ownerEmail);
		return new ResponseEntity<>("Deleted!! Bookings related to this property will be cancelled",HttpStatus.OK);
		
	}
	
	@PostMapping("/searchProperty")
	public List<Property> searchProperty(@RequestBody NewSearch filter){
		System.out.println("Inside bookProperty!");
		System.out.println("filter"+filter);
		List<Property> properties = propertyDao.findAll(new Specification<Property>() {
			
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<Property> query = builder.createQuery(Property.class);
		    Root<Property> root = query.from(Property.class);

		    
			 @Override
			    public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				 List<Predicate> predicates = new ArrayList<>();
				 
				 if (!filter.getCity().equals("")) {
			            predicates.add(cb.like(root.get("city"), "%" + filter.getCity() + "%"));
			        }
				 
				 if (!filter.getZip().equals("")) {
			            predicates.add(cb.equal(root.get("zip"), filter.getZip()));
			        }
				 
				 if (!filter.getSharingType().equals("")) {
					 predicates.add(cb.equal(root.get("sharingType"), filter.getSharingType()));
			        }
				
				 if (!filter.getPropertyType().equals("")) {
					 predicates.add(cb.equal(root.get("propertyType"), filter.getPropertyType()));
			        }
				 
				 
				 if (!filter.getDescription().equals("")) {


			            String[] splited = filter.getDescription().split("\\s+");

			            for (int i = 0; i < splited.length; i++) {
			            	predicates.add(cb.like(root.get("description"), "%" + splited[i] + "%"));
			            }

			        }
				 if (!filter.getWifi().equals("")) {
					 predicates.add(cb.equal(root.get("wifi"), filter.getWifi()));
			        }
				 
//				 if (filter.getMinPrice()!=0) {
//					 predicates.add(cb.equal(root.get("minPrice"), filter.getMinPrice()));
//			        }
//				 
//				 if (filter.getMaxPrice()!=0) {
//					 predicates.add(cb.equal(root.get("maxPrice"), filter.getMaxPrice()));
//			        }

				 //predicates.add(cb.equal(root.get("status"), "Created"));
				 
			        return cb.and(predicates.toArray(new Predicate[0]));
				 
			 }
			
		});
		System.out.println("search results"+properties);
		//return new ResponseEntity<>("search results"+properties, HttpStatus.OK);
		if(properties.size() !=0) {
			return properties;
			
		}
		else {
		return null;
		}
		
	}

@PostMapping("/updateProperty")
public ResponseEntity<Object> updateCompleteProperty(@RequestBody Property req) throws java.text.ParseException{
	System.out.println("Inside updateCompleteProperty post");
	
	String propertyName = req.getPropertyName();
	String description = req.getDescription();
	String propertyPhone = req.getPropertyPhone();
	String propertyType = req.getPropertyType();
	String sharingType = req.getSharingType();
	String parkingAvailability = req.getParkingAvailability();
	String freeParking = req.getFreeParking();
	int parkingFee = req.getParkingFee();
	String wifi = req.getWifi();
	int propertyId = req.getPropertyId();
	
	String ownerEmail = req.getOwnerEmail();
	
	try {
		propertyDao.updateCompleteProperty(propertyName, description, propertyPhone, propertyType, sharingType, parkingAvailability, freeParking, parkingFee, wifi, propertyId);
		emailVerification.updateProperty(ownerEmail);
		System.out.println("Property Updated!!!!");
		return new ResponseEntity<>("Property Updated", HttpStatus.OK);
	}catch(ParseException e) {
		System.out.println(e);
		return new ResponseEntity<>("Unable to update Property", HttpStatus.BAD_REQUEST);
	}
}
	
}
