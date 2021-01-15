package com.cmpe275.project.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.project.Entity.BookingDetails;
import com.cmpe275.project.Repository.BookingDao;
import com.cmpe275.project.Service.TimeService;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class TimeController {

	@Autowired
	TimeService timeService;
	
	@Autowired
	BookingDao bookingDao;
	
	@GetMapping("/time")
	public OffsetDateTime getTime() {return timeService.getCurrentTime();}
	
	@GetMapping("/date")
	public LocalDate getDate() {
		return timeService.getCurrentTime().minusHours(8).toLocalDate();
	}
	
//	@PostMapping("/addoffset/{hours}/{mins}")
//	public ResponseEntity<Object> addOffsetToTime(@PathVariable("hours") long hours, @PathVariable("mins") long mins){
//		System.out.println("**************"+timeService.getCurrentTime());
//		timeService.addHours(hours);
//		timeService.addMins(mins);
//		System.out.println("-----------"+timeService.getCurrentTime());
//		
//		
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
	
	@PostMapping("/addoffset/{hours}/{mins}")
	public ResponseEntity<Object> addOffsetToTime(@PathVariable("hours") long hours, @PathVariable("mins") long mins) throws ParseException{
		System.out.println("******"+timeService.getCurrentTime());
		timeService.addHours(hours);
		timeService.addMins(mins);
		System.out.println("-----------"+timeService.getCurrentTime());
		
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
		//return new ResponseEntity<>("Updated all bookings", HttpStatus.OK);
		
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
