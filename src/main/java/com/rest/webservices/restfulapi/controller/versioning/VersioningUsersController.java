package com.rest.webservices.restfulapi.controller.versioning;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rest.webservices.restfulapi.DAO.UserRepository;
import com.rest.webservices.restfulapi.DAO.Users;

@RestController
public class VersioningUsersController {

	UserRepository userRepo;

	public VersioningUsersController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@GetMapping(path = "usersget", params = "get")
	public List<Users> findAllUsers() {
		return userRepo.findAll();
	}

	@GetMapping(path = "users", headers = "API-Header=2")
	public List<Users> findAllHeaderUsers() {
		return userRepo.findAll();
	}

	@DeleteMapping(path = "users/delete/id={id}", produces = "app/vendor.company.v1-l+json")
	public void deleteUser(@PathVariable int id) {
		userRepo.deleteById(id);// Needs to be reconfigured
	}
}
