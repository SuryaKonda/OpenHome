package com.cmpe275.project.Service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.project.Entity.BookingDetails;
import com.cmpe275.project.Entity.Payments;
import com.cmpe275.project.Repository.BookingDao;

@Service
@Transactional
public class BookingService {

	@Autowired
	BookingDao bookingDao;
	
	public List<BookingDetails> findBookingsById(int bookingId){
		return this.bookingDao.findBookingDetailsById(bookingId);
	}
	
	public ArrayList<BookingDetails> findBookingByRoomId(int roomId){
		return this.bookingDao.findBookingByRoomId(roomId);
	}
	
	public ArrayList<BookingDetails> getBookingByBookingId(int bookingId){
		return this.bookingDao.getBookingByBookingId(bookingId);
	}
	
	public ArrayList<BookingDetails> findBookingDetailsByPropertyId(int propertyId){
		return this.bookingDao.findBookingDetailsByPropertyId(propertyId);
	}
	
	public void updateBookingStatusByBookingId(String status,int bookingId) {
		this.bookingDao.updateBookingStatus(status,bookingId);
	}
	
	public ArrayList<BookingDetails> findBookingDetailsByPID(int propertyId){
		System.out.println("Inside service");
		return this.bookingDao.findBookingDetailsByPID(propertyId);
	}
	
}
