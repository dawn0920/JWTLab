package org.example.jwtlab.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "사용자 관리", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공")
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
                    @ApiResponse(responseCode = "200", description = "로그인 성공")
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
            description = "사용자가 다른 유저의 권한을 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "권한 변경 성공")
            }
    )
    @PostMapping("/admin/user/{targetUserId}/roles")
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

}
