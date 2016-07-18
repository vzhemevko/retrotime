package org.retrotime.dto;

import java.util.Map;

public class RetroDTO {

    private String name;

    private int teamId;

    private int retroId;

    private ContentPartDTO actionItems;

    private Map<Integer, ContentDTO> usersContentMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getRetroId() {
        return retroId;
    }

    public void setRetroId(int retroId) {
        this.retroId = retroId;
    }

    public ContentPartDTO getActionItems() {
        return actionItems;
    }

    public void setActionItems(ContentPartDTO actionItems) {
        this.actionItems = actionItems;
    }

    public Map<Integer, ContentDTO> getUsersContentMap() {
        return usersContentMap;
    }

    public void setUsersContentMap(Map<Integer, ContentDTO> userContentMap) {
        this.usersContentMap = userContentMap;
    }
}
