package org.retrotime.dto;

/**
 * Created by vzhemevko on 05.09.15.
 */
public class UserTagsDTO {

    int userId;

    /*
     * Represent tag name on the view. Basically username.
     */
    String text;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
