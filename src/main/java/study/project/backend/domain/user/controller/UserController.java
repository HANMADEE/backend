package study.project.backend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import study.project.backend.domain.user.response.UserResponse;
import study.project.backend.domain.user.service.UserService;
import study.project.backend.global.common.CustomResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/signin")
    public CustomResponseEntity<UserResponse.Login> socialLogin(
            @RequestParam String code, @RequestParam Platform platform
    ) {
        return CustomResponseEntity.success(userService.socialLogin(code, platform));
    }
}
