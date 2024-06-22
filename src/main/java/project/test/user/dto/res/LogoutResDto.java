package project.test.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class LogoutResDto {
    private int status;
    private String refresh;
}
