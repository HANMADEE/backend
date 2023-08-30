package study.project.backend.domain.paper.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.project.backend.domain.paper.request.PaperRequest;
import study.project.backend.domain.paper.response.PaperResponse;
import study.project.backend.domain.paper.service.PaperService;
import study.project.backend.global.common.CustomResponseEntity;

import java.util.List;

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

    // 내 롤링페이퍼 조회 API
    @GetMapping("")
    public CustomResponseEntity<List<PaperResponse.SimpleRead>> readMyRollingPaper(
            @AuthenticationPrincipal Long userId
    ) {
        return CustomResponseEntity.success(paperService.readMyRollingPaper(userId));
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

    // 롤링페이퍼 조회 API
    @GetMapping("/{paperId}")
    public CustomResponseEntity<PaperResponse.Read> readRollingPaper(
            @PathVariable Long paperId
    ) {
        return CustomResponseEntity.success(paperService.readRollingPaper(paperId));
    }

    // 롤링페이퍼 수정 API
    @PatchMapping("")
    public CustomResponseEntity<Void> updateRollingPaper(
            @RequestBody @Valid PaperRequest.Update request,
            @AuthenticationPrincipal Long userId
    ) {
        return CustomResponseEntity.success(
                paperService.updateRollingPaper(request.toServiceRequest(), userId)
        );
    }

    // 롤링페이퍼 삭제 API
    @DeleteMapping("/{paperId}")
    public CustomResponseEntity<Void> deleteRollingPaper(
            @PathVariable Long paperId,
            @AuthenticationPrincipal Long userId
    ) {
        return CustomResponseEntity.success(paperService.deleteRollingPaper(paperId, userId));
    }

}
