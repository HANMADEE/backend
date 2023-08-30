package study.project.backend.domain.paper.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class PaperRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Create {

        @NotNull(message = "주제는 필수 값 입니다.")
        private String subject;

        private String theme;

        @NotNull(message = "공개 여부는 필수 값 입니다.")
        private Boolean isOpen;

        @NotNull(message = "좋아요 공개 여부는 필수 값 입니다.")
        private Boolean isLikeOpen;

        public PaperServiceRequest.Create toServiceRequest() {
            return PaperServiceRequest.Create.builder()
                    .subject(subject)
                    .theme(theme)
                    .isOpen(isOpen)
                    .isLikeOpen(isLikeOpen)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Update {
        @NotNull(message = "ID는 필수 값 입니다.")
        private Long paperId;

        @NotNull(message = "주제는 필수 값 입니다.")
        private String subject;

        @NotNull(message = "테마는 필수 값 입니다.")
        private String theme;

        @NotNull(message = "공개 여부는 필수 값 입니다.")
        private Boolean isOpen;

        @NotNull(message = "좋아요 공개 여부는 필수 값 입니다.")
        private Boolean isLikeOpen;

        public PaperServiceRequest.Update toServiceRequest() {
            return PaperServiceRequest.Update.builder()
                    .paperId(paperId)
                    .subject(subject)
                    .theme(theme)
                    .isOpen(isOpen)
                    .isLikeOpen(isLikeOpen)
                    .build();
        }
    }
}
