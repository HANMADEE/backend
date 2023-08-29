package study.project.backend.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.project.backend.domain.comment.request.CommentRequest;
import study.project.backend.domain.comment.response.CommentResponse;
import study.project.backend.domain.comment.service.CommentService;
import study.project.backend.global.common.CustomResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 한마디 작성 API
    @PostMapping("/paper/{paperId}/comment")
    public CustomResponseEntity<CommentResponse.Create> createComment(
            @PathVariable Long paperId,
            @RequestBody CommentRequest.Create request,
            @AuthenticationPrincipal Long userId
    ) {
        return CustomResponseEntity.success(
                commentService.createComment(paperId, request.toServiceRequest(), Optional.ofNullable(userId))
        );
    }

    // 내 한마디 조회 API
    @GetMapping("/comment")
    public CustomResponseEntity<List<CommentResponse.Read>> readMyComment(
            @AuthenticationPrincipal Long userId
    ) {
        return CustomResponseEntity.success(commentService.readMyComment(userId));
    }
}
