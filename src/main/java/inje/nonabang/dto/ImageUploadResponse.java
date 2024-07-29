package inje.nonabang.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ImageUploadResponse {
    private List<String> imageUrl;

    public static ImageUploadResponse from(List<String> imageUrls){
        return ImageUploadResponse.builder()
                .imageUrl(imageUrls)
                .build();
    }
}