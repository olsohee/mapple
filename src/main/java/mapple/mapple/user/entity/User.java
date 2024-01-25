package mapple.mapple.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    private String password;

    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthUser oAuthUser;

    public static User create(String username, String email, String password, String phoneNumber) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.phoneNumber = phoneNumber;
        user.oAuthUser = OAuthUser.FALSE;
        return user;
    }

    public static User createOAuthUser(String username, String email) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.oAuthUser = OAuthUser.TRUE;
        return user;
    }
}
