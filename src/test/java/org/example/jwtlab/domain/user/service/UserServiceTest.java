package org.example.jwtlab.domain.user.service;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @InjectMocks private UserService userService;

    @Test
    @DisplayName("유저 회원가입 - 성공")
    public void 유저_회원가입_성공() {
        SignupRequest request = new SignupRequest("test@tset.com", "Password1@", "test", UserRole.ADMIN);

        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(userRepository.save(any())).willReturn(new User("test@test.com", "encodedPassword", "test", UserRole.ADMIN));

        SignupResponse response = userService.signup(request);

        assertNotNull(response);
    }

    @Test
    @DisplayName("유저 회원가입 - 실패, 이미 존재하는 이메일 입니다.")
    public void 유저_회원가입_시_이미_존재하는_이메일이면_예외_처리() {
        SignupRequest request = new SignupRequest("test@tset.com", "Password1@", "test", UserRole.ADMIN);

        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(new User("test@test.com", "encodedPassword", "test", UserRole.ADMIN)));

        UserException exception = assertThrows(UserException.class, () ->
                userService.signup(request)
        );

        assertEquals(UserErrorCode.INVALID_USER_EMAIL, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 - 성공")
    public void 로그인_성공() {
        LoginRequest request = new LoginRequest("test@test.com", "Password1@");
        User user = new User("test@test.com", "encodedPassword", "test", UserRole.ADMIN);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtUtil.createToken(any(), any())).willReturn("mocked-token");

        TokenResponse response = userService.login(request, null);

        assertNotNull(response);
        assertEquals("mocked-token", response.accessToken());
    }

    @Test
    @DisplayName("로그인 - 실패, 존재하지 않는 계정")
    public void 로그인_시_가입하지_않는_이메일이면_예외_처리() {
        LoginRequest request = new LoginRequest("test@test.com", "Password1@");

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () ->
                userService.login(request, null)
        );

        assertEquals(UserErrorCode.INVALID_USER_EMAIL, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 - 실패, 비밀번호 미일치")
    public void 로그인_시_비밀번호가_일치하지_않으면_예외_처리() {
        LoginRequest request = new LoginRequest("test@test.com", "Password1@");
        User user = new User("test@test.com", "encodedPassword", "test", UserRole.ADMIN);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        UserException exception = assertThrows(UserException.class, () ->
                userService.login(request, null)
        );

        assertEquals(UserErrorCode.INVALID_USER_PASSWORD, exception.getErrorCode());
    }

    @Test
    @DisplayName("관리자 권한 부여 - 성공")
    public void 관리자_권한_부여_성공() {
        Long adminUserId = 1L;
        Long targetUserId = 2L;

        User adminUser = User.builder().id(adminUserId).userRole(UserRole.ADMIN).email("admin@test.com").build();
        User targetUser = User.builder().id(targetUserId).userRole(UserRole.USER).email("target@test.com").build();

        given(userRepository.findById(adminUserId)).willReturn(Optional.of(adminUser));
        given(userRepository.findById(targetUserId)).willReturn(Optional.of(targetUser));

        UpdateUserRoleResponse response = userService.updateUserRole(adminUserId, targetUserId);

        assertNotNull(response);
        assertEquals(UserRole.ADMIN, response.userRole());
    }

    @Test
    @DisplayName("관리자 권한 부여 - 실패, 권한 없음")
    public void 일반_사용자가_권한을_변경을_시도할_시_예외_처리() {
        Long userAId = 1L;
        Long userBId = 2L;

        User userA = User.builder().id(userAId).userRole(UserRole.USER).email("userA@test.com").build();
        User userB = User.builder().id(userBId).userRole(UserRole.USER).email("userB@test.com").build();

        given(userRepository.findById(userAId)).willReturn(Optional.of(userA));

        UserException exception = assertThrows(UserException.class, () ->
                userService.updateUserRole(userAId, userBId)
        );
        assertEquals(UserErrorCode.ACCESS_DENIED, exception.getErrorCode());
    }

    @Test
    @DisplayName("관리자 권한 부여 - 실패, 대상 사용자 없음")
    public void 존재하지_않는_사용자에게_권한_부여시_예외_처리() {
        Long adminUserId = 1L;
        Long nonExistentUserId = 999L;

        User adminUser = User.builder().id(adminUserId).userRole(UserRole.ADMIN).email("admin@test.com").build();

        given(userRepository.findById(adminUserId)).willReturn(Optional.of(adminUser));
        given(userRepository.findById(nonExistentUserId)).willReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () ->
                userService.updateUserRole(adminUserId, nonExistentUserId)
        );
        assertEquals(UserErrorCode.USER_ID_NOT_FOUND, exception.getErrorCode());
    }

}
