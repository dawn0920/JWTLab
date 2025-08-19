package org.example.jwtlab.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.example.jwtlab.domain.user.enums.UserRole;


@Schema(description = "회원가입 요청 DTO")
public record SignupRequest(
        @NotBlank
        @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
        @Schema(description = "이메일", example = "test1@test.com")
        String email,

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$", message = "비밀번호 형식이 올바르지 않습니다. 8자 이상, 대소문자 포함, 숫자 및 특수문자(@$!%*?&#) 포함")
        @Schema(description = "비밀번호", example = "Password1@")
        String password,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = "이름은 한글 또는 영문만 입력 가능합니다.")
        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "권한", example = "USER")
        UserRole userRole
) {
    public SignupRequest {
        if (userRole == null) {
            userRole = UserRole.USER;
        }
    }
}
