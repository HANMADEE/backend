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
}
