package com.cmpe275.project.Service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmpe275.project.Entity.BookingDetails;
import com.cmpe275.project.Entity.User;
import com.cmpe275.project.Repository.UserDao;

@Service
@Transactional
public class UserService {

	@Autowired
	UserDao userDao;
	
	public List<User> findUserById(int userId){
		return this.userDao.findUserEmailById(userId);
	}
	
	
	
}
