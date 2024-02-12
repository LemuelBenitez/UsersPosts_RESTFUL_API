package com.rest.webservices.restfulapi.filtering;

import java.time.LocalDate;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.rest.webservices.restfulapi.DAO.UserRepository;
import com.rest.webservices.restfulapi.DAO.Users;

@RestController
public class FilteringController {
	UserRepository userRepo;

	public FilteringController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@GetMapping("filter")
	public Users filtering() {
		LocalDate date = LocalDate.now();
		return new Users(0, "Hi", date);
	}

	@GetMapping("filter-list")
	public MappingJacksonValue filteringList() { // refactor
		MappingJacksonValue map = new MappingJacksonValue(userRepo.findAll());
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "user_name");
		FilterProvider filters = new SimpleFilterProvider().addFilter("Users", filter);
		return map;
	}
}
