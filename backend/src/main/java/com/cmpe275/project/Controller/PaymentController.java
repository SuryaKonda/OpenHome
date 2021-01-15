package com.cmpe275.project.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.project.Entity.Payments;
import com.cmpe275.project.Entity.Property;
import com.cmpe275.project.Entity.User;
import com.cmpe275.project.Repository.PaymentsDao;
import com.cmpe275.project.Repository.UserDao;
import com.cmpe275.project.Service.EmailVerification;
import com.cmpe275.project.Service.PaymentService;
import com.cmpe275.project.Service.UserService;
@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class PaymentController {
	
	@Autowired
	PaymentsDao paymentsDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	EmailVerification emailVerification;
	
	@PostMapping("/addPayment")
	public ResponseEntity<Object> addPayment(@RequestBody Payments req){
		System.out.println("Inside addPayment!!!!!");
		
		int userId = req.getUserId();      //to be changed to, user id should be from session 
		System.out.println("user id "+userId);
		String paymentMethod = req.getPaymentMethod();
		String cardDetails = req.getCardDetails();
		String cvv = req.getCvv();
		
		List <User> userDetails = userService.findUserById(userId);
		String userEmail = userDetails.get(0).getUserEmail();
		System.out.println("userEmail"+userEmail);
		
		List <Payments> paymentsList = paymentsDao.findPaymentsByUserDetails(cardDetails,userId);
		if(paymentsList.size() > 0) {
			return new ResponseEntity<>("Card details exists, please add new card", HttpStatus.BAD_REQUEST);
		}
		
		Payments payments = new Payments();
		payments.setUserId(userId);
		payments.setPaymentMethod(paymentMethod);
		payments.setCardDetails(cardDetails);
		payments.setCvv(cvv);
		
		try {
			payments = paymentsDao.save(payments);
			emailVerification.paymentAdded(userEmail);
			System.out.println("Payment added!!!!");
			return new ResponseEntity<>("Payment added!", HttpStatus.OK);
		}catch(ParseException e) {
			System.out.println(e);
			return new ResponseEntity<>("Payment not added!", HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/getPayment")
	public ResponseEntity<Object> getPayment(@RequestBody Payments req) throws ParseException{
		System.out.println("Inside getPayment!!!!!");
		
		//Payments payments = new Payments();
		int userId = req.getUserId();
		
		List<Payments> paymentsDetails = paymentService.getPaymentByUserId(userId);
		
		if(paymentsDetails.size() != 0) {
		System.out.println("payment details reterived");
		return new ResponseEntity<>(paymentsDetails, HttpStatus.OK);
		}
		else {
		System.out.println("couldn't get payment details");
		return new ResponseEntity<>("couldn't get payment details", HttpStatus.NOT_FOUND);
		}
	}
	
	
}
