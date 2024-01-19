package mapple.mapple.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long id;

    @Column(nullable = false)
    private String storedName;
    @Column(nullable = false)
    private String updatedName;

    @Column(nullable = false)
    private String storeDir;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
}
