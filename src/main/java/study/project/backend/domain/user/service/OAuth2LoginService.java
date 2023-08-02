package study.project.backend.domain.user.service;

import study.project.backend.domain.user.controller.Platform;
import study.project.backend.domain.user.response.UserResponse;

public interface OAuth2LoginService {
    Platform supports();
    UserResponse.OAuth toSocialEntityResponse(String code, Platform platform);
}
