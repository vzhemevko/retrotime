package org.retrotime.service;

import org.apache.log4j.Logger;
import org.retrotime.dto.ContentDTO;
import org.retrotime.dto.ContentPartDTO;
import org.retrotime.dto.RetroDTO;
import org.retrotime.model.Content;
import org.retrotime.model.Retro;
import org.retrotime.model.Team;
import org.retrotime.model.User;
import org.retrotime.springdata.jpa.RetroRepository;
import org.retrotime.util.Util;
import org.retrotime.util.exception.RetroNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.*;

/**
 * Created by vzhemevko on 5/23/2015.
 */
@Service
@Transactional
public class RetroServiceImpl implements RetroService {

    final static Logger logger = Logger.getLogger(RetroServiceImpl.class);

    @Autowired
    private RetroRepository retroRepository;

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Override
    public void saveRetro(Retro retro) {
        retroRepository.save(retro);
    }

    @Override
    public RetroDTO getRetroDTOById(int retroId) {
        Retro retro = retroRepository.findOne(retroId).orElseThrow(() -> new RetroNotFoundException(retroId));
        RetroDTO sprintRetroDTO = convertToDTO(retro);
        return sprintRetroDTO;
    }

    @Override
    public List<RetroDTO> getRetroListByTeamId(String teamId) {
        List<Retro> listRetro = retroRepository.findByTeamIdOrderByCreatedAtDesc(Integer.parseInt(teamId));
        List<RetroDTO> listRetroDTO = new ArrayList<>();
        for (Retro retro : listRetro) {
            listRetroDTO.add(convertToDTO(retro));
        }
        return listRetroDTO;
    }

    @Override
    public Optional<Retro> createRetroOrUpdateName(String name, String teamId, String retroId) {
        Team team = teamService.getById(teamId);
        if(Util.isNull(team)) {
            return Optional.empty();
        }
        Retro retro;
        if (Util.isNotEmpty(retroId)) {
            retro = retroRepository.findOne(Integer.parseInt(retroId))
                                                .orElseThrow(() -> new RetroNotFoundException(retroId));
            logger.debug("Updating a retro record wiht id : " + retroId + " of a team with id : " + teamId);
        }
        else {
           retro = new Retro();
           logger.debug("Creating a new retro record  for a team with id : " + teamId);
        }
        retro.setName(name.trim());
        retro.setActive(Boolean.TRUE);
        retro.setTeam(team);
        saveRetro(retro);
        logger.debug("Saved the retro record wiht id : " + retroId);
        return Optional.ofNullable(retro);
    }

    @Override
    public void deleteByIdAndTeamId(String retroId, String teamId) {
        retroRepository.deleteByIdAndTeamId(Integer.parseInt(retroId), Integer.parseInt(teamId));
        logger.debug("Deleted a retro record wiht id : " + retroId + " of a team with id : " + teamId);
    }

    @Override
    public Retro getRetroById(int retroId) {
        return retroRepository.findOne(retroId).orElseThrow(() -> new RetroNotFoundException(retroId));
    }

    private RetroDTO convertToDTO(Retro retro) {
        RetroDTO retroDTO = new RetroDTO();
        if (Util.isNotNull(retro)) {
            retroDTO.setName(retro.getName());
            retroDTO.setRetroId(retro.getId());
            retroDTO.setTeamId(retro.getTeam().getId());

            ContentPartDTO actionItems = new ContentPartDTO();
            actionItems.setPartId(ContentPartDTO.ACTION_ITEMS_ID);
            actionItems.setText(retro.getActionItems());
            actionItems.setRetroId(retro.getId());
            retroDTO.setActionItems(actionItems);

            // Start converting Content records for retro.


            Map<Integer, User> userIdsOfTeam = new HashMap<>();
            for (User user : retro.getTeam().getUsers()) {
                userIdsOfTeam.put(user.getId(), user);
                // Some users who are in a team can don't have
                // Content records. We should remember all users in team
                // and later create an empty Content records for those who don't have.
            }

            // Convert existing Content records.
            Map<Integer, ContentDTO> userContentMap = new HashMap<>();
            for (Content content : retro.getContentRecords()) {
                int userId = content.getUser().getId();

                ContentDTO contentDTO = new ContentDTO();
                contentDTO.setId(content.getId());
                if (userIdsOfTeam.containsKey(userId)) {
                    userIdsOfTeam.remove(userId); // Remove users that are in Team
                }

                contentDTO.setUserId(userId);
                contentDTO.setUsername(content.getUser().getName());
                contentDTO.setPersonalName(content.getUser().getPersonalName());
                contentDTO = setContentPartsDTO(retro.getId(), userId, content, contentDTO);

                userContentMap.put(userId, contentDTO);
            }
            // This means the users doesn't have a content record yet
            // for the retro. We should create empty record for each one.
            for (Integer userId : userIdsOfTeam.keySet()) {
                User user = userIdsOfTeam.get(userId);
                Content content = createAndPersistEmptyContent(userId, retro.getId());
                ContentDTO contentDTO = new ContentDTO();
                contentDTO.setUserId(content.getUser().getId());
                contentDTO.setUsername(user.getName());
                contentDTO.setPersonalName(user.getPersonalName());

                contentDTO = setContentPartsDTO(retro.getId(), userId, content, contentDTO);
                userContentMap.put(userId, contentDTO);
            }
            retroDTO.setUsersContentMap(userContentMap);
        }
        else {
            throw new RuntimeException("No retro object"); // TODO find better exception.
        }
        return retroDTO;
    }

    private Content createAndPersistEmptyContent(int userId, int retroId) {
        Content content = new Content();
        Retro retro = retroRepository.findOne(retroId).orElseThrow(() -> new RetroNotFoundException(retroId));
        User user = userService.getUserById(userId);

        content.setRetro(retro);
        content.setUser(user);
        contentService.saveContent(content);
        logger.debug("Created an empty content record [userId : "+ userId + " ] for a retro record with id :  " + retroId);

        content =  contentService.getContentById(content.getId());
        return content;
    }

    private ContentDTO setContentPartsDTO(int retroId, int userId, Content content, ContentDTO contentDTO) {
        String userPersonalName = content.getUser().getPersonalName();
        String username = content.getUser().getName();
        ContentPartDTO www = new ContentPartDTO(userId, ContentPartDTO.WWW_ID, retroId,
                                                content.getId(), username, userPersonalName, content.getWww());
        ContentPartDTO wcwi = new ContentPartDTO(userId, ContentPartDTO.WCWI_ID,retroId,
                                                content.getId(), username, userPersonalName, content.getWcwi());
        ContentPartDTO kudos = new ContentPartDTO(userId, ContentPartDTO.KUDOS_ID, retroId,
                                                content.getId(), username, userPersonalName, content.getKudos());

        contentDTO.addPart(ContentPartDTO.WWW_ID, www);
        contentDTO.addPart(ContentPartDTO.WCWI_ID, wcwi);
        contentDTO.addPart(ContentPartDTO.KUDOS_ID, kudos);
        return contentDTO;
    }
}
