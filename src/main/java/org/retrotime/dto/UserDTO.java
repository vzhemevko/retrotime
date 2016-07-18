package org.retrotime.dto;

import org.retrotime.util.Constant;

/**
 * Created by vzhemevko on 21.09.15.
 */
public class UserDTO {
    private int id;
    private String username = Constant.EMPTY_STRING;
    private String personalName = Constant.EMPTY_STRING;
    private String email = Constant.EMPTY_STRING;
    /*private byte[] avatar;*/
    private int role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonalName() {
        return personalName;
    }

    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

/* public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }*/

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
