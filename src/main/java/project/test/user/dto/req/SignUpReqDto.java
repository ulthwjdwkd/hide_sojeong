package project.test.user.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SignUpReqDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userid;

    @NotBlank(message = "이름을 입력해주세요.")
//    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "이름은 영문자 또는 한글만 입력 가능합니다.")
    private String username;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 6, message = "비밀번호는 6글자 이상이여야 합니다.")
//    @Pattern(
//            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*,.?]).+$",
//            message = "비밀번호는 영문, 숫자, 특정 특수문자(~!@#$%^&*,.?)를 각각 최소 하나 이상 포함해야 합니다."
//    )
    private String password;

    @NotBlank(message = "확인 비밀번호를 입력해주세요.")
    private String checkPassword;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "인증번호를 입력해주세요.")
    private String code;
}
