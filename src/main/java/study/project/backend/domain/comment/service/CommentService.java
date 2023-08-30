package study.project.backend.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.comment.entity.Comment;
import study.project.backend.domain.comment.repository.CommentRepository;
import study.project.backend.domain.comment.request.CommentRequest;
import study.project.backend.domain.comment.request.CommentServiceRequest;
import study.project.backend.domain.comment.response.CommentResponse;
import study.project.backend.domain.paper.Repository.PaperRepository;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;
import study.project.backend.global.common.Result;
import study.project.backend.global.common.exception.CustomException;

import java.util.List;
import java.util.Optional;

import static study.project.backend.global.common.Result.*;
import static study.project.backend.global.common.Result.NOT_FOUND_PAPER;
import static study.project.backend.global.common.Result.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PaperRepository paperRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // 한마디 작성 API
    @Transactional
    public CommentResponse.Create createComment(Long paperId, CommentServiceRequest.Create request, Optional<Long> userId) {
        Users user = userId.map(this::getUser).orElse(null);
        Paper paper = getPaper(paperId);

        Comment saveComment = commentRepository.save(Comment.builder()
                .user(user)
                .paper(paper)
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .font(request.getFont())
                .sort(request.getSort())
                .backgroundColor(request.getBackgroundColor())
                .kind(request.getKind())
                .build());

        return CommentResponse.Create.response(saveComment);
    }

    // 내 한마디 조회 API
    public List<CommentResponse.Read> readMyComment(Long userId) {
        List<Comment> comments = commentRepository.findAllByUserWithFetch(userId);
        return comments.stream()
                .map(CommentResponse.Read::response)
                .toList();
    }

    // 내 한마디 수정 API
    @Transactional
    public Void updateComment(CommentServiceRequest.Update request, Long userId) {
        Comment comment = getComment(request.getCommentId());
        validateMyComment(comment, userId);

        comment.toUpdate(
                request.getContent(),
                request.getImageUrl(),
                request.getFont(),
                request.getSort(),
                request.getBackgroundColor(),
                request.getKind()
        );

        return null;
    }

    private void validateMyComment(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(NOT_MY_COMMENT);
        }
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(NOT_FOUND_COMMENT)
        );
    }
    private Paper getPaper(Long paperId) {
        return paperRepository.findById(paperId).orElseThrow(
                () -> new CustomException(NOT_FOUND_PAPER)
        );
    }

    private Users getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER)
        );
    }

}
