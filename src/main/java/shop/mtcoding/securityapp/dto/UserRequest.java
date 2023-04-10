package shop.mtcoding.securityapp.dto;

import lombok.Getter;
import lombok.Setter;

public class UserRequest {

    @Getter
    @Setter
    public class joinDTO {
        private String username;
        private String password;
        private String email;

    }
}
