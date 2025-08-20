package org.example.jwtlab.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.jwtlab.domain.user.dto.response.UpdateUserRoleResponse;
import org.example.jwtlab.domain.user.service.UserService;
import org.example.jwtlab.global.jwt.UserAuth;
import org.example.jwtlab.global.response.BaseResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin API", description = "관리자 API")
public class AdminController {

    private final UserService userService;

    @Operation(
            summary = "권한 변경",
            description = "관리자 권한을 가진 사용자가 다른 유저의 권한을 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "권한 변경 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(
                                            name = "accessDeniedError",
                                            value = "{\"status\": 403, \"code\": \"ACCESS_DENIED\", \"message\": \"접근 권한이 없습니다.\"}"
                                    ))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(
                                            name = "userNotFoundError",
                                            value = "{\"status\": 404, \"code\": \"U001\", \"message\": \"존재하지 않는 사용자 입니다.\"}"
                                    )))
            }
    )
    @PatchMapping("/user/{targetUserId}/roles")
    public BaseResponse<UpdateUserRoleResponse> updateUserRole(
            @PathVariable Long targetUserId,
            @AuthenticationPrincipal UserAuth userAuth
    ) {
        Long authUserId = userAuth.getUserId();
        UpdateUserRoleResponse response = userService.updateUserRole(authUserId, targetUserId);
        return new BaseResponse<>(response);
    }

    /* 권한 변경 테스트용 (USER, ADMIN 모두 변경 가능)
    @PatchMapping("/admin/user/{targetUserId}/roles")
    public BaseResponse<UpdateUserRoleResponse> updateUserRole(
            @PathVariable Long targetUserId,
            @RequestBody @Valid UpdateUserRoleRequest updateUserRoleRequest,
            @AuthenticationPrincipal UserAuth userAuth
    ) {
        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                () -> new UserException(UserErrorCode.USER_ID_NOT_FOUND)
        );

        Long authUserId = userAuth.getUserId();
        UpdateUserRoleResponse response = userService.updateUserRole(authUserId, targetUserId, updateUserRoleRequest);
        return new BaseResponse<>(response);
    }

     */
}
