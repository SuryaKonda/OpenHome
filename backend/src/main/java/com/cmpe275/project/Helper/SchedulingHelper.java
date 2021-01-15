package com.cmpe275.project.Helper;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cmpe275.project.Controller.BookingController;

@Component
public class SchedulingHelper {

	@Autowired
	BookingController bookingController;
	
	@Scheduled(cron="0 0 3 * * *", zone="America/Los_Angeles")
	public void schedulingTest() throws ParseException {
		System.out.println("Yeah!!! Scheduling is working!!!");
//		bookingController.checkinCronJob();
//		bookingController.checkoutCronJob();
		
		
		
	}
	
}