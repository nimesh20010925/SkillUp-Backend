package com.skillup.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skillup.demo.dto.UserDto;
import com.skillup.demo.exception.UserException;
import com.skillup.demo.model.User;
import com.skillup.demo.repository.UserRepository;
import com.skillup.demo.security.JwtTokenClaims;
import com.skillup.demo.security.JwtTokenProvider;
import com.skillup.demo.util.UserUtil;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Override
    public User registerUser(User user) throws UserException {
        System.out.println("registered user ------ ");
        
        Optional<User> isEmailExist = repo.findByEmail(user.getEmail());
        if (isEmailExist.isPresent()) {
            throw new UserException("Email Already Exist");
        }
        
        Optional<User> isUsernameTaken = repo.findByUsername(user.getUsername());
        if(isUsernameTaken.isPresent()) {
            throw new UserException("Username Already Taken");
        }
        
        if(user.getEmail() == null || user.getPassword() == null || 
           user.getUsername() == null || user.getName() == null) {
            throw new UserException("Email, password, username and name are required");
        }
        
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setUsername(user.getUsername());
        newUser.setName(user.getName());
        
        return repo.save(newUser);
    }

    @Override
    public User findUserById(Integer userId) throws UserException {
        return repo.findById(userId)
            .orElseThrow(() -> new UserException("User not found with userid: " + userId));
    }

    @Override
    public String followUser(Integer reqUserId, Integer followUserId) throws UserException {
        User followUser = findUserById(followUserId);
        User reqUser = findUserById(reqUserId);
        
        UserDto follower = new UserDto();
        follower.setEmail(reqUser.getEmail());
        follower.setUsername(reqUser.getUsername());
        follower.setId(reqUser.getId());
        follower.setName(reqUser.getName());
        follower.setUserImage(reqUser.getImage());
        
        UserDto following = new UserDto();
        following.setEmail(followUser.getEmail());
        following.setUsername(followUser.getUsername());
        following.setId(followUser.getId());
        following.setName(followUser.getName());
        following.setUserImage(followUser.getImage());
        
        followUser.getFollower().add(follower);
        reqUser.getFollowing().add(following);
        
        repo.save(followUser);
        repo.save(reqUser);
        
        return "You are now following " + followUser.getUsername();
    }

    @Override
    public String unfollowUser(Integer reqUserId, Integer unfollowUserId) throws UserException {
        User unfollowUser = findUserById(unfollowUserId);
        User reqUser = findUserById(reqUserId);
        
        UserDto unfollow = new UserDto();
        unfollow.setEmail(reqUser.getEmail());
        unfollow.setUsername(reqUser.getUsername());
        unfollow.setId(reqUser.getId());
        unfollow.setName(reqUser.getName());
        unfollow.setUserImage(reqUser.getImage());
        
        unfollowUser.getFollower().removeIf(u -> u.getId().equals(reqUser.getId()));
        reqUser.getFollowing().removeIf(u -> u.getId().equals(unfollowUser.getId()));
        
        repo.save(unfollowUser);
        repo.save(reqUser);
        
        return "You have unfollowed " + unfollowUser.getUsername();
    }

    @Override
    public User findUserProfile(String token) throws UserException {
        token = token.substring(7);
        JwtTokenClaims jwtTokenClaims = jwtTokenProvider.getClaimsFromToken(token);
        String username = jwtTokenClaims.getUsername();
        
        return repo.findByEmail(username)
            .orElseThrow(() -> new UserException("User not exist with email: " + username));
    }

    @Override
    public User findUserByUsername(String username) throws UserException {
        return repo.findByUsername(username)
            .orElseThrow(() -> new UserException("User not exist with username: " + username));
    }

    @Override
    public List<User> findUsersByUserIds(List<Integer> userIds) {
        return repo.findAllUserByUserIds(userIds);
    }

    @Override
    public List<User> searchUser(String query) throws UserException {
        List<User> users = repo.findByQuery(query);
        if(users.isEmpty()) {
            throw new UserException("No users found");
        }
        return users;
    }

    @Override
    public User updateUserDetails(User updatedUser, User existingUser) throws UserException {
        if(!updatedUser.getId().equals(existingUser.getId())) {
            throw new UserException("You can't update another user"); 
        }
        
        if(updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
        if(updatedUser.getBio() != null) existingUser.setBio(updatedUser.getBio());
        if(updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
        if(updatedUser.getUsername() != null) existingUser.setUsername(updatedUser.getUsername());
        if(updatedUser.getMobile() != null) existingUser.setMobile(updatedUser.getMobile());
        if(updatedUser.getGender() != null) existingUser.setGender(updatedUser.getGender());
        if(updatedUser.getWebsite() != null) existingUser.setWebsite(updatedUser.getWebsite());
        if(updatedUser.getImage() != null) existingUser.setImage(updatedUser.getImage());
        
        return repo.save(existingUser);
    }

    @Override
    public List<User> popularUser() {
        List<User> users = repo.findAll();
        UserUtil.sortUserByNumberOfPost(users);
        return users.subList(0, Math.min(users.size(), 5));
    }
}