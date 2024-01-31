package mapple.mapple.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Embeddable
@Getter
public class Image {

    private String storedName;

    private String updatedName;

    private String storeDir;

    public static Image create(String storedName, String updatedName, String storeDir) {
        Image image = new Image();
        image.storedName = storedName;
        image.updatedName = updatedName;
        image.storeDir = storeDir;
        return image;
    }
}
