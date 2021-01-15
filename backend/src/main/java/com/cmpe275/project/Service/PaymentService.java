package com.cmpe275.project.Service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.project.Entity.Payments;
import com.cmpe275.project.Entity.Property;
import com.cmpe275.project.Repository.PaymentsDao;

@Service
@Transactional
public class PaymentService {
	
	@Autowired
	PaymentsDao paymentsDao;
	
	public List<Payments> getPaymentByUserId(int userId){
		return this.paymentsDao.findPayemntsByUserId(userId);
	}

}
