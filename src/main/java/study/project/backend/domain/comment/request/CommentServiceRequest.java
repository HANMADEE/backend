package study.project.backend.domain.comment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentServiceRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Create {
        private String content;
        private String imageUrl;
        private String font;
        private String sort;
        private String backgroundColor;
        private String kind;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Update {
        private Long commentId;
        private String content;
        private String imageUrl;
        private String font;
        private String sort;
        private String backgroundColor;
        private String kind;
    }
}
