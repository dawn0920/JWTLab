package org.example.jwtlab.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.jwtlab.domain.user.entity.User;
import org.example.jwtlab.domain.user.enums.UserRole;

@Schema(description = "회원가입 응답 DTO")
public record SignupResponse(
        @Schema(description = "이메일", example = "test1@test.com")
        String email,

        @Schema(description = "비밀번호", example = "Password1@")
        String password,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "유저 역할", example = "USER")
        UserRole userRole
) {
        public static SignupResponse from(User user) {
                return new SignupResponse(
                        user.getEmail(),
                        user.getPassword(),
                        user.getName(),
                        user.getUserRole()
                );
        }
}