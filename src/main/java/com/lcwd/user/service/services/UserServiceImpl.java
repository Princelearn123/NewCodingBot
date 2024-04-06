package com.lcwd.user.service.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.external.services.RatingService;
import com.lcwd.user.service.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService  {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	  @Autowired
	  private HotelService hotelService;
	  
	  @Autowired
	  private RatingService ratingService;
	  
	//private Logger logger=(Logger) LoggerFactory.getLogger(UserService.class);
	
	  
	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		
		String randomUserId=UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		
		return userRepository.save(user);
		
	}

	@Override
	public List<User> getAllUser() {

		List<User>users=userRepository.findAll();
		
		users.forEach((u)->{
			String userId=u.getUserId();
			String url="http://localhost:8083/ratings/users/"+userId;
			
			@SuppressWarnings("unchecked")
			List<Rating>forObject=restTemplate.getForObject(url,ArrayList.class );
			//logger.info(forObject.toString(),"");
			//System.out.println(forObject.toString());
			u.setRatings(forObject);
			
			
		});
		return users;
	}

	@Override
	public User getUser(String userId) {
		User user= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user with given id "+userId+" is not found"));
		//fetch rating of the above user from RATING SERVICE
		//String url="http://RATING-SERVICE/ratings/users/"+userId;
		
//		@SuppressWarnings("unchecked")
//		Rating[] ratings=restTemplate.getForObject(url,Rating[].class );
		//logger.info(forObject.toString(),"");
		//System.out.println(forObject.toString());
		
		ResponseEntity<List<Rating>> ratings=ratingService.getRating(userId);
		
		List<Rating> ratingList=ratings.getBody();
//		List<Rating> forObject=Arrays.stream(ratings).toList();
		
		List<Rating>ratingNew=ratingList.stream().map(rating->{
			
			//api call to hotel service to get hotel
			//ResponseEntity<Hotel>forEntity=restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(),Hotel.class);
			Hotel hotel=hotelService.getHotel(rating.getHotelId());
			rating.setHotel(hotel);	
			return rating;
		}).collect(Collectors.toList());
		
		user.setRatings(ratingList);
		
		return user;
		
	}

	@Override
	public ResponseEntity<Rating> createRating(Rating rating) {

		return ratingService.createRating(rating);
	}

}
