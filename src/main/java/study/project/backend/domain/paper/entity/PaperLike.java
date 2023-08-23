package study.project.backend.domain.paper.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.project.backend.domain.user.entity.Users;

import static jakarta.persistence.GenerationType.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PaperLike {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "paperId")
    private Paper paper;
}
