package org.retrotime.controller;


import org.hibernate.exception.ConstraintViolationException;
import org.retrotime.dto.UserDTO;
import org.retrotime.dto.UserInTeamDTO;
import org.retrotime.dto.UserTagsDTO;
import org.retrotime.model.User;
import org.retrotime.service.UserService;
import org.retrotime.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/users")
@Scope("session")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = {"/registration"}, method = RequestMethod.POST)
	public ResponseEntity<UserDTO> register(@RequestBody String account) {
		User user;
		UserDTO userDTO;

		try {
			user = Util.decodeAccountInfo(account);

			userService.saveUser(user);
			User userDB = userService.getUserByName(user.getName()).get();
			userDTO = userService.generateUserDTO(userDB);

		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} catch (ConstraintViolationException ex) {
			return new ResponseEntity<>(HttpStatus.CONFLICT); // already exists;
		}
		return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
	}

	@RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
	public HttpStatus logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		return HttpStatus.OK;
	}

	@RequestMapping(method = RequestMethod.GET,  params="username")
	public ResponseEntity<UserDTO> getUserByName(@RequestParam String username) {
		User user = userService.getUserByName(username).get();
		UserDTO userDTO =  userService.generateUserDTO(user);
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<UserInTeamDTO> getUsersList() {
		return userService.getAllUsersInTeams();
	}

	@RequestMapping(method = RequestMethod.GET, params="query")
	public List<UserTagsDTO> getUsersTags(@RequestParam String query) {
		return userService.getUserTags(query);
	}


	@RequestMapping(method = RequestMethod.GET, params="teamId")
	public List<UserTagsDTO> getUsersTagsByTeam(@RequestParam int teamId) {
		return userService.getUserTagsByTeamId(teamId);
	}

	@RequestMapping(method = RequestMethod.GET, params="smlist")
	public List<UserTagsDTO> getScrumMastersTags() {
		return userService.getScrumMaterTags();
	}

	@RequestMapping(method = RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
	public void updateScrumMastersTags(@RequestBody final List<UserTagsDTO> sMasters) {
		userService.updateScrumMastersList(sMasters);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes= MediaType.APPLICATION_JSON_VALUE)
	public UserDTO saveUser(@RequestBody final UserDTO user) {

		return userService.saveUserUponDTO(user);
	}
}
