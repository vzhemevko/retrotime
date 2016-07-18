package org.retrotime.controller;

import org.retrotime.dto.UserTagsDTO;
import org.retrotime.model.Team;
import org.retrotime.service.TeamService;
import org.retrotime.service.UserService;
import org.retrotime.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by vzhemevko on 5/23/2015.
 */
@RestController
@Scope("session")
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
    public List<Team> create(@RequestBody final List<UserTagsDTO> users, @RequestParam(required = false) final String teamName,
                             @RequestParam(required = false) final Integer teamId) {
        teamService.createOrUpdate(users, teamName, Util.isNotNull(teamId) ? teamId.intValue() : 0);
        return list();
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<Team> list() {
        return teamService.findAllTeams();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public List<Team>  delete(int teamId) {
        teamService.delete(teamId);
        return list();
    }
}
