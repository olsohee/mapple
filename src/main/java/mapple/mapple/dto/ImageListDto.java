package mapple.mapple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class ImageListDto {

    private List<ImageDto> imageDtos = new ArrayList<>();
    private int imageCount;

    @AllArgsConstructor
    @Getter
    public static class ImageDto {
        private String storedName;

        private String updatedName;

        private String storeDir;
    }
}
