package com.example.demo.service;

import java.util.Optional;

import com.example.demo.model.User;

public interface UserService {
	Integer saveUser(User user);
	Optional<User> findByUsername(String username);
}
