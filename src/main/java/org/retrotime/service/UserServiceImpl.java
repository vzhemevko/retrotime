package org.retrotime.service;

import org.apache.log4j.Logger;
import org.retrotime.dto.UserDTO;
import org.retrotime.dto.UserInTeamDTO;
import org.retrotime.dto.UserTagsDTO;
import org.retrotime.model.Team;
import org.retrotime.model.User;
import org.retrotime.springdata.jpa.UserRepository;
import org.retrotime.util.Util;
import org.retrotime.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private Md5PasswordEncoder md5PasswordEncoder;

    @Autowired
    private UserRepository userRepository;

	public void saveUser(User user) {
	    if (user.getId() == 0) {
	        // new user
	        String encodedPasswd = md5PasswordEncoder.encodePassword(user.getPasswd(), user.getName());
	        user.setPasswd(encodedPasswd);
	    }
	    userRepository.save(user);
	    logger.debug("Saved a user with username : " + user.getName());
	}

	@Override
	public UserDTO saveUserUponDTO(UserDTO userDTO) {
		User user = getUserById(userDTO.getId());
		String personalName = userDTO.getPersonalName();
		String username = userDTO.getUsername();
		String email = userDTO.getEmail();
		String passwd = userDTO.getEmail();
		if (!Util.isNotEmpty(personalName, username, email)) {
		    throw new IllegalArgumentException("Personal data cannot be empty");
		}
		user.setPersonalName(personalName);
		user.setName(username);
		user.setEmail(email);

		if (!Util.isEmpty(passwd)) {
			String encodedPasswd = md5PasswordEncoder.encodePassword(passwd, username);
			user.setPasswd(encodedPasswd);
		}

		try {

			saveUser(user);

		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return generateUserDTO(getUserById(userDTO.getId()));
		}
		logger.debug("Updated user's personal data.");
		return generateUserDTO(user);
	}

	public List<User> getAllUsers() {
	    List<User> usersList = userRepository.findAll();
	    logger.debug("Providing a list of all users. Count : " + usersList.size());
		return usersList;
	}


	public List<UserInTeamDTO> getAllUsersInTeams() {
		List<User> userList = userRepository.findAll();
		List<UserInTeamDTO> userInTeamDTOList = new ArrayList<>();
		for (User user: userList) {
			UserInTeamDTO userInTeamDTO = new UserInTeamDTO();
			userInTeamDTO.setUserId(user.getId());
			userInTeamDTO.setUsername(user.getName());
			userInTeamDTO.setUserEmail(user.getEmail());
			for (Team team : user.getTeams()) {
				userInTeamDTO.addTeamId(team.getId());
			}
			userInTeamDTOList.add(userInTeamDTO);
		}
		logger.debug("Providing a list of all users in teams");
		return userInTeamDTOList;
	}

	public User getUserByNameAndPsswd(String username, String passwd) {
	    User user = userRepository.findUserByNameAndPasswd(username.trim(), passwd.trim());
		if (Util.isNotNull(user)) {
			logger.debug("Providing a user :  "  + user.getName()
					+ ". The user was found by name and passwd");
		}
		return user;
	}

	public List<UserTagsDTO> getUserTags(String query) {
		List<User> users = userRepository.findByPersonalNameContainingIgnoreCase(query);
		return generateUserTags(users);
	}

	@Override
	public List<UserTagsDTO> getUserTagsByTeamId(int teamId) {
		List<User> users = userRepository.findByTeamId(teamId);
		return generateUserTags(users);
	}

	@Override
	public List<UserTagsDTO> getScrumMaterTags() {
		List<User> users = userRepository.findByRole(Roles.SCRUM_MASTER_ROLE.intVal());
		return generateUserTags(users);
	}

	private List<UserTagsDTO> generateUserTags(List<User> users) {
		List<UserTagsDTO> usersTags = new ArrayList<>();
		for (User user : users) {
			UserTagsDTO userTag = new UserTagsDTO();
			userTag.setUserId(user.getId());
			userTag.setText(user.getPersonalName());
			usersTags.add(userTag);
		}
		logger.debug("Providing a list of user tags. Count :  "  + usersTags.size());
		return usersTags;
	}

	public User getUserById(int id) {
	    User user = userRepository.findOne(id).orElseThrow(() -> new UserNotFoundException(id));
	    logger.debug("Providing a user :  "
	                    + user.getName() + ". The user was found by id");
		return user;
	}

	public Optional<User> getUserByName(String name) {
		logger.info("Looking for user with name : " + name);
		User user = userRepository.findUserByName(name.trim().toLowerCase());
		Optional<User> res = Optional.ofNullable(user);
        return res;
    }

	@Override
	public UserDTO generateUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setUsername(user.getName());
		userDTO.setPersonalName(user.getPersonalName());
		userDTO.setEmail(user.getEmail());
		//userDTO.setAvatar(user.getAvatar());
		userDTO.setRole(user.getRole());
		return userDTO;
	}

	@Override
	public void updateScrumMastersList(List<UserTagsDTO> sMasters) {
		List<User> existingSMasters = userRepository.findByRole(Roles.SCRUM_MASTER_ROLE.intVal());
		List<User> newSMasters = new ArrayList<>();

		for (UserTagsDTO userTag : sMasters) {
			User user = getUserById(userTag.getUserId());
			if (Util.isNotNull(user)) {
				newSMasters.add(user);
			} else {
				throw new IllegalArgumentException("Cannot find user with id: "  + userTag.getUserId());
			}
		}

		List<User> addSMasters = new ArrayList<>(newSMasters);
		addSMasters.removeAll(existingSMasters);

		List<User> removeSMasters = new ArrayList<>(existingSMasters);
		removeSMasters.removeAll(newSMasters);

		logger.debug("About to add new scrum masters. Count : " + addSMasters.size());
		for (User user : addSMasters) {
			user.setRole(Roles.SCRUM_MASTER_ROLE.intVal());
			saveUser(user);
		}

		logger.debug("About to remove existing scrum masters. Count : " + removeSMasters.size());
		for (User user : removeSMasters) {
			user.setRole(Roles.USER_ROLE.intVal());
			saveUser(user);
		}
	}
}
