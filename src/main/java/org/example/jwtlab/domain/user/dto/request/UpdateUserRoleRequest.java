package org.example.jwtlab.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.jwtlab.domain.user.enums.UserRole;

public record UpdateUserRoleRequest(
        @Schema(description = "권한", example = "ADMIN")
        UserRole userRole
) {
}
