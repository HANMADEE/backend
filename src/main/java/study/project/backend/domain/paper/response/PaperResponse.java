package study.project.backend.domain.paper.response;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.user.entity.Users;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

public class PaperResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Create {
        private Long id;
        private Long userId;
        private String subject;
        private String theme;
        private Boolean isOpen;
        private Boolean isLikeOpen;

        public static PaperResponse.Create response(Paper paper) {
            return Create.builder()
                    .id(paper.getId())
                    .userId(paper.getUser().getId())
                    .subject(paper.getSubject())
                    .theme(paper.getTheme())
                    .isOpen(paper.getIsOpen())
                    .isLikeOpen(paper.getIsLikeOpen())
                    .build();
        }
    }
}
