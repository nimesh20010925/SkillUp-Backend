package com.skillup.demo.services;

import java.util.List;

import com.skillup.demo.exception.StoryException;
import com.skillup.demo.exception.UserException;
import com.skillup.demo.model.Story;

public interface StoryService {

    public Story createStory(Story story,Integer userId) throws UserException;
	
	public List<Story> findStoryByUserId(Integer userId) throws UserException, StoryException;

}
