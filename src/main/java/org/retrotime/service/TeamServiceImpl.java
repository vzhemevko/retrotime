package org.retrotime.service;

import org.apache.log4j.Logger;
import org.retrotime.dto.UserTagsDTO;
import org.retrotime.model.Team;
import org.retrotime.model.User;
import org.retrotime.springdata.jpa.TeamRepository;
import org.retrotime.util.Util;
import org.retrotime.util.exception.TeamNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by vzhemevko on 5/23/2015.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    final static Logger logger = Logger.getLogger(TeamServiceImpl.class);

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private  UserService userService;

    @Override
    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    @Override
    public List<Team> findAllTeams() {
        return teamRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Team getById(String id) {
        return teamRepository.findOne(Integer.parseInt(id)).orElseThrow(() -> new TeamNotFoundException(id));
    }

    @Override
    public void createOrUpdate(List<UserTagsDTO> userTags, String teamName, int teamId) {

        if (Util.isEmpty(teamName) && teamId == 0) {
            String message = "Cannot create or modify without team name and team id.";
            RuntimeException ex = new IllegalArgumentException(message);
            logger.error(ex);
            throw ex;
        }

        Set<User> users = new HashSet<>();

        for (UserTagsDTO userTag : userTags) {
            User user = userService.getUserById(userTag.getUserId());
            users.add(user);
        }

        Team team;
        if (teamId != 0) {
            logger.info("Updating an existing team.");
            team = teamRepository.findOne(teamId).orElseThrow(()-> new TeamNotFoundException(teamId));
        }
        else if (Util.isNotEmpty(teamName)) {
            logger.info("Creating a new team with name : " + teamName);
            team = new Team();
        }
        else {
            RuntimeException ex = new IllegalArgumentException("Cannot create or modify without team name and team id.");
            logger.error(ex);
            throw ex;
        }
        team.setName(teamName.trim().toLowerCase());
        team.setActive(Boolean.TRUE);
        team.setUsers(users);
        this.saveTeam(team);

    }


    @Override
    public void delete(int teamId) {
        teamRepository.deleteById(teamId);
    }
}
