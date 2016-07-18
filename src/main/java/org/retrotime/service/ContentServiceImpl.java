package org.retrotime.service;

import org.apache.log4j.Logger;
import org.retrotime.dto.ContentPartDTO;
import org.retrotime.model.Content;
import org.retrotime.model.Retro;
import org.retrotime.model.User;
import org.retrotime.springdata.jpa.ContentRepository;
import org.retrotime.util.exception.ContentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by vzhemevko on 5/23/2015.
 */
@Service
@Transactional
public class ContentServiceImpl implements  ContentService {

    final static Logger logger = Logger.getLogger(ContentServiceImpl.class);

    @Autowired
    private ContentRepository contRepository;

    @Autowired
    private RetroService retroService;

    @Autowired
    private UserService userService;

    @Override
    public Content saveContent(Content content) {
        return contRepository.save(content);
    }

    public int initContent(int retroId, int userId) {
        List<Content> contForUserInRetro = findContForUserInRetro(retroId, userId);
        if (contForUserInRetro.size() > 0) {
            // that's an issue. Delete all existing
            // and save new. Handle it more gently in future!
            logger.warn("Found more than one or more Content records for User with id: " + userId + " in a Retro with Id : "
                        + retroId + ". Expected to find zero. Deleting them...");
            for (Content cont : contForUserInRetro) {
                deleteContent(cont);
                logger.debug("Deleted Content for User with id :" + userId + " in a Retro with Id :" + retroId);
            }
        }
        Content content = new Content();
        Retro retro = retroService.getRetroById(retroId);
        User user = userService.getUserById(userId);
        content.setUser(user);
        content.setRetro(retro);
        // create a new content record.
        content = saveContent(content);
        logger.debug("Created a new Content record. for for User : " + user.getName() + " in a Retro with Id :" + retroId);
        return content.getId();
    }

    @Override
    public void updateContentPart(ContentPartDTO contPartDTO) {

        if (contPartDTO.getContentId() == 0
                    && contPartDTO.getPartId() != ContentPartDTO.ACTION_ITEMS_ID) {
            // part of a not existing content, we need to create it.
            // This is possible when user is not in a Team but joined the retro.
            handleNonExistingContent(contPartDTO);

        }
        // Parse the Content part and find appropriate Content record.
        // Content part can be action items type which is stored in Retro record.
        // If it's Action items it'll be update here.
        Optional<Content> optional = findContentOrUpdateActionItems(contPartDTO);

        if (!optional.isPresent()) {
            // it's action items content part or empty content.
            // If we are here it was updated and no need to go further.
            return;
        }
        Content content = optional.get();

        switch (contPartDTO.getPartId()) {
            case ContentPartDTO.WWW_ID:
                content.setWww(contPartDTO.getText());
                contRepository.save(content);
                break;

            case ContentPartDTO.WCWI_ID:
                content.setWcwi(contPartDTO.getText());
                contRepository.save(content);
                break;

            case ContentPartDTO.KUDOS_ID:
                content.setKudos(contPartDTO.getText());
                contRepository.save(content);
                break;

            case ContentPartDTO.ACTION_ITEMS_ID:

                break;

            default:
                break;
        }
    }

    @Override
    public Content getContentById(int contentId) {
        return contRepository.findOne(contentId).orElseThrow(() -> new ContentNotFoundException(contentId));
    }

    private void handleNonExistingContent(ContentPartDTO contPartDTO) {
        List<Content> contForUserInRetro = contRepository.findContForUserInRetro(contPartDTO.getRetroId(), contPartDTO.getUserId());
        String username = contPartDTO.getUsername();
        int retroId = contPartDTO.getRetroId();

        if (contForUserInRetro.size() > 0) {
            // that's an issue. Delete all existing
            // and save new. Handle it more gently in future!
            logger.warn("Found more than one or more Content records for User : " + username + " in a Retro with Id : "
                        + retroId + ". Expected to find zero. Deleting them...");
            for (Content cont : contForUserInRetro) {
                contRepository.delete(cont);
                logger.debug("Deleted Content for User :" + username + " in a Retro with Id :" + retroId);
            }
        }
        else {
            Content content = new Content();
            Retro retro = retroService.getRetroById(retroId);
            User user = userService.getUserById(contPartDTO.getUserId());
            content.setUser(user);
            content.setRetro(retro);
            // create a new content record.
            content = contRepository.save(content);
            logger.debug("Created a new Content record. for for User : " + username + " in a Retro with Id :" + retroId);
            // set the created record for a new incoming content part.
            contPartDTO.setContentId(content.getId());
        }
    }

    private Optional<Content> findContentOrUpdateActionItems(ContentPartDTO contPartDTO) {
        if (contPartDTO.getPartId() == ContentPartDTO.ACTION_ITEMS_ID) {
            int retroId = contPartDTO.getRetroId();
            Retro retro = retroService.getRetroById(retroId);
            retro.setActionItems(contPartDTO.getText());
            retroService.saveRetro(retro);
            return Optional.empty();
        }
        else {

            Content content  = contRepository.findOne(contPartDTO.getContentId())
                                                        .orElseThrow(() -> new ContentNotFoundException(contPartDTO.getContentId()));
           /* Team team = content.getRetro().getTeam();

            if (!team.getUsers().contains(content.getUser())) {
                // We have a content that belongs to user not in the Team
                if (Util.isEmpty(content.getWww(), content.getWcwi(),
                                    content.getKudos(), contPartDTO.getText())) {
                    // the content record don't have any information. Delete it.
                    contRepository.delete(content);
                    logger.debug("Found empty user's content. Deleting...since user is not in the Team.");
                    return Optional.empty();
                }
            }*/
            return Optional.of(content);
        }
    }


    @Override
    public void deleteContent(Content content) {
        contRepository.delete(content);
    }


    @Override
    public List<Content> findContForUserInRetro(int retroId, int userId) {
        return contRepository.findContForUserInRetro(retroId, userId);
    }
}
