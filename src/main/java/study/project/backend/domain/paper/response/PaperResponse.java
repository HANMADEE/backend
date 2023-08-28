package study.project.backend.domain.paper.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.project.backend.domain.comment.entity.Comment;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.user.entity.Users;

import java.util.List;
import java.util.Optional;

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

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Read {
        private Long id;
        private Long userId;
        private String subject;
        private String theme;
        private Boolean isOpen;
        private Boolean isLikeOpen;
        private List<Comments> comments;

        public static PaperResponse.Read response(Paper paper) {
            return Read.builder()
                    .id(paper.getId())
                    .userId(paper.getUser().getId())
                    .subject(paper.getSubject())
                    .theme(paper.getTheme())
                    .isOpen(paper.getIsOpen())
                    .isLikeOpen(paper.getIsLikeOpen())
                    .comments(paper.getComments().stream()
                            .map(Comments::toEntity)
                            .toList())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Comments {
        private Long id;
        private String userName;
        private String content;
        private String imageUrl;
        private String font;
        private String sort;
        private String backgroundColor;
        private String kind;

        public static PaperResponse.Comments toEntity(Comment comment) {
            return Comments.builder()
                    .id(comment.getId())
                    .userName(comment.getUser().getNickName())
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
