package com.cmpe275.project.Controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.project.Service.EmailVerification;
import com.cmpe275.project.Service.UserService;
import com.cmpe275.project.Entity.User;
import com.cmpe275.project.Repository.UserDao;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	EmailVerification emailVerification;
	
	@PostMapping("/signUp")
	public ResponseEntity<Object> signUp(@RequestBody User req){
		System.out.println("Inside user sign up!!!!!");
		
		String userName = req.getUserName();
		String userEmail = req.getUserEmail();
		String emailCheck[] = userEmail.split("@");
		String userPassword = req.getUserPassword();
		String profileImage = req.getProfileImage();
		String userPhone = req.getUserPhone();
		String accountType;
		
		if(emailCheck[1].equals("sjsu.edu")) {
			accountType = "2";
		} else {
			accountType = "1";
		}
		
		//checking whether email and password are empty
		
		if(userEmail.equals("")) {
			return new ResponseEntity<>("Please enter email", HttpStatus.OK);
		}
		if(userPassword.equals("")) {
			return new ResponseEntity<>("Please enter password", HttpStatus.OK);
		}
		
		// to check if the user already exixts
		List <User> users = userDao.findUserByUserEmail(userEmail);
		if(users.size() > 0) {
			return new ResponseEntity<>("Email already exists, please use different email", HttpStatus.OK);
		}
		List <User> userNames = userDao.findUserByUserName(userName);
		if(userNames.size() > 0) {
			return new ResponseEntity<>("User already exists, please try with different user name", HttpStatus.OK);
		}
		
		String userToken = String.valueOf(new Random(System.nanoTime()).nextInt(10000));
		
		User user = new User();
		user.setUserName(userName);
		user.setUserEmail(userEmail);
		user.setUserPassword(userPassword);
		user.setUserPhone(userPhone);
		user.setProfileImage(profileImage);
		user.setAccountType(accountType);
		user.setVerified(false);
		user.setUserToken(userToken);
		
		try {
			user = userDao.save(user);
			emailVerification.sendEmail(userToken, userEmail);
			System.out.println("user saved!!!!");
			return new ResponseEntity<>("Success!", HttpStatus.OK);
		}catch(ParseException e) {
			System.out.println(e);
			return new ResponseEntity<>("Please signUp again", HttpStatus.OK);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> loginUser(@RequestBody User user) throws ParseException{
		System.out.println("Inside login post");
		List<User> loginUser = userDao.findUserByUserEmail(user.getUserEmail());
		//System.out.println(loginUser.get(0).isVerified());
		if(loginUser!=null && loginUser.size() > 0 && loginUser.get(0).isVerified()) {
			System.out.println(loginUser.get(0).getUserEmail());
		if(loginUser!=null && loginUser.get(0).getUserPassword().equals(user.getUserPassword())) {
			return new ResponseEntity<>(loginUser, HttpStatus.OK);
		}
		else {
			//return ResponseEntity.notFound().build();
			  return new ResponseEntity<>("Invalid Credentials", HttpStatus.NOT_FOUND);
		}
		}else {
			if(loginUser.size() != 0 && !loginUser.get(0).isVerified()) {
				return new ResponseEntity<>("Please verify your email to login", HttpStatus.METHOD_NOT_ALLOWED);
			}
			else {
			return new ResponseEntity<>("Email doesn't match any account !!! Please Sign Up", HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@GetMapping("/activateLogin")
	public ResponseEntity<Object> activateUser(@RequestParam("userEmail") String userEmail, @RequestParam("userToken") String userToken){
		System.out.println("Inside activateUser!!!!!");
		System.out.println("userEmail " + userEmail);
		System.out.println("userToken " + userToken);
		
		List<User> users = userDao.findUserByUserEmail(userEmail);
		
		if(users.size() == 0) {
			return new ResponseEntity<>("Invalid user email", HttpStatus.OK);
		}
		
		User activateUser = users.get(0);
		if(activateUser.getUserToken().equals(userToken)) {
			activateUser.setVerified(true);
			userDao.save(activateUser);
			return new ResponseEntity<>("User validated", HttpStatus.OK);
		} else {
			System.out.println("couldn't verify user");
			return new ResponseEntity<>("Invalid activation code", HttpStatus.OK);
		}
	}
	
	
}
