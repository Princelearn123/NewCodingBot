package com.lcwd.user.service.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;

public interface UserService {
	
	
	//create 
	User saveUser(User user);
	
	
	//get all user
	List<User>getAllUser();
	
	//get single user of given user Id
	
	User getUser(String userId);


	ResponseEntity<Rating> createRating(Rating rating);
	
	//TODO :delete 
	//TODO:update
	

}
