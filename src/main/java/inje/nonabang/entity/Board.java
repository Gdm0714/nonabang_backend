package inje.nonabang.entity;

import inje.nonabang.dto.BoardRequest;
import inje.nonabang.dto.ImageUploadResponse;
import inje.nonabang.service.BoardService;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Table(name = "Board_table")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long id;

    private String title;
    private String content;

    @ElementCollection
    @Column(name = "image_url")
    private List<String> Images;


    public static Board from(BoardRequest request,List<String> imageUrls){
        return Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .Images(imageUrls)
                .build();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
