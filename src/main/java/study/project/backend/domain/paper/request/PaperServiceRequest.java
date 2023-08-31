package study.project.backend.domain.paper.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

public class PaperServiceRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Create {
        private String subject;
        private String theme;
        private Boolean isOpen;
        private Boolean isLikeOpen;

        public Optional<String> getTheme() {
            return Optional.ofNullable(theme);
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Update {
        private Long paperId;
        private String subject;
        private String theme;
        private Boolean isOpen;
        private Boolean isLikeOpen;
    }
}
