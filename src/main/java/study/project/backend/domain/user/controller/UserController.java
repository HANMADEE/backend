package study.project.backend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.project.backend.domain.user.response.UserResponse;
import study.project.backend.domain.user.service.UserService;
import study.project.backend.global.common.CustomResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    @GetMapping("/signin")
    public CustomResponseEntity<UserResponse.Login> googleLogin(
            @RequestParam String code, @RequestParam Platform platform
    ) {
        return CustomResponseEntity.success(userService.googleLogin(code, platform));
    }
}
