package inje.nonabang.entity;

import inje.nonabang.dto.BoardRequest;
import jakarta.persistence.*;
import lombok.*;


@Table(name = "Board_table")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Integer id;

    private String title;

    private String content;

    public static Board from(BoardRequest request){
        return Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
