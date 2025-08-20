package org.example.jwtlab.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jwtlab.domain.auth.response.TokenResponse;
import org.example.jwtlab.domain.user.dto.request.LoginRequest;
import org.example.jwtlab.domain.user.dto.request.SignupRequest;
import org.example.jwtlab.domain.user.dto.request.UpdateUserRoleRequest;
import org.example.jwtlab.domain.user.dto.response.SignupResponse;
import org.example.jwtlab.domain.user.dto.response.UpdateUserRoleResponse;
import org.example.jwtlab.domain.user.entity.User;
import org.example.jwtlab.domain.user.exception.UserErrorCode;
import org.example.jwtlab.domain.user.exception.UserException;
import org.example.jwtlab.domain.user.repository.UserRepository;
import org.example.jwtlab.domain.user.service.UserService;
import org.example.jwtlab.global.jwt.UserAuth;
import org.example.jwtlab.global.response.BaseResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "User API", description = "유저 관련 API")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공"),
                    @ApiResponse(responseCode = "401", description = "잘못된 요청 (이미 존재하는 이메일)",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(
                                            name = "signupError",
                                            value = "{\"status\": 401, \"code\": \"U002\", \"message\": \"유효하지 않은 사용자 이메일입니다.\"}"
                                    )))
            }
    )
    @PostMapping("/auth/signup")
    public BaseResponse<SignupResponse> signup(
            @Valid @RequestBody SignupRequest signupRequest
            ) {
        SignupResponse response = userService.signup(signupRequest);
        return new BaseResponse<>(response);
    }

    @Operation(
            summary = "로그인",
            description = "사용자 로그인을 수행합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "401", description = "잘못된 요청 (유효하지 않은 이메일 또는 비밀번호)",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    name = "loginEmailError",
                                                    value = "{\"status\": 401, \"code\": \"U002\", \"message\": \"유효하지 않은 사용자 이메일입니다.\"}"
                                            ),
                                            @ExampleObject(
                                                    name = "loginPasswordError",
                                                    value = "{\"status\": 401, \"code\": \"U003\", \"message\": \"유효하지 않은 사용자 비밀번호입니다.\"}"
                                            )
                                    }))
            }
    )
    @PostMapping("/auth/login")
    public BaseResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response) {
        TokenResponse tokenResponse = userService.login(loginRequest, response);
        return new BaseResponse<>(tokenResponse);
    }

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
    @PatchMapping("/admin/user/{targetUserId}/roles")
    public BaseResponse<UpdateUserRoleResponse> updateUserRole(
            @PathVariable Long targetUserId,
            @AuthenticationPrincipal UserAuth userAuth
    ) {
        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                () -> new UserException(UserErrorCode.USER_ID_NOT_FOUND)
        );

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
