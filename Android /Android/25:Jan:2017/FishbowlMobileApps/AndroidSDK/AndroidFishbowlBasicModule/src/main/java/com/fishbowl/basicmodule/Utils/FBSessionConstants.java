package com.fishbowl.basicmodule.Utils;

/**
 * Created by Nauman Afzaal on 22/04/15.
 */
public class FBSessionConstants
{
    // SpendGo API Errors

    public enum SERVER_ERROR{
        EMAIL_NOT_VALIDATED(202), EMAIL_NOT_VALID_OR_NOT_EXIST(400), MEMBER_NOT_EXIST(404), TOKEN_EXPIRED(408), MEMBER_ALREADY_EXISTS(409), USERID_PASSWORD_INVALID(401), INTERNAL_SERVER_ERROR(500), INVALID_AUTH_TOKEN(1010);
        public int value;

        private SERVER_ERROR(int value) {
            this.value = value;
        }
    };

    public enum LOOK_UP_STATUS
    {
        ACTIVATED("Activated"), NOT_FOUND("NotFound"), INVALID_EMAIL("InvalidEmail"), STARTED_ACCOUNT("StarterAccount");
        public final String value;

        private LOOK_UP_STATUS(String val)
        {
            this.value = val;
        }
    }
}
