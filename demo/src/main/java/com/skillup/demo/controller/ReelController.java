package com.skillup.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.demo.dto.UserDto;
import com.skillup.demo.exception.PostException;
import com.skillup.demo.exception.UserException;
import com.skillup.demo.model.Reels;
import com.skillup.demo.model.User;
import com.skillup.demo.response.MessageResponse;
import com.skillup.demo.services.ReelService;
import com.skillup.demo.services.UserService;

@RestController
@RequestMapping("/api/reels")
public class ReelController {

    @Autowired
	private ReelService reelService;
	
	@Autowired
	private UserService userService;
	

	
	@PostMapping("/create")
	public ResponseEntity<Reels> createPostHandler(@RequestBody Reels reel,@RequestHeader("Authorization") String token) throws UserException{
		
		
		User user=userService.findUserProfile(token);
		
		UserDto userDto=new UserDto();
		
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUsername());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		
		reel.setUser(userDto);
		
		System.out.println("create reel------"+reel.getVideo());
		
		Reels createdReel=reelService.createReels(reel);
		

		
		return new ResponseEntity<Reels>(createdReel, HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Reels>> getAllReelHandler(){
		
		System.out.println("get all reel -------- ");
		List<Reels> reels=reelService.getAllReels();
		
		return new ResponseEntity<List<Reels>>(reels,HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/delete/{reelId}")
	public ResponseEntity<MessageResponse> deleteReelHandler(@PathVariable Integer reelId) throws PostException{
		
		reelService.deleteReels(reelId);
		
		MessageResponse res=new MessageResponse("Reel Deleted Succefully");
		
		return new ResponseEntity<MessageResponse> (res, HttpStatus.OK);
		
	}


}
