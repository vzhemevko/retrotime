package org.retrotime.dto;

import org.retrotime.util.Constant;


/**
 * Created by vzhemevko on 1.10.15.
 */
public class ContentPartDTO {

    public static final int WWW_ID = 1; // What went well
    public static final int WCWI_ID = 2; // What can we improve
    public static final int KUDOS_ID = 3;
    public static final int ACTION_ITEMS_ID = 4;

    private int userId;

    private int partId;

    private int retroId;

    private int contentId;

    private String username = Constant.EMPTY_STRING;

    private String personalName = Constant.EMPTY_STRING;

    private String senderUuid = Constant.EMPTY_STRING;;

    private String senderName = Constant.EMPTY_STRING;;

    private String text = Constant.EMPTY_STRING;;

    private boolean locked;

    private boolean lockType;

    public ContentPartDTO() {
    }

    public ContentPartDTO(int userId, int partId, int retroId,
                            int contentId, String username,
                            String personalName, String text) {
        this.userId = userId;
        this.partId = partId;
        this.retroId = retroId;
        this.contentId = contentId;
        this.text = text;
        this.username = username;
        this.personalName = personalName;

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRetroId() {
        return retroId;
    }

    public void setRetroId(int retroId) {
        this.retroId = retroId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLockType() {
        return lockType;
    }

    public void setLockType(boolean isLockType) {
        this.lockType = isLockType;
    }

    public String getSenderUuid() {
        return senderUuid;
    }

    public void setSenderUuid(String senderUuid) {
        this.senderUuid = senderUuid;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public int hashCode() {
       int res = userId * retroId
                        * contentId
                        * partId;
       return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ContentPartDTO)) {
            return false;
        }
        ContentPartDTO other = (ContentPartDTO) obj;

        // Only this set of fields can identify
        // should be used to identify equality.
        // This is part of websocket communication logic.

        // A ContentPartDTO object is shared against multiply
        // websocket clients. To identify it we need to use this set
        // which is unique across all clients. All other fields can
        // vary from client to client.
        if (userId != other.userId
                || retroId != other.retroId
                || contentId != other.contentId
                || partId != other.partId) {
            return false;
        }
        return true;
    }
}
