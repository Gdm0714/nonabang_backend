package inje.nonabang.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image_url")
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String fileName;


    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

}
