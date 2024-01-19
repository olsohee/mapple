package mapple.mapple.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long id;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String content;

    private String url;

    @Column(nullable = false)
    private PublicStatus publicStatus;

    @Column(nullable = false)
    private Rating rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
