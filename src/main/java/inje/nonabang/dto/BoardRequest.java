package inje.nonabang.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class BoardRequest {
    private String title;
    private String content;
    private List<MultipartFile> images;
}
