package study.project.backend.domain.paper.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.project.backend.domain.comment.entity.Comment;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.user.entity.Users;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PaperResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class SimpleRead {
        private Long id;
        private String subject;
        private String theme;
        private Boolean isOpen;
        private Boolean isLikeOpen;
        private Integer likes;

        public static SimpleRead response(Paper paper) {
            return SimpleRead.builder()
                    .id(paper.getId())
                    .subject(paper.getSubject())
                    .theme(paper.getTheme())
                    .isOpen(paper.getIsOpen())
                    .isLikeOpen(paper.getIsLikeOpen())
                    .likes(paper.getPaperLikes().size())
                    .build();
        }
    }

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
    public static class ALL {
        private Long id;
        private Long userId;
        private String userName;
        private String subject;
        private String theme;
        private String likes;
        private String createdAt;

        public static PaperResponse.ALL response(Paper paper) {
            String likes = (paper.getIsLikeOpen()) ?
                    String.valueOf(paper.getPaperLikes().size()) : "비공개";
            return ALL.builder()
                    .id(paper.getId())
                    .userId(paper.getUser().getId())
                    .userName(paper.getUser().getNickName())
                    .subject(paper.getSubject())
                    .theme(paper.getTheme())
                    .likes(likes)
                    .createdAt(paper.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
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
            String userName = Optional.ofNullable(comment.getUser())
                    .map(Users::getNickName)
                    .orElse("익명");

            return Comments.builder()
                    .id(comment.getId())
                    .userName(userName)
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
