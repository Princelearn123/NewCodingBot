package com.lcwd.user.service.external.services;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.lcwd.user.service.entities.Rating;

@FeignClient(name="RATING-SERVICE")
public interface RatingService {

	//post
	
	@PostMapping("/ratings")
	public ResponseEntity<Rating> createRating(Rating values);
	
	//get
	
	@GetMapping("/ratings/users/{userId}")
	public  ResponseEntity<List<Rating>> getRating(@PathVariable String userId);
	
	
}
