package com.taskMangementService.model.response;

import com.taskMangementService.model.entities.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public static enum LoginStatus {
        AUTHORIZED, UN_AUTHENTICATED, UN_AUTHORIZED
    }

    public final LoginStatus loginStatus;
    private String token;
    private String userName;
    private User.Role userRole;
}
