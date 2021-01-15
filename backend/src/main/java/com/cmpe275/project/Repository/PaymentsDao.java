package com.cmpe275.project.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cmpe275.project.Entity.Payments;
import com.cmpe275.project.Entity.Property;


public interface PaymentsDao extends JpaRepository<Payments, Integer> {
	
	ArrayList<Payments> findPayemntsByUserId(int userId);
	
	@Query(value = "select * from payments where card_details = ?1 and user_id=?2", nativeQuery = true)
	ArrayList<Payments> findPaymentsByUserDetails(String cardDetails, int userId);
}
