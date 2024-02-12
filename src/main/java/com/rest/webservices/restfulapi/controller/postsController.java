package com.rest.webservices.restfulapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rest.webservices.restfulapi.DAO.Posts;
import com.rest.webservices.restfulapi.DAO.PostsRepository;

@RestController
public class postsController {
	PostsRepository postsRepo;

	public postsController(PostsRepository postsRepo) {
		this.postsRepo = postsRepo;
	}

	@GetMapping("/posts")
	public List<Posts> showAllPosts() {
		return postsRepo.findAll();
	}

	@GetMapping(path = "posts/id={id}")
	public Optional<Posts> findPost(@PathVariable int id) {
		return postsRepo.findById(id);
	}
}
