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

    private String username;

    @Column(nullable = false)
    private String identifier;

    private String password;

    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    public static User create(String username, String email, String password, String phoneNumber) {
        User user = new User();
        user.username = username;
        user.identifier = email;
        user.password = password;
        user.phoneNumber = phoneNumber;
        user.oAuthProvider = OAuthProvider.NONE;
        return user;
    }

    public static User createOAuthUser(String identifier, OAuthProvider oAuthProvider) {
        User user = new User();
        user.identifier = identifier;
        user.oAuthProvider = oAuthProvider;
        return user;
    }
}
