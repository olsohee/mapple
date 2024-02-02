package mapple.mapple.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Embeddable
@Getter
public class Image {

    @Column(nullable = false)
    private String storedName;

    @Column(nullable = false)
    private String updatedName;

    @Column(nullable = false)
    private String storeDir;

    public static Image create(String storedName, String updatedName, String storeDir) {
        Image image = new Image();
        image.storedName = storedName;
        image.updatedName = updatedName;
        image.storeDir = storeDir;
        return image;
    }
}
