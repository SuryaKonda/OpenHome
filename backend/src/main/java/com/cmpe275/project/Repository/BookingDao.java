package com.cmpe275.project.Repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cmpe275.project.Entity.BookingDetails;
import com.cmpe275.project.Entity.Property;
public interface BookingDao extends JpaRepository<BookingDetails, Integer>{
	
	@Query(value = "select * from booking_details where booking_id = ?1", nativeQuery = true)
	ArrayList<BookingDetails> findBookingDetailsById(int bookingId);
	
	ArrayList<BookingDetails> findBookingByRoomId(int roomId);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE booking_details b SET b.booking_status =?1,b.refund_amount=?2 WHERE b.booking_id=?3",nativeQuery=true)
	void hostCancelBooking(String bookingStatus,int refund_amount,int bookingId);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE booking_details b SET b.booking_status =?1,b.penalty_amount=?2 WHERE b.booking_id=?3",nativeQuery=true)
	void guestCancelBooking(String bookingStatus,int penalty_amount,int bookingId);
	
	ArrayList<BookingDetails> getBookingByBookingId(int bookingId);
	
	ArrayList<BookingDetails> findBookingDetailsByPropertyId(int propertyId);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE booking_details SET booking_status = ?1 where booking_id=?2", nativeQuery=true)
	void updateBookingStatus(String status,int bookingId);
	
	@Query(value="SELECT *  from booking_details WHERE property_id=?1",nativeQuery=true)
	ArrayList<BookingDetails> findBookingDetailsByPID(int propertyId);
	
	@Query(value = "select * from booking_details where property_id = ?1", nativeQuery = true)
	ArrayList<BookingDetails> getOwnerBookingDetails(int propertyId);
	
	@Query(value = "select * from booking_details where customer_id = ?1", nativeQuery = true)
	ArrayList<BookingDetails> getUserBookingDetails(int customerId);
	
	@Query(value = "select * from booking_details where property_id = ?1 and owner_email = ?2 order by booking_end_date desc", nativeQuery = true)
	ArrayList<BookingDetails> findOwnerReport(int propertyId, String ownerEmail);
	
	@Query(value = "select * from booking_details where owner_email = ?1 order by booking_end_date desc", nativeQuery = true)
	ArrayList<BookingDetails> findCompleteOwnerReport(String ownerEmail);
	
	@Query(value = "select * from booking_details where customer_id = ?1 order by booking_end_date desc", nativeQuery = true)
	ArrayList<BookingDetails> findCustomerReport(int customerId);
	
	@Query(value = "select * from booking_details where booking_id = ?1", nativeQuery = true)
	ArrayList<BookingDetails> findCustomerId(int bookingId);
	
	@Query(value = "select * from booking_details",nativeQuery=true)
	ArrayList<BookingDetails> allBookingDetails();
}
