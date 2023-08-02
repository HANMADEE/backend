package study.project.backend.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;

    @Enumerated(STRING)
    private Authority authority;

    @NotNull
    private String nickName;

    @NotNull
    private String email;

    @NotNull
    private String password;
}