package org.retrotime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.retrotime.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="USERS")
public class User {

	private int id;

	private String name;

	private String email;

	private String passwd;

	private String personalName;

	// default role
	private int role = UserService.Roles.USER_ROLE.intVal();

	private byte[] avatar;

	private Set<Content> contentRecords = new HashSet<>(0);

	private Set<Team> teams = new HashSet<>(0);

	public User(String name, String personalName, String email, String passwd) {
		this.name = name;
		this.personalName = personalName;
		this.email = email;
		this.passwd = passwd;
	}

	public User(){}

	@Column(name = "PASSWD", nullable = false)
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@Column(name = "EMAIL", nullable = true, unique = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Column(name = "NAME", nullable = true, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AVATAR", nullable = true)
	@Lob
	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = { CascadeType.MERGE })
	public Set<Content> getContentRecords() {
		return contentRecords;
	}

	public void setContentRecords(Set<Content> contentRecords) {
		this.contentRecords = contentRecords;
	}

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users", cascade = { CascadeType.MERGE })
	public Set<Team> getTeams() {
		return teams;
	}

	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}
	@Column(name = "ROLE", nullable = false)
	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	@Column(name = "PERSONAL_NAME", nullable = false, unique = true)
	public String getPersonalName() {
		return personalName;
	}

	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}

	@Override
	public int hashCode() {
		int result = id
				+ name.hashCode()
				+ passwd.hashCode()
				+ role;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (id != other.id
				|| !name.equalsIgnoreCase(other.name)
				|| role != other.role) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", role=" + role + "]";
	}
}
