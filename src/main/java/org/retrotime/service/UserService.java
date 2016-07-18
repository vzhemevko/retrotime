package org.retrotime.service;

import java.util.List;
import java.util.Optional;

import org.retrotime.dto.UserDTO;
import org.retrotime.dto.UserInTeamDTO;
import org.retrotime.dto.UserTagsDTO;
import org.retrotime.model.User;

public interface UserService {

	enum Roles {
		SCRUM_MASTER_ROLE(2, "SCRUM_MATER"), USER_ROLE(1, "USER");

		private int role;
		private String roleTxt;

		Roles(int role, String roleTxt) {
			this.role = role;
			this.roleTxt = roleTxt;
		}

		public int intVal() {
			return role;
		}

		@Override
		public String toString() {
			return roleTxt;
		}
	}

	void saveUser(User user);

	UserDTO saveUserUponDTO(UserDTO userDTO);

	List<User> getAllUsers();

	User getUserById(int id);

	Optional<User> getUserByName(String name);

	User getUserByNameAndPsswd(String username, String psswd);

	List<UserInTeamDTO> getAllUsersInTeams();

	List<UserTagsDTO> getUserTags(String query);

	List<UserTagsDTO> getUserTagsByTeamId(int teamId);

	List<UserTagsDTO> getScrumMaterTags();

	UserDTO generateUserDTO(User user);

	void updateScrumMastersList(List<UserTagsDTO> smasters);
}
