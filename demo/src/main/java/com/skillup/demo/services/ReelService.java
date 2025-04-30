package com.skillup.demo.services;

import java.util.List;

import com.skillup.demo.model.Reels;

public interface ReelService {

    public Reels createReels(Reels reel);
	
	public void deleteReels(Integer reelId);
	
	public void editReels(Reels reel);
	
	public List<Reels> getAllReels();

}
