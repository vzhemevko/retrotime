package org.retrotime.controller;

import org.retrotime.dto.RetroDTO;
import org.retrotime.service.RetroService;
import org.retrotime.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by vzhemevko on 5/24/2015.
 */
@RestController
@Scope("session")
@RequestMapping("/retro")
public class RetroConroller {

    @Autowired
    private RetroService retroService;

    @Autowired
    private TeamService teamService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<RetroDTO>> createOrUpdateName(@RequestParam final String name,  @RequestParam final String teamId,
                                                                @RequestParam(required=false) final String retroId) {
        try {
            String errorMsg = "Cannot update or create a user. Probably due to a bad request.";
            retroService.createRetroOrUpdateName(name, teamId, retroId).orElseThrow(() -> new Exception(errorMsg));
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(retroService.getRetroListByTeamId(teamId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, params="retroId")
    public RetroDTO getRetroById(@RequestParam int retroId) {
        return retroService.getRetroDTOById(retroId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<RetroDTO> getRetroListByTeamId(@RequestParam String teamId) {
        return retroService.getRetroListByTeamId(teamId);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public List<RetroDTO> deleteByIdAndTeamId(@RequestParam final String retroId,
                                    @RequestParam final String teamId) {
        retroService.deleteByIdAndTeamId(retroId, teamId);
        return retroService.getRetroListByTeamId(teamId);
    }

    /*@RequestMapping(method = RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity update(@RequestBody final RetroDTO retroCont) {
        return retroService.updateContent(retroCont);
    }*/
}
