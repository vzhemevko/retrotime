package org.retrotime.dto;

import java.util.HashMap;
import java.util.Map;

import org.retrotime.util.Util;

public class ContentDTO {

    private int id;

    private int userId;

    private String username;

    private String personalName;

    private Map<Integer, ContentPartDTO> partsMap = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Map<Integer, ContentPartDTO> getPartsMap() {
        return partsMap;
    }

    public void setPartsMap(Map<Integer, ContentPartDTO> partsMap) {
        this.partsMap = partsMap;
    }

    public void addPart(int id, ContentPartDTO part) {
        if (Util.isNull(partsMap)) {
            partsMap = new HashMap<>();
        }
        partsMap.put(Integer.valueOf(id), part);
    }
}
