package org.retrotime.service;

import java.util.List;

import org.retrotime.dto.ContentPartDTO;
import org.retrotime.model.Content;

/**
 * Created by vzhemevko on 5/23/2015.
 */
public interface ContentService {

    Content getContentById(int contentId);

    Content saveContent(Content content);

    void updateContentPart(ContentPartDTO partDTO);

    void deleteContent(Content content);

    List<Content> findContForUserInRetro(int retroId, int userId);

    int initContent(int retroId, int userId);
}
