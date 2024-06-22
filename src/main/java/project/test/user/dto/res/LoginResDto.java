package project.test.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class LoginResDto {
    private int status;
    private String accessToken;
    private String refreshToken;
}
