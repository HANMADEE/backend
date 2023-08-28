package study.project.backend.domain.comment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.project.backend.domain.comment.entity.Comment;
import study.project.backend.domain.user.entity.Users;

import java.util.Optional;

public class CommentResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Create {
        private Long id;
        private Long paperId;
        private Long userId;
        private String userName;
        private String content;
        private String imageUrl;
        private String font;
        private String sort;
        private String backgroundColor;
        private String kind;

        public static CommentResponse.Create response(Comment comment) {
            Users user = Optional.ofNullable(comment.getUser()).orElse(
                    Users.builder()
                            .id(0L)
                            .nickName("익명")
                            .build()
            );

            return Create.builder()
                    .id(comment.getId())
                    .paperId(comment.getPaper().getId())
                    .userId(user.getId())
                    .userName(user.getNickName())
                    .content(comment.getContent())
                    .imageUrl(comment.getImageUrl())
                    .font(comment.getFont())
                    .sort(comment.getSort())
                    .backgroundColor(comment.getBackgroundColor())
                    .kind(comment.getKind())
                    .build();
        }
    }
}
