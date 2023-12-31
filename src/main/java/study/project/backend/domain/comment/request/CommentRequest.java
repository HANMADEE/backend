package study.project.backend.domain.comment.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Create {

        @NotNull(message = "내용은 필수 값 입니다.")
        private String content;

        private String imageUrl;

        @NotNull(message = "폰트는 필수 값 입니다.")
        private String font;

        @NotNull(message = "정렬은 필수 값 입니다.")
        private String sort;

        @NotNull(message = "배경색은 필수 값 입니다.")
        private String backgroundColor;

        @NotNull(message = "유형은 필수 값 입니다.")
        private String kind;

        public CommentServiceRequest.Create toServiceRequest() {
            return CommentServiceRequest.Create.builder()
                    .content(content)
                    .imageUrl(imageUrl)
                    .font(font)
                    .sort(sort)
                    .backgroundColor(backgroundColor)
                    .kind(kind)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Update {
        @NotNull(message = "코멘트 ID는 필수 값 입니다.")
        private Long commentId;
        @NotNull(message = "코멘트 내용은 필수 값 입니다.")
        private String content;
        @NotNull(message = "이미지 URL은 필수 값 입니다.")
        private String imageUrl;
        @NotNull(message = "폰트는 필수 값 입니다.")
        private String font;
        @NotNull(message = "정렬은 필수 값 입니다.")
        private String sort;
        @NotNull(message = "배경색은 필수 값 입니다.")
        private String backgroundColor;
        @NotNull(message = "관계 유형은 필수 값 입니다.")
        private String kind;

        public CommentServiceRequest.Update toServiceRequest() {
            return CommentServiceRequest.Update.builder()
                    .commentId(commentId)
                    .content(content)
                    .imageUrl(imageUrl)
                    .font(font)
                    .sort(sort)
                    .backgroundColor(backgroundColor)
                    .kind(kind)
                    .build();
        }
    }
}
