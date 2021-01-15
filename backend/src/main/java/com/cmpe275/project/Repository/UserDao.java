package com.cmpe275.project.Repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.project.Entity.User;


public interface UserDao extends JpaRepository<User, Integer>{
	
	ArrayList<User> findUserByUserEmail(String userEmail);
	
	ArrayList<User> findUserByUserName(String userName);
	
	@Query(value = "select * from users where user_id = ?1", nativeQuery = true)
	ArrayList<User> findUserEmailById(int userId);
	
	@Query(value = "select * from users where user_id = ?1", nativeQuery = true)
	ArrayList<User> findUserByCustomerId(int customerId);
	
}
