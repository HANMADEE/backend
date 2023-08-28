package study.project.backend.domain.comment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.project.backend.domain.BaseEntity;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.user.entity.Users;

import java.util.Optional;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "paperId")
    private Paper paper;

    @NotNull
    private String content;

    private String imageUrl;

    @NotNull
    private String font;

    @NotNull
    private String sort;

    @NotNull
    private String backgroundColor;

    @NotNull
    private String kind;
}
