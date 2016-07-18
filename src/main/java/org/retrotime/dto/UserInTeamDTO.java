package org.retrotime.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vzhemevko on 05.09.15.
 */
public class UserInTeamDTO {

    private int userId;
    private String userEmail;
    private String username;
    private List<Integer> teamsIdList;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getTeamsIdList() {
        return teamsIdList;
    }

    public void setTeamsIdList(List<Integer> teamsIdList) {
        this.teamsIdList = teamsIdList;
    }

    public void addTeamId(Integer teamId) {
        if (getTeamsIdList() == null) {
            setTeamsIdList(new ArrayList<>());
        }
        getTeamsIdList().add(teamId);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
