package org.example.jwtlab.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.jwtlab.domain.user.entity.User;
import org.example.jwtlab.domain.user.enums.UserRole;

public record UpdateUserRoleResponse(
        @Schema(description = "이메일", example = "test1@test.com")
        String email,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "유저 역할", example = "USER")
        UserRole userRole
) {
    public static UpdateUserRoleResponse from(User user) {
        return new UpdateUserRoleResponse(
                user.getEmail(),
                user.getName(),
                user.getUserRole()
        );
    }
}
