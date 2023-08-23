package study.project.backend.domain.paper.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.project.backend.domain.paper.request.PaperRequest;
import study.project.backend.domain.paper.response.PaperResponse;
import study.project.backend.domain.paper.service.PaperService;
import study.project.backend.global.common.CustomResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paper")
public class PaperController {

    private final PaperService paperService;

    // 롤링페이퍼 만들기 API
    @PostMapping("")
    public CustomResponseEntity<PaperResponse.Create> createRollingPaper(
            @RequestBody @Valid PaperRequest.Create request, @AuthenticationPrincipal Long userId
    ) {
        return CustomResponseEntity.success(paperService.createRollingPaper(request.toServiceRequest(), userId));
    }

    // 롤링페이퍼 선물하기 API
    @PatchMapping("/gift")
    public CustomResponseEntity<PaperResponse.Create> giftRollingPaper(
            @RequestParam Long paperId,
            @RequestParam Long giftedUserId,
            @AuthenticationPrincipal Long userId
    ) {
        return CustomResponseEntity.success(paperService.giftRollingPaper(paperId, giftedUserId, userId));
    }
}
