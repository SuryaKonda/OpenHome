package com.cmpe275.project.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.project.Entity.BookingDetails;
import com.cmpe275.project.Entity.Payments;
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
public class BookingController {

	@Autowired
	BookingDao bookingDao;
	
	@Autowired
	BookingService bookingService;
	
	@Autowired(required = true)
	RoomDao roomDao;
	
	@Autowired
	PropertyService propertyService;
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	TimeService timeservice;
	
	@Autowired
	EmailVerification emailVerification;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDao userDao;
	
	@Autowired(required = true)
	PropertyDao propertyDao;
	
	@PostMapping("/bookRoom")
	public ResponseEntity<Object> bookRoom(@RequestBody BookingDetails body) throws ParseException, java.text.ParseException{
		System.out.println("Inside bookProperty!");
		
		String startDate = body.getBookingStartDate();
		System.out.println("startDate"+startDate);
		String endDate = body.getBookingEndDate();
		System.out.println("endDate"+endDate);
		
		String s_test = startDate+" "+ "15:00:00";
		String e_test = endDate+" "+ "11:00:00";
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		
		Date final_date = formatter.parse(s_test);
		Date end_date = formatter.parse(e_test);
		
		//String userEmail = body.getOwnerEmail();
		int propertyId= body.getPropertyId();
		int roomId = body.getRoomId();
		int customerId = body.getCustomerId();
		int paymentId = body.getPaymentId();
		String customerName = body.getCustomerName();
		
		int refundAmount = 0;
		int penaltyAmount = 0;
		
		List <User> userDetails = userDao.findUserByCustomerId(customerId);
		String userEmail = userDetails.get(0).getUserEmail();
		System.out.println("userEmail"+userEmail);
		
		List<Property> propertyDetails = propertyService.getPropertyDetails(propertyId);
		
		String ownerEmail = propertyDetails.get(0).getOwnerEmail();
		String propertyName = propertyDetails.get(0).getPropertyName();
		String streetAddress = propertyDetails.get(0).getAddress();
		String city = propertyDetails.get(0).getCity();
		String state = propertyDetails.get(0).getState();
		String zip = propertyDetails.get(0).getZip();
		
		String append_address = streetAddress+","+city+","+state+","+zip;
		
		BookingDetails book = new BookingDetails();
				
		book.setBookingStartDate(s_test);
		book.setBookingEndDate(e_test);
		book.setCustomerId(customerId);
		book.setPropertyId(propertyId);
		book.setRoomId(roomId);
		book.setPaymentId(paymentId);
		book.setCustomerName(customerName);
		book.setBookingStatus("new");
		book.setRefundAmount(refundAmount);
		book.setPenaltyAmount(penaltyAmount);
		book.setOwnerEmail(ownerEmail);
		book.setPropertyName(propertyName);
		book.setPropertyAddress(append_address);
		
		System.out.println("customerId"+book.getCustomerId());
		
		Calendar start = Calendar.getInstance();
		start.setTime(final_date);
		
		Calendar end = Calendar.getInstance();
		end.setTime(end_date);
		
		int weekdays=0;
		int weekends=0;
		
		int sunday = Calendar.SUNDAY;
		int saturday = Calendar.SATURDAY;
		
		while(start.before(end)) {
			System.out.println(start.get(Calendar.DAY_OF_WEEK));
			if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday){
				weekdays=weekdays+1;
				start.add(Calendar.DATE, 1);
				
			}else {
				weekends=weekends+1;
				start.add(Calendar.DATE, 1);
			}
		}
		Rooms room = roomDao.findRoomsByRoomId(roomId);
		
		int weekday_price = room.getWeekdayPrice();
		int weekend_price = room.getWeekendPrice();
		
		int total_price = (int) ((weekday_price*weekdays) + (weekend_price*weekends));
		
		System.out.println("Weekdays"+weekdays);
		System.out.println("Weekends"+weekends);
		System.out.println("Total_Price"+total_price);
		
		book.setTotalPrice(total_price);
		
		try {
			bookingDao.save(book);
			emailVerification.userBookingConfirmation(userEmail);
			emailVerification.ownerBookingConfirmation(ownerEmail);
			System.out.println("Booking saved!!!!");
			return new ResponseEntity<>(total_price,HttpStatus.OK);
		}catch(Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("could not book a room!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/cancelBookingHost")
	public ResponseEntity<Object> cancelBookingHost(@RequestBody BookingDetails req) throws java.text.ParseException{
		System.out.println("Inside cancelBookingHost!!!!!");
		
		int bookingId = req.getBookingId();
		int roomId = req.getRoomId();
		
		int propertyId = req.getPropertyId();
		int refund_amount = 0;
		
		//String userEmail = req.getOwnerEmail();
		String ownerEmail = req.getOwnerEmail();
		
		
		String bookingStatus = "cancel";
		
		List <BookingDetails> customerID = bookingDao.findCustomerId(bookingId);
		int customerId = customerID.get(0).getCustomerId();
		
		List <User> userDetails = userDao.findUserByCustomerId(customerId);
		String userEmail = userDetails.get(0).getUserEmail();
		System.out.println("userEmail"+userEmail);
		
		
		ArrayList<BookingDetails> bookingDetails = bookingService.getBookingByBookingId(bookingId);
		
		Property property = propertyDao.findPropertyByPropertyId(propertyId);
		
		int parking_fee = property.getParkingFee();
		Rooms room = roomDao.findRoomsByRoomId(roomId);
//		System.out.println("roomdetails"+room.getSunday());
		
		String s_d = bookingDetails.get(0).getBookingStartDate();
		String e_d = bookingDetails.get(0).getBookingEndDate();
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		
		Date f_date = formatter.parse(s_d);
		Date e_date = formatter.parse(e_d);
		
		
		////$$$$$$$$$$$$$$$$$$$$$$$$$$$$ change here !!!!!
		Date date = new Date();
		
		Calendar current = Calendar.getInstance();
		date = date.from(timeservice.getCurrentTime().toInstant());
		
		current.setTime(date);
	
		Calendar start = Calendar.getInstance();
		Calendar new_start = Calendar.getInstance();
		start.setTime(f_date);
		new_start.setTime(f_date);
		Calendar end = Calendar.getInstance();
		end.setTime(e_date);
		
		int booking_days = (int) ((e_date.getTime()-f_date.getTime())/86400000);
		booking_days+=1;
		int days = (int) ((f_date.getTime()-date.getTime())/86400000);
		
		int days_left = booking_days-(Math.abs(days)+1);
		
		System.out.println("days"+days);
		System.out.println("booking_days"+booking_days);
		
		if(days>7) {
			 
			bookingDao.hostCancelBooking("cancel", 0, bookingId);
			emailVerification.hostBookingCancelGuest(userEmail);
			emailVerification.hostBookingCancelHost(ownerEmail);
			return new ResponseEntity<>("Booking > 7 days. No penalty will be paid by Host", HttpStatus.OK);
		}
		else {
			
			if(bookingDetails.get(0).getBookingStatus().equalsIgnoreCase("new")) {
				int perDayPrice=0;
				if(start.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY && start.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY) {
					//int perDayPrice = room.getWeekdayPrice()+parking_fee;
					perDayPrice = room.getWeekdayPrice();
					
				}else {
					
					perDayPrice = room.getWeekendPrice();
				}
					refund_amount = (int) (perDayPrice*0.15);
					
					System.out.println("refund_amount"+refund_amount);
					
					BookingDetails booking = new BookingDetails();
					
					bookingDao.hostCancelBooking("cancel", refund_amount, bookingId);
					emailVerification.hostBookingCancelGuest(userEmail);
					emailVerification.hostBookingCancelHost(ownerEmail);
					return new ResponseEntity<>("You have to pay a compensation of $"+refund_amount, HttpStatus.OK);
			}
			if(bookingDetails.get(0).getBookingStatus().equalsIgnoreCase("checkin")) {
				
				int weekdays=0;
				int weekends=0;
				
				System.out.println("inside checkin");
				
				
				
				start.add(Calendar.DATE, Math.abs(days)+1);
				new_start.add(Calendar.DATE, Math.abs(days)+1+6);
				while(start.before(end) && start.before(new_start)) {
					if(start.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY && start.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY) {
						weekdays=weekdays+1;
						start.add(Calendar.DATE, 1);
					}else {
						weekends=weekends+1;
						start.add(Calendar.DATE, 1);
					}	
				}
				int remaining_day_price = (weekdays*room.getWeekdayPrice()+weekends*room.getWeekendPrice());
				System.out.println("remaining_day_price"+remaining_day_price);
				
				refund_amount = (int)(remaining_day_price*0.15);
				
				bookingDao.hostCancelBooking("cancel", refund_amount, bookingId);
				emailVerification.hostBookingCancelGuest(userEmail);
				emailVerification.hostBookingCancelHost(ownerEmail);
				return new ResponseEntity<>("You have to pay a compensation of $"+refund_amount, HttpStatus.OK);
			}
			
		return new ResponseEntity<>("couldn't cancel host booking", HttpStatus.NOT_FOUND);
		}
	}
	
//	@PostMapping("/cancelBookingGuest")
//	public ResponseEntity<Object> cancelBooking(@RequestBody BookingDetails req) throws java.text.ParseException{
//		System.out.println("Inside cancelBookingGuest!!!!!");
//		int bookingId = req.getBookingId();
//		
//		List<BookingDetails> bookingDetails = bookingService.getBookingByBookingId(bookingId);
//		
//		int roomId = bookingDetails.get(0).getRoomId();
//		int total_price = bookingDetails.get(0).getTotalPrice();
//		int final_total_price = 0;
//		int refund_amount = 0;
//		int customerId = req.getCustomerId();
//		//String ownerEmail = req.getOwnerEmail();
//		
//		List <BookingDetails> OwnerEmail = bookingDao.findCustomerId(bookingId);
//		String ownerEmail = OwnerEmail.get(0).getOwnerEmail();
//		
//		String s_d = bookingDetails.get(0).getBookingStartDate();
//		String e_d = bookingDetails.get(0).getBookingEndDate();
//		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
//		
//		Date f_date = formatter.parse(s_d);
//		Date e_date = formatter.parse(e_d);
//		
//		
//		//////date change**************
//		Date date = new Date();
//		
//		Calendar current = Calendar.getInstance();
//		date = date.from(timeservice.getCurrentTime().toInstant());
//		System.out.println("date-----"+date);
//		
//		current.setTime(date);
//		System.out.println("current"+current);
//	
//		int booking_days = (int) ((e_date.getTime()-f_date.getTime())/86400000);
//		booking_days+=1;
//		int days = (int) ((f_date.getTime()-date.getTime())/86400000);
//		int temp_days = (int)((f_date.getTime() - date.getTime())/86400000);
//		System.out.println("==============="+days);
//		System.out.println("%%%%%%%%%%%%%%%%%"+temp_days);
//
//		Calendar start = Calendar.getInstance();
//		start.setTime(f_date);
//		Calendar end = Calendar.getInstance();
//		end.setTime(e_date);
//		
//		int sunday = Calendar.SUNDAY;
//		int saturday = Calendar.SATURDAY;
//		
//		int weekdays =0;
//		int weekends=0;
//		
//		int penalty =0;
//		float to_pay=0;
//		
//		String bookingStatus = req.getBookingStatus();
//		
//		Rooms room = roomDao.findRoomsByRoomId(roomId);
//		
//		List <User> userDetails = userDao.findUserByCustomerId(customerId);
//		String userEmail = userDetails.get(0).getUserEmail();
//		System.out.println("userEmail"+userEmail);
//		
//		String current_status = bookingDetails.get(0).getBookingStatus();
//		int weekend_price =room.getWeekendPrice();
//		int weekday_price = room.getWeekdayPrice();
//		
//		System.out.println("WD"+weekday_price);
//		System.out.println("WE"+weekend_price);
//		
//		int i=0;
//		int temp_weekend=0;
//		int temp_weekday=0;
//		if(days<0) {
//			long add_days=Math.abs(days)+1;
//			System.out.println("Abs_Days:"+add_days);
//			while(i<add_days) {
//				if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday) {
//					temp_weekday=temp_weekday+1;
//					start.add(Calendar.DATE, 1);
//					i++;
//				}else {
//					temp_weekend=temp_weekend+1;
//					start.add(Calendar.DATE, 1);
//					i++;
//				}	
//			}
//			System.out.println("temp_weekday"+temp_weekday);
//			System.out.println("temp_weekend"+temp_weekend);
//			
//			
//			System.out.println("Updated Date"+start.get(Calendar.DATE));
//		}
//		
//		
//		//Calendar start_sec = Calendar.getInstance();
//		//start_sec.setTime(start.add(Calendar.Date, amount););
//		
//		Calendar sec = Calendar.getInstance();
//		sec.setTime(date);
//		
//		sec.add(Calendar.DATE,1);
//		
//		System.out.println("Second Date"+sec.get(Calendar.DATE));
//		//sec.setTime(
//		
//		while(start.before(sec)) {
//			if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday) {
//				weekdays=weekdays+1;
//				start.add(Calendar.DATE, 1);
//			}else {
//				weekends=weekends+1;
//				start.add(Calendar.DATE, 1);
//			}	
//		}
//		
//		System.out.println("Weekdays"+weekdays);
//		System.out.println("Weekends"+weekends);
//		int perDayPrice = (int) (total_price/booking_days);
//		
//		if(days>=2){
//			bookingDao.guestCancelBooking("cancel",0, bookingId);
//			System.out.println("Booking cancelled before two days. No cancellation fee");
//			emailVerification.userBookingCancelGuest(userEmail);
//			emailVerification.userBookingCancelHost(ownerEmail);
//			return new ResponseEntity<>("Booking cancelled before two days. No cancellation fee",HttpStatus.OK);	
//		}
//
//		if(days<=0 && !(current_status.equalsIgnoreCase("cancel") )) {
//			//total_price = (float) (total_price - (perDayPrice * 0.3)*(Math.abs(days)+1));
//			
//			to_pay = (weekday_price*temp_weekday)+(weekend_price*temp_weekend);
//			
//			int new_penalty = (int) ((weekday_price*0.3*weekdays)+(weekend_price*0.3*weekends));
//			
//			//penalty = (int) ((weekday_price*0.3*temp_weekday)+(weekend_price*0.3*temp_weekday));
//			
//			final_total_price = (int)(to_pay+new_penalty);
//			refund_amount = total_price - final_total_price;
//			
//			System.out.println("totalPriceRefundToCustomer"+total_price);
//			System.out.println("PenaltyToBePaid"+penalty);
//			System.out.println("NewPenaltyToBePaid"+new_penalty);
//			System.out.println("ToPay"+to_pay);
//			
//			bookingDao.guestCancelBooking("cancel",penalty, bookingId);
//			System.out.println("30% perDay booking price is penalty");
//			emailVerification.userBookingCancelGuest(userEmail);
//			emailVerification.userBookingCancelHost(ownerEmail);
//			
//			return new ResponseEntity<>("You have been charged $"+final_total_price+", you will get a refund of $"+refund_amount,HttpStatus.CREATED);	
//		}
//		
//		if(days<-1) {
//			
//			System.out.println("couldn't cancel booking details");
//			return new ResponseEntity<>("couldn't cancel booking details", HttpStatus.NOT_FOUND);
//		}
//		else {
//			System.out.println("couldn't cancel booking details");
//			return new ResponseEntity<>("couldn't cancel booking details", HttpStatus.NOT_FOUND);
//			}	
//	}
	
	@PostMapping("/cancelBookingGuest")
	public ResponseEntity<Object> cancelBooking(@RequestBody BookingDetails req) throws java.text.ParseException{
		System.out.println("Inside cancelBookingGuest!!!!!");
		int bookingId = req.getBookingId();
		
		List<BookingDetails> bookingDetails = bookingService.getBookingByBookingId(bookingId);
		
		int roomId = bookingDetails.get(0).getRoomId();
		int total_price = bookingDetails.get(0).getTotalPrice();
		int final_total_price = 0;
		int refund_amount = 0;
		//int customerId = req.getCustomerId();
		
		//start date and end date into calendar
		String s_d = bookingDetails.get(0).getBookingStartDate();
		String e_d = bookingDetails.get(0).getBookingEndDate();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		Date f_date = formatter.parse(s_d);
		Date e_date = formatter.parse(e_d);
		
		
		//////date change******
		Date date = new Date();
		date = date.from(timeservice.getCurrentTime().toInstant());
		System.out.println("date-----"+date);
		
		List <BookingDetails> OwnerEmail = bookingDao.findCustomerId(bookingId);
		String ownerEmail = OwnerEmail.get(0).getOwnerEmail();
		int customerId = OwnerEmail.get(0).getCustomerId();
		List <User> userDetails = userDao.findUserByCustomerId(customerId);
		String userEmail = userDetails.get(0).getUserEmail();
		System.out.println("userEmail"+userEmail);
		
		//number of bookingdays and days from start date
		int booking_days = (int) ((e_date.getTime()-f_date.getTime())/86400000);
		booking_days+=1;
		int days = (int) ((f_date.getTime()-date.getTime())/86400000);
		int temp_days = (int)((f_date.getTime() - date.getTime())/86400000);
		System.out.println("==============="+days);
		System.out.println("%%%%%%%%%%%%%%%%%"+temp_days);
		System.out.println("******"+booking_days);

		Calendar start = Calendar.getInstance();
		start.setTime(f_date);
		Calendar end = Calendar.getInstance();
		end.setTime(e_date);
		
		int sunday = Calendar.SUNDAY;
		int saturday = Calendar.SATURDAY;
		int weekdays =0;
		int weekends=0;
		int penalty =0;
		float to_pay=0;
		
		String bookingStatus = req.getBookingStatus();
		
		//room details
		Rooms room = roomDao.findRoomsByRoomId(roomId);
		String current_status = bookingDetails.get(0).getBookingStatus();
		int weekend_price =room.getWeekendPrice();
		int weekday_price = room.getWeekdayPrice();
		System.out.println("WD"+weekday_price);
		System.out.println("WE"+weekend_price);
		
		int i=0;
		int temp_weekend=0;
		int temp_weekday=0;
		if(days<0) {
			long add_days=Math.abs(days)+1;
			System.out.println("Abs_Days:"+add_days);
			while(i<add_days) {
				if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday) {
					temp_weekday=temp_weekday+1;
					start.add(Calendar.DATE, 1);
					i++;
				}else {
					temp_weekend=temp_weekend+1;
					start.add(Calendar.DATE, 1);
					i++;
				}	
			}
			System.out.println("temp_weekday"+temp_weekday);
			System.out.println("temp_weekend"+temp_weekend);
			System.out.println("Updated Date"+start.get(Calendar.DATE));
		}
		
		
		if(days<=0) {
		Calendar sec = Calendar.getInstance();
		sec.setTime(date);
		sec.add(Calendar.DATE,1);
		System.out.println("Second Date"+sec.get(Calendar.DATE));
	
		
		while(start.before(sec)) {
			if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday) {
				weekdays=weekdays+1;
				start.add(Calendar.DATE, 1);
			}else {
				weekends=weekends+1;
				start.add(Calendar.DATE, 1);
			}	
		}
		}else{
			if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday) {
				weekdays=weekdays+1;
			}else {
				weekends=weekends+1;		
		}
		}
		
		System.out.println("Weekdays"+weekdays);
		System.out.println("Weekends"+weekends);
		
		if(days>=2){
			bookingDao.guestCancelBooking("cancel",0, bookingId);
			emailVerification.userBookingCancelGuest(userEmail);
			emailVerification.userBookingCancelHost(ownerEmail);
			System.out.println("Booking cancelled before two days. No cancellation fee");
			return new ResponseEntity<>("Booking cancelled before two days. No cancellation fee",HttpStatus.OK);	
		}

		if(days<=1 && !(current_status.equalsIgnoreCase("cancel") )) {
			
			
			to_pay = (weekday_price*temp_weekday)+(weekend_price*temp_weekend);
			int new_penalty = (int) ((weekday_price*0.3*weekdays)+(weekend_price*0.3*weekends));
			final_total_price = (int)(to_pay+new_penalty);
			refund_amount = total_price - final_total_price;
			
			System.out.println("totalPriceOftheBooking"+total_price);
			System.out.println("PenaltyToBePaid"+new_penalty);
			System.out.println("ToPayforStaying"+to_pay);
			bookingDao.guestCancelBooking("cancel",penalty, bookingId);
			emailVerification.userBookingCancelGuest(userEmail);
			emailVerification.userBookingCancelHost(ownerEmail);
			System.out.println("30% perDay booking price is penalty");
			
			return new ResponseEntity<>("You have been charged $"+final_total_price+", you will get a refund of $"+refund_amount,HttpStatus.CREATED);	
		}
		
//		if(days<-1) {
//			
//			System.out.println("couldn't cancel booking details");
//			return new ResponseEntity<>("couldn't cancel booking details", HttpStatus.NOT_FOUND);
//		}
		else {
			System.out.println("couldn't cancel booking details");
			return new ResponseEntity<>("couldn't cancel booking details", HttpStatus.NOT_FOUND);
			}	
	}
	
	@PostMapping("/guestCheckIn")
	public ResponseEntity<Object> guestCheckIn(@RequestBody Map<String, String> body) throws ParseException, java.text.ParseException{
		System.out.println("In guestCheckIn post");
		
		int bookingId = Integer.parseInt(body.get("bookingId"));
		int roomId = Integer.parseInt(body.get("roomId"));
		//int customerId = Integer.parseInt(body.get("userId"));
		
		List <Rooms> room = roomService.findRoomsByRoomId(roomId);
		
		System.out.println(room);
		
		int weekendPrice = room.get(0).getWeekendPrice();
		int weekdayPrice = room.get(0).getWeekdayPrice();
		
		List <BookingDetails> bookings = bookingService.findBookingsById(bookingId);
		System.out.println(bookings);
		
		List <BookingDetails> OwnerEmail = bookingDao.findCustomerId(bookingId);
		String ownerEmail = OwnerEmail.get(0).getOwnerEmail();
		int customerId = OwnerEmail.get(0).getCustomerId();
		
		List <User> userDetails = userDao.findUserByCustomerId(customerId);
		String userEmail = userDetails.get(0).getUserEmail();
		System.out.println("userEmail"+userEmail);
		
		String s_d = bookings.get(0).getBookingStartDate();
		String e_d = bookings.get(0).getBookingEndDate();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date f_date = formatter.parse(s_d);
		System.out.println("-----------------"+f_date);
		Date e_date = formatter.parse(e_d);
		Date date = new Date();
		
		int booking_days = (int) ((e_date.getTime()-f_date.getTime())/86400000);
		booking_days+=1;
		
		Calendar current = Calendar.getInstance();
		
		date = date.from(timeservice.getCurrentTime().toInstant());
		current.setTime(date);
		//current.setTime(timeservice.getCurrentTime().toInstant());

		/*Calendar currentOne = Calendar.getInstance();
		currentOne.setTime(date);
		currentOne.add(currentOne.DATE, 1);*/
		
		Calendar start = Calendar.getInstance();
		start.setTime(f_date);
		System.out.println(start.getTime());
		
		Calendar second = Calendar.getInstance();
		second.setTime(f_date);
		second.add(second.DATE, 1);
		System.out.println(second.getTime());
		
		Calendar end = Calendar.getInstance();
		end.setTime(e_date);
		
		System.out.println("************"+current.getTime());
		System.out.println("************"+start.getTime());
		
		if(current.before(start)) {
			System.out.println("---------------------1");
			return new ResponseEntity<>(" Cannot checkin before 3 PM of Booking start date ",HttpStatus.BAD_REQUEST);
		}else if(current.after(end)){
			System.out.println("---------------------after");
			return new ResponseEntity<>(" Cannot checkin after 11 AM of Booking end date ",HttpStatus.UNAUTHORIZED);
		}else {
			System.out.println("current.DATE--------------"+current.DATE);
			System.out.println("current___date********"+(current.getTime().getDate()));
			System.out.println("start.DATE--------------"+start.DATE);
			System.out.println("start_date*************"+start.getTime().getDate());
			System.out.println("current.HOUR_OF_DAY----------"+current.HOUR_OF_DAY);
			System.out.println("current_hour****************"+current.getTime().getHours());
			System.out.println("second.DATE-----------"+second.DATE);
			System.out.println("second_date***************"+second.getTime().getDate());
			System.out.println("(start.DAY_OF_WEEK----------------"+start.DAY_OF_WEEK);
			System.out.println("start_day_of_week**************"+start.getTime().getDay());
			System.out.println("second_day_of_week%%%%%%%%%%%%%%"+second.getTime().getDay());
			
			int current_date = current.getTime().getDate();
			
			if((current.getTime().getDate() == start.getTime().getDate() && current.getTime().getHours() > 15) || (current.getTime().getDate() == second.getTime().getDate() && current.getTime().getHours() < 3)) {
				//update status to checking0
				bookingService.updateBookingStatusByBookingId("checkin",bookingId);
				System.out.println("-----------------2");
				return new ResponseEntity<>("checked in",HttpStatus.OK);
			}else {
				if(booking_days == 1) {
					if(start.getTime().getDay() == 6 || start.getTime().getDay() == 7) {
						bookingDao.guestCancelBooking("cancel",(int)(0.3*weekendPrice),bookingId);
						System.out.println("-----------------3");
						return new ResponseEntity<>("room cancelled with guest penality of "+(0.3* weekendPrice),HttpStatus.PAYMENT_REQUIRED);
					}else {
						bookingDao.guestCancelBooking("cancel",(int)(0.3*weekdayPrice),bookingId);
						System.out.println("-----------------4");
						return new ResponseEntity<>("room cancelled with guest penality of "+(0.3* weekdayPrice),HttpStatus.FORBIDDEN);
					}
				}else {
					//booking_days>1
					if(start.getTime().getDay() == 6 && second.getTime().getDay() == 7) {
						bookingDao.guestCancelBooking("cancel",(int)(0.3*2*weekendPrice),bookingId);
						System.out.println("-----------------5");
						return new ResponseEntity<>("room cancelled with guest penality of "+(0.3*2*weekendPrice),HttpStatus.NOT_FOUND);
					}
					else if((start.getTime().getDay() == 6 && second.getTime().getDay() != 7) || (start.getTime().getDay() !=6 && second.getTime().getDay() == 7)) {
						bookingDao.guestCancelBooking("cancel", (int)(0.3*(weekendPrice+weekdayPrice)), bookingId);
						System.out.println("-----------------6");
						return new ResponseEntity<>("room cancelled with guest penality of "+(0.3*(weekendPrice+weekdayPrice)),HttpStatus.METHOD_NOT_ALLOWED);
					}else {
						bookingDao.guestCancelBooking("cancel",(int)(0.3*2*weekdayPrice),bookingId);
						System.out.println("-----------------7");
						return new ResponseEntity<>("room cancelled with guest penality of "+(0.3*2*weekdayPrice),HttpStatus.NOT_ACCEPTABLE);
					}
				}
			}
		}
	}
	
//	@PostMapping("/guestCheckout")
//	public ResponseEntity<Object> guestCheckout(@RequestBody BookingDetails req) throws java.text.ParseException{
//		
//		System.out.println("Inside cancelBookingGuest!!!!!");
//		int bookingId = req.getBookingId();
//		List<BookingDetails> bookingDetails = bookingService.getBookingByBookingId(bookingId);
//		int roomId = bookingDetails.get(0).getRoomId();
//		int total_price = bookingDetails.get(0).getTotalPrice();
//		int final_total_price = 0;
//		int refund_amount = 0;
//		
//		Rooms room = roomDao.findRoomsByRoomId(roomId);
//		
//		
//		String s_d = bookingDetails.get(0).getBookingStartDate();
//		String e_d = bookingDetails.get(0).getBookingEndDate();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
//		Date f_date = formatter.parse(s_d);
//		Date e_date = formatter.parse(e_d);
//		
//		
//		//////date change**************
//		Date date = new Date();
//		
//		Calendar current = Calendar.getInstance();
//		Calendar end = Calendar.getInstance();
//		Calendar start = Calendar.getInstance();
//		
//		start.setTime(f_date);
//		end.setTime(e_date);
//		date = date.from(timeservice.getCurrentTime().toInstant());
//		current.setTime(date);
//		
//		
//		int booking_days = (int) ((e_date.getTime()-f_date.getTime())/86400000);
//		booking_days+=1;
//		int days = (int) ((f_date.getTime()-date.getTime())/86400000);
//		int temp_days = (int)((f_date.getTime() - date.getTime())/86400000);
//		System.out.println("==============="+days);
//		System.out.println("%%%%%%%%%%%%%%%%%"+temp_days);
//		
//		if(current.after(end)) {
//			bookingDao.guestCancelBooking("checkout",0, bookingId);
//			
//			return new ResponseEntity<>("Checkout Successful",HttpStatus.OK);
//			
//		}
//		
//		int i=0;
//		int temp_weekend=0;
//		int temp_weekday=0;
//		int sunday = Calendar.SUNDAY;
//		int saturday = Calendar.SATURDAY;
//		int weekdays =0;
//		int weekends=0;
//		String current_status = bookingDetails.get(0).getBookingStatus();
//		int penalty =0;
//		float to_pay=0;
//		
//		int weekend_price =room.getWeekendPrice();
//		int weekday_price = room.getWeekdayPrice();
//		
//		
//		if(current.before(end)) {
//			
//			if(days<=0) {
//				long add_days=Math.abs(days)+1;
//				System.out.println("Abs_Days:"+add_days);
//				while(i<add_days) {
//					if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday) {
//						temp_weekday=temp_weekday+1;
//						start.add(Calendar.DATE, 1);
//						i++;
//					}else {
//						temp_weekend=temp_weekend+1;
//						start.add(Calendar.DATE, 1);
//						i++;
//					}	
//				}
//				System.out.println("temp_weekday"+temp_weekday);
//				System.out.println("temp_weekend"+temp_weekend);
//				
//				
//				System.out.println("Updated Date"+start.get(Calendar.DATE));
//			}
//			
//			
//			//Calendar start_sec = Calendar.getInstance();
//			//start_sec.setTime(start.add(Calendar.Date, amount););
//			
//			Calendar sec = Calendar.getInstance();
//			sec.setTime(date);
//			
//			sec.add(Calendar.DATE,1);
//			
//			System.out.println("Second Date"+sec.get(Calendar.DATE));
//			//sec.setTime(
//			
//			while(start.before(sec)) {
//				if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday) {
//					weekdays=weekdays+1;
//					start.add(Calendar.DATE, 1);
//				}else {
//					weekends=weekends+1;
//					start.add(Calendar.DATE, 1);
//				}	
//			}
//			
//			System.out.println("Weekdays"+weekdays);
//			System.out.println("Weekends"+weekends);
//			int perDayPrice = (int) (total_price/booking_days);
//			
//			if(days>=2){
//				bookingDao.guestCancelBooking("cancel",0, bookingId);
//				System.out.println("Booking cancelled before two days. No cancellation fee");
//				return new ResponseEntity<>("Booking cancelled before two days. No cancellation fee",HttpStatus.OK);	
//			}
//
//			if(days<=0 && !(current_status.equalsIgnoreCase("cancel") )) {
//				//total_price = (float) (total_price - (perDayPrice * 0.3)*(Math.abs(days)+1));
//				
//				to_pay = (weekday_price*temp_weekday)+(weekend_price*temp_weekend);
//				
//				int new_penalty = (int) ((weekday_price*0.3*weekdays)+(weekend_price*0.3*weekends));
//				//penalty = (int) ((weekday_price*0.3*temp_weekday)+(weekend_price*0.3*temp_weekday));
//				
//				
//				
//				final_total_price = (int)(to_pay+new_penalty);
//				refund_amount = total_price - final_total_price;
//				
//				System.out.println("totalPriceRefundToCustomer"+total_price);
//				//System.out.println("PenaltyToBePaid"+penalty);
//				System.out.println("NewPenaltyToBePaid"+new_penalty);
//				System.out.println("ToPay"+to_pay);
//				
//				bookingDao.guestCancelBooking("cancel",penalty, bookingId);
//				System.out.println("30% perDay booking price is penalty");
//				
//				return new ResponseEntity<>("You have been charged $"+final_total_price+", you will get a refund of $"+refund_amount,HttpStatus.PAYMENT_REQUIRED);	
//			}
//			
//			if(days<-1) {
//				
//				System.out.println("couldn't cancel booking details");
//				return new ResponseEntity<>("couldn't cancel booking details", HttpStatus.NOT_FOUND);
//			}
//			else {
//				System.out.println("couldn't cancel booking details");
//				return new ResponseEntity<>("couldn't cancel booking details", HttpStatus.NOT_FOUND);
//				}		
//		}
//		return new ResponseEntity<>("Checkout",HttpStatus.OK);
//	}
	
	@PostMapping("/guestCheckout")
	public ResponseEntity<Object> guestCheckout(@RequestBody BookingDetails req) throws java.text.ParseException{
		
		System.out.println("Inside cancelBookingGuest!!!!!");
		int bookingId = req.getBookingId();
		List<BookingDetails> bookingDetails = bookingService.getBookingByBookingId(bookingId);
		int roomId = bookingDetails.get(0).getRoomId();
		int total_price = bookingDetails.get(0).getTotalPrice();
		int final_total_price = 0;
		int refund_amount = 0;
		int customerId = req.getCustomerId();
		
		Rooms room = roomDao.findRoomsByRoomId(roomId);
		
		
		String s_d = bookingDetails.get(0).getBookingStartDate();
		String e_d = bookingDetails.get(0).getBookingEndDate();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		Date f_date = formatter.parse(s_d);
		Date e_date = formatter.parse(e_d);
		
		
		//////date change******
		Date date = new Date();
		
		Calendar current = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		
		start.setTime(f_date);
		end.setTime(e_date);
		date = date.from(timeservice.getCurrentTime().toInstant());
		current.setTime(date);
		
		List <BookingDetails> OwnerEmail = bookingDao.findCustomerId(bookingId);
		String ownerEmail = OwnerEmail.get(0).getOwnerEmail();
		
		List <User> userDetails = userDao.findUserByCustomerId(customerId);
		String userEmail = userDetails.get(0).getUserEmail();
		System.out.println("userEmail"+userEmail);
		
		int booking_days = (int) ((e_date.getTime()-f_date.getTime())/86400000);
		booking_days+=1;
		int days = (int) ((f_date.getTime()-date.getTime())/86400000);
		int temp_days = (int)((f_date.getTime() - date.getTime())/86400000);
		System.out.println("==============="+days);
		System.out.println("%%%%%%%%%%%%%%%%%"+temp_days);
		
		if(current.after(end)) {
			bookingDao.guestCancelBooking("checkout",0, bookingId);
			emailVerification.checkoutGuest(userEmail);
			emailVerification.checkoutHost(ownerEmail);
			return new ResponseEntity<>("Checkout Successful",HttpStatus.OK);
			
		}
		
		int i=0;
		int temp_weekend=0;
		int temp_weekday=0;
		int sunday = Calendar.SUNDAY;
		int saturday = Calendar.SATURDAY;
		int weekdays =0;
		int weekends=0;
		String current_status = bookingDetails.get(0).getBookingStatus();
		int penalty =0;
		float to_pay=0;
		
		int weekend_price =room.getWeekendPrice();
		int weekday_price = room.getWeekdayPrice();
		
		
		if(current.before(end)) {
			
			if(days<0) {
				long add_days=Math.abs(days)+1;
				System.out.println("Abs_Days:"+add_days);
				while(i<add_days) {
					if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday) {
						temp_weekday=temp_weekday+1;
						start.add(Calendar.DATE, 1);
						i++;
					}else {
						temp_weekend=temp_weekend+1;
						start.add(Calendar.DATE, 1);
						i++;
					}	
				}
				System.out.println("temp_weekday"+temp_weekday);
				System.out.println("temp_weekend"+temp_weekend);
				System.out.println("Updated Date"+start.get(Calendar.DATE));
			}
			
			if(days<=0) {
				Calendar sec = Calendar.getInstance();
				sec.setTime(date);
				sec.add(Calendar.DATE,1);
				System.out.println("Second Date"+sec.get(Calendar.DATE));
			
			
			while(start.before(sec)) {
				if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday) {
					weekdays=weekdays+1;
					start.add(Calendar.DATE, 1);
				}else {
					weekends=weekends+1;
					start.add(Calendar.DATE, 1);
				}	
			}
			}else { 
				if(start.get(Calendar.DAY_OF_WEEK)!=saturday && start.get(Calendar.DAY_OF_WEEK)!=sunday) {
					weekdays=weekdays+1;
				}else {
					weekends=weekends+1; 
			}
			}
			
			System.out.println("Weekdays"+weekdays);
			System.out.println("Weekends"+weekends);
			
			
		/*	if(days>=2){
				bookingDao.guestCancelBooking("cancel",0, bookingId);
				System.out.println("Booking cancelled before two days. No cancellation fee");
				return new ResponseEntity<>("Booking cancelled before two days. No cancellation fee",HttpStatus.OK);	
			}*/

			if(days<=1 && !(current_status.equalsIgnoreCase("cancel") )) {
				
				
				to_pay = (weekday_price*temp_weekday)+(weekend_price*temp_weekend);
				int new_penalty = (int) ((weekday_price*0.3*weekdays)+(weekend_price*0.3*weekends));
				final_total_price = (int)(to_pay+new_penalty);
				refund_amount = total_price - final_total_price;
				
				System.out.println("totalPriceRefundToCustomer"+total_price);
				System.out.println("PenaltyToBePaid"+new_penalty);
				System.out.println("ToPayForStaying"+to_pay);
				
				bookingDao.guestCancelBooking("cancel",penalty, bookingId);
				emailVerification.checkoutGuest(userEmail);
				emailVerification.checkoutHost(ownerEmail);
				System.out.println("30% perDay booking price is penalty");
				
				return new ResponseEntity<>("You have been charged $"+final_total_price+", you will get a refund of $"+refund_amount,HttpStatus.PAYMENT_REQUIRED);	
			}
			
//			if(days<-1) {
//				
//				System.out.println("couldn't cancel booking details");
//				return new ResponseEntity<>("couldn't cancel booking details", HttpStatus.NOT_FOUND);
//			}
			else {
				System.out.println("couldn't checkout");
				return new ResponseEntity<>("couldn't checkout", HttpStatus.NOT_FOUND);
				}		
		}
		return new ResponseEntity<>("Checkout",HttpStatus.OK);
	}
	
	@PostMapping("/getPastOwnerBookingDetails")
	public ResponseEntity<List<BookingDetails>> PastOwnerBookingDetails(@RequestBody BookingDetails body) throws ParseException, java.text.ParseException{
		System.out.println("Inside PastOwnerBookingDetails");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		Date currentDate = new Date();	
		currentDate = currentDate.from(timeservice.getCurrentTime().toInstant());
		System.out.println("current Date is "+currentDate);
		int propertyId = body.getPropertyId();
		
		
		List<BookingDetails> bookingDetails = bookingDao.getOwnerBookingDetails(propertyId);
		List<BookingDetails> pastBookings = new ArrayList<>();
		
		if(bookingDetails.size()!=0) {
		for(int i=0;i<bookingDetails.size();i++){
			BookingDetails newBD = bookingDetails.get(i);
			String startDate = bookingDetails.get(i).getBookingStartDate();
			String endDate = bookingDetails.get(i).getBookingEndDate();
			
			Date start=formatter.parse(startDate);
			Date end=formatter.parse(endDate);
			System.out.println("fewfes"+bookingDetails.get(i).getBookingStatus());
			if((start.before(currentDate) && end.before(currentDate)) || bookingDetails.get(i).getBookingStatus().equalsIgnoreCase("cancel")){
				
				pastBookings.add(newBD);
				
			}
			
		}
		
		if(StringUtils.isEmpty(pastBookings)) {
			return new ResponseEntity<List<BookingDetails>> (HttpStatus.NOT_FOUND);
		}
		
		//return new ResponseEntity<>("Past Bookings are:"+pastBookings, HttpStatus.OK);
		return new ResponseEntity<List<BookingDetails>> (pastBookings,HttpStatus.OK);
	}
		else {
			return new ResponseEntity<List<BookingDetails>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping("/getCurrentOwnerBookingDetails")
	public ResponseEntity<List<BookingDetails>> CurrentOwnerBookingDetails(@RequestBody BookingDetails body) throws ParseException, java.text.ParseException{
		System.out.println("Inside getCurrentOwnerBookingDetails");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		Date currentDate = new Date();
		currentDate = currentDate.from(timeservice.getCurrentTime().toInstant());
		System.out.println("current Date is "+currentDate);
		int propertyId = body.getPropertyId();
		
		
		List<BookingDetails> bookingDetails = bookingDao.getOwnerBookingDetails(propertyId);
		List<BookingDetails> currentBookings = new ArrayList<>();
		
		if(bookingDetails.size()!=0) {
		for(int i=0;i<bookingDetails.size();i++){
			BookingDetails newBD = bookingDetails.get(i);
			String startDate = bookingDetails.get(i).getBookingStartDate();
			String endDate = bookingDetails.get(i).getBookingEndDate();
			
			Date start=formatter.parse(startDate);
			Date end=formatter.parse(endDate);
		
			if((start.before(currentDate) && end.after(currentDate)) && bookingDetails.get(i).getBookingStatus().equalsIgnoreCase("checkin")){
				
				currentBookings.add(newBD);
				
			}
			
		}
		
		if(StringUtils.isEmpty(currentBookings)) {
			return new ResponseEntity<List<BookingDetails>> (HttpStatus.NOT_FOUND);
		}
		
		//return new ResponseEntity<>("Past Bookings are:"+pastBookings, HttpStatus.OK);
		return new ResponseEntity<List<BookingDetails>> (currentBookings,HttpStatus.OK);
	}
		else {
			return new ResponseEntity<List<BookingDetails>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/getFutureOwnerBookingDetails")
	public ResponseEntity<List<BookingDetails>> FutureOwnerBookingDetails(@RequestBody BookingDetails body) throws ParseException, java.text.ParseException{
		System.out.println("Inside FutureOwnerBookingDetails");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		Date currentDate = new Date();
		currentDate = currentDate.from(timeservice.getCurrentTime().toInstant());
		System.out.println("current Date is "+currentDate);
		int propertyId = body.getPropertyId();
		
		
		List<BookingDetails> bookingDetails = bookingDao.getOwnerBookingDetails(propertyId);
		List<BookingDetails> futureBookings = new ArrayList<>();
		
		if(bookingDetails.size()!=0) {
		for(int i=0;i<bookingDetails.size();i++){
			BookingDetails newBD = bookingDetails.get(i);
			String startDate = bookingDetails.get(i).getBookingStartDate();
			String endDate = bookingDetails.get(i).getBookingEndDate();
			
			Date start=formatter.parse(startDate);
			Date end=formatter.parse(endDate);
		
			if(end.after(currentDate) && bookingDetails.get(i).getBookingStatus().equalsIgnoreCase("new")){
				
				futureBookings.add(newBD);
				
			}
			
		}
		
		if(StringUtils.isEmpty(futureBookings)) {
			return new ResponseEntity<List<BookingDetails>> (HttpStatus.NOT_FOUND);
		}
		
		//return new ResponseEntity<>("Past Bookings are:"+pastBookings, HttpStatus.OK);
		return new ResponseEntity<List<BookingDetails>> (futureBookings,HttpStatus.OK);
	}
		else {
			return new ResponseEntity<List<BookingDetails>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/getPastUserBookingDetails")
	public ResponseEntity<List<BookingDetails>> PastUserBookingDetails(@RequestBody BookingDetails body) throws ParseException, java.text.ParseException{
		System.out.println("Inside getPastUserBookingDetails");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		Date currentDate = new Date();	
		currentDate = currentDate.from(timeservice.getCurrentTime().toInstant());
		System.out.println("current Date is "+currentDate);
		int customerId = body.getCustomerId();
		
		
		List<BookingDetails> bookingDetails = bookingDao.getUserBookingDetails(customerId);
		List<BookingDetails> pastBookings = new ArrayList<BookingDetails>();
		
		if(bookingDetails.size()!=0) {
		for(int i=0;i<bookingDetails.size();i++){
			BookingDetails newBD = bookingDetails.get(i);
			String startDate = bookingDetails.get(i).getBookingStartDate();
			String endDate = bookingDetails.get(i).getBookingEndDate();
			
			Date start=formatter.parse(startDate);
			Date end=formatter.parse(endDate);
		
			if((start.before(currentDate) && end.before(currentDate)) || bookingDetails.get(i).getBookingStatus().equalsIgnoreCase("cancel")){
				
				pastBookings.add(newBD);
				
			}
			
		}
		if(StringUtils.isEmpty(pastBookings)) {
			return new ResponseEntity<List<BookingDetails>> (HttpStatus.NOT_FOUND);
		}
		
		//return new ResponseEntity<>("Past Bookings are:"+pastBookings, HttpStatus.OK);
		return new ResponseEntity<List<BookingDetails>> (pastBookings,HttpStatus.OK);
	}
		else {
			return new ResponseEntity<List<BookingDetails>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/getCurrentUserBookingDetails")
	public ResponseEntity<List<BookingDetails>> CurrentUserBookingDetails(@RequestBody BookingDetails body) throws ParseException, java.text.ParseException{
		System.out.println("Inside getCurrentUserBookingDetails");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		Date currentDate = new Date();
		currentDate = currentDate.from(timeservice.getCurrentTime().toInstant());
		System.out.println("current Date is "+currentDate);
		int customerId = body.getCustomerId();
		
		
		List<BookingDetails> bookingDetails = bookingDao.getUserBookingDetails(customerId);
		List<BookingDetails> currentBookings = new ArrayList<>();
		
		if(bookingDetails.size()!=0) {
		for(int i=0;i<bookingDetails.size();i++){
			BookingDetails newBD = bookingDetails.get(i);
			String startDate = bookingDetails.get(i).getBookingStartDate();
			String endDate = bookingDetails.get(i).getBookingEndDate();
			
			Date start=formatter.parse(startDate);
			Date end=formatter.parse(endDate);
		
			if((start.before(currentDate) && end.after(currentDate)) && bookingDetails.get(i).getBookingStatus().equalsIgnoreCase("checkin")){
				
				currentBookings.add(newBD);
				
			}
			
		}
		
		if(StringUtils.isEmpty(currentBookings)) {
			return new ResponseEntity<List<BookingDetails>> (HttpStatus.NOT_FOUND);
		}
		
		//return new ResponseEntity<>("Past Bookings are:"+pastBookings, HttpStatus.OK);
		return new ResponseEntity<List<BookingDetails>> (currentBookings,HttpStatus.OK);
	}
		else {
			return new ResponseEntity<List<BookingDetails>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/getFutureUserBookingDetails")
	public ResponseEntity<List<BookingDetails>> FutureUserBookingDetails(@RequestBody BookingDetails body) throws ParseException, java.text.ParseException{
		System.out.println("Inside getFutureUserBookingDetails");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		Date currentDate = new Date();	
		currentDate = currentDate.from(timeservice.getCurrentTime().toInstant());
		System.out.println("current Date is "+currentDate);
		int customerId = body.getCustomerId();
		
		
		List<BookingDetails> bookingDetails = bookingDao.getUserBookingDetails(customerId);
		List<BookingDetails> futureBookings = new ArrayList<>();
		
		if(bookingDetails.size()!=0) {
		for(int i=0;i<bookingDetails.size();i++){
			BookingDetails newBD = bookingDetails.get(i);
			String startDate = bookingDetails.get(i).getBookingStartDate();
			String endDate = bookingDetails.get(i).getBookingEndDate();
			
			Date start=formatter.parse(startDate);
			Date end=formatter.parse(endDate);
		
			if(end.after(currentDate) && bookingDetails.get(i).getBookingStatus().equalsIgnoreCase("new")){
				
				futureBookings.add(newBD);
				
			}
			
		}
		
		if(StringUtils.isEmpty(futureBookings)) {
			return new ResponseEntity<List<BookingDetails>> (HttpStatus.NOT_FOUND);
		}
		
		//return new ResponseEntity<>("Past Bookings are:"+pastBookings, HttpStatus.OK);
		return new ResponseEntity<List<BookingDetails>> (futureBookings,HttpStatus.OK);
	}
		else {
			return new ResponseEntity<List<BookingDetails>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/getOwnerReport")
	public ResponseEntity<Object> getOwnerReport(@RequestBody BookingDetails body) throws ParseException, java.text.ParseException{
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		int propertyId = body.getPropertyId();
		String ownerEmail = body.getOwnerEmail();
		
		LinkedHashMap<String, Integer> report = new LinkedHashMap<String, Integer>();
		
		List<BookingDetails> reportDetails = bookingDao.findOwnerReport(propertyId, ownerEmail);
		
		if(reportDetails.size()!=0) {
			for(int i=0;i<reportDetails.size();i++){
				String date = reportDetails.get(i).getBookingEndDate();
				Date endDate=formatter.parse(date);
				int month = endDate.getMonth()+1;
				System.out.println("fefefe"+month);
				int bookPrice = reportDetails.get(i).getTotalPrice();
				int refundPrice = reportDetails.get(i).getRefundAmount();
				int penalityPrice = reportDetails.get(i).getPenaltyAmount();
				
				int finalPrice = bookPrice - refundPrice + penalityPrice;
				
				if(month == 1) {
					if(report.containsKey("January")) {
						int updatePrice = report.get("January") + finalPrice;
						report.put("January", updatePrice);
					}else {
						report.put("January", finalPrice);
					}
				}else if(month == 2) {
					if(report.containsKey("February")) {
						int updatePrice = report.get("February") + finalPrice;
						report.put("February", updatePrice);
					}else {
						report.put("February", finalPrice);
					}
				}else if(month == 3) {
					if(report.containsKey("March")) {
						int updatePrice = report.get("March") + finalPrice;
						report.put("March", updatePrice);
					}else {
						report.put("March", finalPrice);
					}
				}else if(month == 4) {
					if(report.containsKey("April")) {
						int updatePrice = report.get("April") + finalPrice;
						report.put("April", updatePrice);
					}else {
						report.put("April", finalPrice);
					}
				}else if(month == 5) {
					if(report.containsKey("May")) {
						int updatePrice = report.get("May") + finalPrice;
						report.put("May", updatePrice);
					}else {
						report.put("May", finalPrice);
					}
				}else if(month == 6) {
					if(report.containsKey("June")) {
						int updatePrice = report.get("June") + finalPrice;
						report.put("June", updatePrice);
					}else {
						report.put("June", finalPrice);
					}
				}else if(month == 7) {
					if(report.containsKey("July")) {
						int updatePrice = report.get("July") + finalPrice;
						report.put("July", updatePrice);
					}else {
						report.put("July", finalPrice);
					}
				}else if(month == 8) {
					if(report.containsKey("August")) {
						int updatePrice = report.get("August") + finalPrice;
						report.put("August", updatePrice);
					}else {
						report.put("August", finalPrice);
					}
				}else if(month == 9) {
					if(report.containsKey("September")) {
						int updatePrice = report.get("September") + finalPrice;
						report.put("September", updatePrice);
					}else {
						report.put("September", finalPrice);
					}
				}else if(month == 10) {
					if(report.containsKey("October")) {
						int updatePrice = report.get("October") + finalPrice;
						report.put("October", updatePrice);
					}else {
						report.put("October", finalPrice);
					}
				}else if(month == 11) {
					if(report.containsKey("November")) {
						int updatePrice = report.get("November") + finalPrice;
						report.put("November", updatePrice);
					}else {
						report.put("November", finalPrice);
					}
				}else if(month == 12) {
					if(report.containsKey("December")) {
						int updatePrice = report.get("December") + finalPrice;
						report.put("December", updatePrice);
					}else {
						report.put("December", finalPrice);
					}
				}
			}
			System.out.println("------"+report);
			return new ResponseEntity<>(report, HttpStatus.OK);
		} else{
			return new ResponseEntity<>("Report not found", HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	@PostMapping("/getCompleteOwnerReport")
	public ResponseEntity<Object> getCompleteOwnerReport(@RequestBody BookingDetails body) throws ParseException, java.text.ParseException{
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		String ownerEmail = body.getOwnerEmail();
		
		LinkedHashMap<String, Integer> report = new LinkedHashMap<String, Integer>();
		
		List<BookingDetails> reportDetails = bookingDao.findCompleteOwnerReport(ownerEmail);
		
		if(reportDetails.size()!=0) {
			for(int i=0;i<reportDetails.size();i++){
				String date = reportDetails.get(i).getBookingEndDate();
				Date endDate=formatter.parse(date);
				int month = endDate.getMonth()+1;
				System.out.println("fefefe"+month);
				int bookPrice = reportDetails.get(i).getTotalPrice();
				int refundPrice = reportDetails.get(i).getRefundAmount();
				int penalityPrice = reportDetails.get(i).getPenaltyAmount();
				
				int finalPrice = bookPrice - refundPrice + penalityPrice;
				
				if(month == 1) {
					if(report.containsKey("January")) {
						int updatePrice = report.get("January") + finalPrice;
						report.put("January", updatePrice);
					}else {
						report.put("January", finalPrice);
					}
				}else if(month == 2) {
					if(report.containsKey("February")) {
						int updatePrice = report.get("February") + finalPrice;
						report.put("February", updatePrice);
					}else {
						report.put("February", finalPrice);
					}
				}else if(month == 3) {
					if(report.containsKey("March")) {
						int updatePrice = report.get("March") + finalPrice;
						report.put("March", updatePrice);
					}else {
						report.put("March", finalPrice);
					}
				}else if(month == 4) {
					if(report.containsKey("April")) {
						int updatePrice = report.get("April") + finalPrice;
						report.put("April", updatePrice);
					}else {
						report.put("April", finalPrice);
					}
				}else if(month == 5) {
					if(report.containsKey("May")) {
						int updatePrice = report.get("May") + finalPrice;
						report.put("May", updatePrice);
					}else {
						report.put("May", finalPrice);
					}
				}else if(month == 6) {
					if(report.containsKey("June")) {
						int updatePrice = report.get("June") + finalPrice;
						report.put("June", updatePrice);
					}else {
						report.put("June", finalPrice);
					}
				}else if(month == 7) {
					if(report.containsKey("July")) {
						int updatePrice = report.get("July") + finalPrice;
						report.put("July", updatePrice);
					}else {
						report.put("July", finalPrice);
					}
				}else if(month == 8) {
					if(report.containsKey("August")) {
						int updatePrice = report.get("August") + finalPrice;
						report.put("August", updatePrice);
					}else {
						report.put("August", finalPrice);
					}
				}else if(month == 9) {
					if(report.containsKey("September")) {
						int updatePrice = report.get("September") + finalPrice;
						report.put("September", updatePrice);
					}else {
						report.put("September", finalPrice);
					}
				}else if(month == 10) {
					if(report.containsKey("October")) {
						int updatePrice = report.get("October") + finalPrice;
						report.put("October", updatePrice);
					}else {
						report.put("October", finalPrice);
					}
				}else if(month == 11) {
					if(report.containsKey("November")) {
						int updatePrice = report.get("November") + finalPrice;
						report.put("November", updatePrice);
					}else {
						report.put("November", finalPrice);
					}
				}else if(month == 12) {
					if(report.containsKey("December")) {
						int updatePrice = report.get("December") + finalPrice;
						report.put("December", updatePrice);
					}else {
						report.put("December", finalPrice);
					}
				}
			}
			System.out.println("------"+report);
			return new ResponseEntity<>(report, HttpStatus.OK);
		} else{
			return new ResponseEntity<>("Report not found", HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	
	@PostMapping("/getCustomerReport")
	public ResponseEntity<Object> getCustomerReport(@RequestBody BookingDetails body) throws ParseException, java.text.ParseException{
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		int customerId = body.getCustomerId();
		
		LinkedHashMap<String, Integer> report = new LinkedHashMap<String, Integer>();
		
		List<BookingDetails> reportDetails = bookingDao.findCustomerReport(customerId);
		
		if(reportDetails.size()!=0) {
			for(int i=0;i<reportDetails.size();i++){
				String date = reportDetails.get(i).getBookingEndDate();
				Date endDate=formatter.parse(date);
				int month = endDate.getMonth()+1;
				System.out.println("fefefe"+month);
				int bookPrice = reportDetails.get(i).getTotalPrice();
				int refundPrice = reportDetails.get(i).getRefundAmount();
				int penalityPrice = reportDetails.get(i).getPenaltyAmount();
				
				int finalPrice = bookPrice + refundPrice - penalityPrice;
				
				if(month == 1) {
					if(report.containsKey("January")) {
						int updatePrice = report.get("January") + finalPrice;
						report.put("January", updatePrice);
					}else {
						report.put("January", finalPrice);
					}
				}else if(month == 2) {
					if(report.containsKey("February")) {
						int updatePrice = report.get("February") + finalPrice;
						report.put("February", updatePrice);
					}else {
						report.put("February", finalPrice);
					}
				}else if(month == 3) {
					if(report.containsKey("March")) {
						int updatePrice = report.get("March") + finalPrice;
						report.put("March", updatePrice);
					}else {
						report.put("March", finalPrice);
					}
				}else if(month == 4) {
					if(report.containsKey("April")) {
						int updatePrice = report.get("April") + finalPrice;
						report.put("April", updatePrice);
					}else {
						report.put("April", finalPrice);
					}
				}else if(month == 5) {
					if(report.containsKey("May")) {
						int updatePrice = report.get("May") + finalPrice;
						report.put("May", updatePrice);
					}else {
						report.put("May", finalPrice);
					}
				}else if(month == 6) {
					if(report.containsKey("June")) {
						int updatePrice = report.get("June") + finalPrice;
						report.put("June", updatePrice);
					}else {
						report.put("June", finalPrice);
					}
				}else if(month == 7) {
					if(report.containsKey("July")) {
						int updatePrice = report.get("July") + finalPrice;
						report.put("July", updatePrice);
					}else {
						report.put("July", finalPrice);
					}
				}else if(month == 8) {
					if(report.containsKey("August")) {
						int updatePrice = report.get("August") + finalPrice;
						report.put("August", updatePrice);
					}else {
						report.put("August", finalPrice);
					}
				}else if(month == 9) {
					if(report.containsKey("September")) {
						int updatePrice = report.get("September") + finalPrice;
						report.put("September", updatePrice);
					}else {
						report.put("September", finalPrice);
					}
				}else if(month == 10) {
					if(report.containsKey("October")) {
						int updatePrice = report.get("October") + finalPrice;
						report.put("October", updatePrice);
					}else {
						report.put("October", finalPrice);
					}
				}else if(month == 11) {
					if(report.containsKey("November")) {
						int updatePrice = report.get("November") + finalPrice;
						report.put("November", updatePrice);
					}else {
						report.put("November", finalPrice);
					}
				}else if(month == 12) {
					if(report.containsKey("December")) {
						int updatePrice = report.get("December") + finalPrice;
						report.put("December", updatePrice);
					}else {
						report.put("December", finalPrice);
					}
				}
			}
			System.out.println("------"+report);
			return new ResponseEntity<>(report, HttpStatus.OK);
		} else{
			return new ResponseEntity<>("Report not found", HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PostMapping("/bulkUpdate")
	public ResponseEntity<Object> bulkUpdate(@RequestBody BookingDetails req) throws java.text.ParseException{
		System.out.println("Inside bulkUpdate!!!!!");
		
		List<BookingDetails> bookingDetails = bookingDao.allBookingDetails();
		
		for(int i=0;i<bookingDetails.size();i++) {
			String s_d = bookingDetails.get(i).getBookingStartDate();
			String e_d = bookingDetails.get(i).getBookingEndDate();
			String status = bookingDetails.get(i).getBookingStatus();
			int bookingId = bookingDetails.get(i).getBookingId();
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
			
			Date f_date = formatter.parse(s_d);
			Date e_date = formatter.parse(e_d);
			
			Date date = new Date();
			Calendar start = Calendar.getInstance();
			start.setTime(f_date);
			
			
			Calendar current = Calendar.getInstance();
			//date = date.from(timeservice.getCurrentTime().toInstant());
			current.setTime(date);
			
			if(start.before(current) && !status.equalsIgnoreCase("cancel")) {
				bookingDao.hostCancelBooking("cancel", 0, bookingId);
				
			}
		}
		
		//System.out.println(bookingDetails.get(0).getBookingId());
		return new ResponseEntity<>("Updated all bookings", HttpStatus.OK);
		
	}
	
	@PostMapping("/checkinCronJob")
	public ResponseEntity<Object> checkinCronJob() throws java.text.ParseException{
		System.out.println("Inside bulkUpdate!!!!!");
		
		List<BookingDetails> bookingDetails = bookingDao.allBookingDetails();
		
		for(int i=0;i<bookingDetails.size();i++) {
			String s_d = bookingDetails.get(i).getBookingStartDate();
			
			String status = bookingDetails.get(i).getBookingStatus();
			int bookingId = bookingDetails.get(i).getBookingId();
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
			
			Date f_date = formatter.parse(s_d);
		
			
			Date date = new Date();
			date = date.from(timeservice.getCurrentTime().toInstant());
		
			
			if(f_date.getDate()+1 == date.getDate()) {
				
				System.out.println("inside dateeeeee");
				bookingDao.hostCancelBooking("cancel", 0, bookingId);	
			}

		}
		
		return new ResponseEntity<>("Updated all bookings", HttpStatus.OK);	
	}
	
	@PostMapping("/checkoutCronJob")
	public ResponseEntity<Object> checkoutCronJob() throws java.text.ParseException{
		System.out.println("Inside bulkUpdate!!!!!");
		
		List<BookingDetails> bookingDetails = bookingDao.allBookingDetails();
		
		for(int i=0;i<bookingDetails.size();i++) {
			
			String e_d = bookingDetails.get(i).getBookingEndDate();
			String status = bookingDetails.get(i).getBookingStatus();
			int bookingId = bookingDetails.get(i).getBookingId();
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
			
			Date e_date = formatter.parse(e_d);
			
			Date date = new Date();
			date = date.from(timeservice.getCurrentTime().toInstant());
	
			
			if(e_date.getDate() == date.getDate()) {
				System.out.println("inside dateeeeee");
				bookingDao.hostCancelBooking("cancel", 0, bookingId);	
			}
		}
		return new ResponseEntity<>("Updated all bookings", HttpStatus.OK);	
	}
}
