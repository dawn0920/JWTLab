package org.example.jwtlab.domain.user.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.jwtlab.domain.auth.response.TokenResponse;
import org.example.jwtlab.domain.user.dto.request.LoginRequest;
import org.example.jwtlab.domain.user.dto.request.SignupRequest;
import org.example.jwtlab.domain.user.dto.request.UpdateUserRoleRequest;
import org.example.jwtlab.domain.user.dto.response.SignupResponse;
import org.example.jwtlab.domain.user.dto.response.UpdateUserRoleResponse;
import org.example.jwtlab.domain.user.entity.User;
import org.example.jwtlab.domain.user.enums.UserRole;
import org.example.jwtlab.domain.user.exception.UserErrorCode;
import org.example.jwtlab.domain.user.exception.UserException;
import org.example.jwtlab.domain.user.repository.UserRepository;
import org.example.jwtlab.global.jwt.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    public SignupResponse signup (SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.email()).isPresent()) {
            throw new UserException(UserErrorCode.INVALID_USER_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.password());

        User user = User.builder()
                .email(signupRequest.email())
                .password(encodedPassword)
                .name(signupRequest.name())
                .userRole(signupRequest.userRole())
                .build();

        userRepository.save(user);

        return SignupResponse.from(user);
    }

    // 로그인
    public TokenResponse login (LoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new UserException(UserErrorCode.INVALID_USER_EMAIL)
        );

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_USER_PASSWORD);
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getUserRole());

        return new TokenResponse(bearerToken);
    }

    // 관리자 권한 부여
    public UpdateUserRoleResponse updateUserRole (
            Long userId,
            Long targetUserId,
            UpdateUserRoleRequest updateUserRoleRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.USER_ID_NOT_FOUND)
        );

        if(!user.getUserRole().equals(UserRole.ADMIN)) {
            throw new UserException(UserErrorCode.ACCESS_DENIED);
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_ID_NOT_FOUND));


        targetUser.updateRole(updateUserRoleRequest.userRole());
        userRepository.save(targetUser);
        return UpdateUserRoleResponse.from(targetUser);
    }
}
