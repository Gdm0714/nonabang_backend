package inje.nonabang.dto;


import lombok.*;

@Data
@NoArgsConstructor
@ToString
public class BoardRequest {
    private String title;
    private String content;
}
