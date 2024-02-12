package com.rest.webservices.restfulapi.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rest.webservices.restfulapi.DAO.Posts;
import com.rest.webservices.restfulapi.DAO.PostsRepository;
import com.rest.webservices.restfulapi.DAO.UserRepository;
import com.rest.webservices.restfulapi.DAO.Users;
import com.rest.webservices.restfulapi.errorHandlers.UserNotFoundException;

import jakarta.validation.Valid;

@RestController
public class userController {

	UserRepository userRepo;
	PostsRepository postsRepo;

	public userController(UserRepository userRepo, PostsRepository postsRepo) {
		this.userRepo = userRepo;
		this.postsRepo = postsRepo;
	}

	@GetMapping("users")
	public List<Users> findAllUsers() {
		return userRepo.findAll();
	}

	@GetMapping("users-internationalized")
	public List<Users> findAllInternationalUsers() {
		Locale lc = LocaleContextHolder.getLocale();// add in version 2
		return userRepo.findAll();
	}

	@GetMapping("users/id={id}")
	public EntityModel<Users> findlUser(@PathVariable int id) {
		EntityModel eM = EntityModel.of(userRepo.findById(id));
		if (userRepo.findById(id).isEmpty())
			throw new UserNotFoundException();
		WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).findAllUsers());
		eM.add(link.withRel("all-users"));
		return eM;
	}

	@PostMapping("createUser/name={name}/birthDate={birthDate}")
	public ResponseEntity<Object> createUser(@Valid Users user, @PathVariable String name,
			@PathVariable LocalDate birthDate) {
		user.setBirthDate(birthDate);
		user.setName(name);
		userRepo.save(user);
		URI location = ServletUriComponentsBuilder.fromPath("localhost:8080/users/").path("id={id}")
				.buildAndExpand(userRepo.save(user).getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PostMapping("createPost/id={id}")
	public ResponseEntity<Object> createPostForUser(@PathVariable int id, @Valid Posts post) {
		Optional<Users> user = userRepo.findById(id);
		if (user.isEmpty())
			throw new UserNotFoundException();
		post.setUser(user.get());
		Posts savedPost = postsRepo.save(post);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("id={id}")
				.buildAndExpand(savedPost.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("users/delete/id={id}")
	public void deleteUser(@PathVariable int id) {
		userRepo.deleteById(id);
	}
}
