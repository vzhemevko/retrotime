package org.retrotime.service;

import java.util.List;

import org.retrotime.dto.UserTagsDTO;
import org.retrotime.model.Team;

/**
 * Created by vzhemevko on 5/23/2015.
 */
public interface TeamService {

    void saveTeam(Team team);

    void createOrUpdate(List<UserTagsDTO> users, String teamName, int teamId);

    void delete(int teamId);

    List<Team> findAllTeams();

    Team getById(String id);
}
