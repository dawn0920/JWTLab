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
import org.example.jwtlab.domain.user.dto.response.SignupResponse;
import org.example.jwtlab.domain.user.repository.UserRepository;
import org.example.jwtlab.domain.user.service.UserService;
import org.example.jwtlab.global.response.BaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "User API", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

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
    @PostMapping("/signup")
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
    @PostMapping("/login")
    public BaseResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response) {
        TokenResponse tokenResponse = userService.login(loginRequest, response);
        return new BaseResponse<>(tokenResponse);
    }

}
