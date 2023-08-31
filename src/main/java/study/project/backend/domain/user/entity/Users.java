package study.project.backend.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private Long id;

    @Enumerated(STRING)
    private Authority authority;

    @NotNull
    private String nickName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String profileImageUrl;

    public void toUpdateNickname(String nickName) {
        this.nickName = nickName;
    }

    public void toUpdate(String nickName, String email) {
        this.nickName = nickName;
        this.email = email;
    }

    public void toUpdatePassword(String newPassword) {
        this.password = newPassword;
    }
}
