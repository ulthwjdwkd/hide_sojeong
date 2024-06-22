package project.test.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class AuthDto {
    private String loginId;
    private String password;
    private String role;
}
