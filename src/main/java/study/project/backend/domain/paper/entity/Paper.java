package study.project.backend.domain.paper.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.project.backend.domain.BaseEntity;
import study.project.backend.domain.user.entity.Users;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Paper extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @NotNull
    private String subject;

    @NotNull
    private String theme;

    @NotNull
    private Boolean isOpen;
    @NotNull
    private Boolean isLikeOpen;

}
