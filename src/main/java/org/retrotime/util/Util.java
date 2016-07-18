package org.retrotime.util;

import org.retrotime.model.User;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;

public class Util {

    private static final String USERNAME_INDX = "username";
    private static final String PERSONAL_NAME_INDX = "personalname";
    private static final String EMAIL_INDX = "email";
    private static final String PASSWD_INDX = "passwd";


    public static boolean isNotNull(Object object) {
        if (object == null) {
            return false;
        }
        return true;
    }


    public static boolean isNull(Object object) {
        if (object == null) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String... strs) {
        for (String str : strs) {
            if (str == null || str.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String... strs) {
        for (String str : strs) {
            if (str != null && !str.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    public static User decodeAccountInfo(String accountInfo) throws UnsupportedEncodingException {

        byte[] decodedB = Base64Utils.decode(accountInfo.getBytes(Constant.UTF_8));
        String decodedS = new String(decodedB, Constant.UTF_8);
        String[] decodedA = decodedS.split(":");
        String username = decodedA[0].startsWith(USERNAME_INDX) ? decodedA[0].substring(decodedA[0].indexOf("=") + 1) : "";
        String personalName = decodedA[1].startsWith(PERSONAL_NAME_INDX) ? decodedA[1].substring(decodedA[1].indexOf("=") + 1) : "";
        String email = decodedA[2].startsWith(EMAIL_INDX) ? decodedA[2].substring(decodedA[2].indexOf("=") + 1) : "";
        String passwd = decodedA[3].startsWith(PASSWD_INDX) ? decodedA[3].substring(decodedA[3].indexOf("=") + 1) : "";

        if (isNotEmpty(username, personalName, email, passwd)) {
            username = username.trim().toLowerCase();
            personalName = personalName.trim();
            email = email.trim().toLowerCase();
            passwd = passwd.trim();

            return new User(username, personalName, email, passwd);
        }
        else {
            throw new IllegalArgumentException("Not valid input data.");
        }
    }
}
