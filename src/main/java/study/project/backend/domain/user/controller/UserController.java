package study.project.backend.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.project.backend.domain.user.request.UserRequest;
import study.project.backend.domain.user.response.UserResponse;
import study.project.backend.domain.user.service.UserService;
import study.project.backend.global.common.CustomResponseEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    // 회원가입 API
    @PostMapping("/register")
    public CustomResponseEntity<UserResponse.Register> register(
            @RequestBody @Valid UserRequest.Register request
    ) {
        return CustomResponseEntity.success(userService.register(request.toServiceRequest()));
    }

    // 로그인 API
    @PostMapping("/login")
    public CustomResponseEntity<UserResponse.Login> login(
            @RequestBody @Valid UserRequest.Login request
    ) {
        return CustomResponseEntity.success(userService.login(request.toServiceRequest()));
    }

    // 로그아웃 API
    @DeleteMapping("/logout")
    public CustomResponseEntity<Void> logout(
            @AuthenticationPrincipal Long userId,
            @RequestHeader(value = "Authorization") String atk
    ) {
        return CustomResponseEntity.success(userService.logout(userId, atk));
    }

    // 소셜 로그인 API
    @PostMapping("/signin")
    public CustomResponseEntity<UserResponse.Login> socialLogin(
            @RequestParam String code, @RequestParam Platform platform
    ) {
        return CustomResponseEntity.success(userService.socialLogin(code, platform));
    }

    // 닉네임 수정 API
    @PatchMapping("/signin")
    public CustomResponseEntity<Void> updateNickName(
            @RequestBody @Valid UserRequest.UpdateNickName request,
            @AuthenticationPrincipal Long userId
    ) {
        return CustomResponseEntity.success(userService.updateNickName(request.toServiceRequest(), userId));
    }

    // 유저 검색 API
    @GetMapping("/users")
    public CustomResponseEntity<List<UserResponse.Search>> searchUsers(
            @RequestBody @Valid UserRequest.Search request
    ) {
        return CustomResponseEntity.success(userService.searchUsers(request.toServiceRequest()));
    }

    // 내 정보 조회 API
    @GetMapping("")
    public CustomResponseEntity<UserResponse.Search> readUser(
            @AuthenticationPrincipal Long userId
    ) {
        return CustomResponseEntity.success(userService.readUser(userId));
    }

    // 내 정보 수정 API
    @PatchMapping("")
    public CustomResponseEntity<Void> updateUser(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserRequest.Update request
    ) {
        return CustomResponseEntity.success(userService.updateUser(userId, request.toServiceRequest()));
    }

    // 비밀번호 변경 API
    @PatchMapping("/password")
    public CustomResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserRequest.UpdatePassword request
    ) {
        return CustomResponseEntity.success(userService.updatePassword(userId, request.toServiceRequest()));
    }
}
