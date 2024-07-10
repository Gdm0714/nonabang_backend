package inje.nonabang.entity;

import inje.nonabang.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Table(name = "member_table")
@NoArgsConstructor
@AllArgsConstructor
public class Member { //table 역할

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long id;

    @Column(unique = true)
    private String memberEmail;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    @Column
    private String memberNumber;

    public static Member toMemberEntity(MemberDTO memberDTO){
        return Member.builder()
                .memberNumber(memberDTO.getMemberNumber())
                .memberPassword(memberDTO.getMemberPassword())
                .memberName(memberDTO.getMemberName())
                .memberEmail(memberDTO.getMemberEmail())
                .build();
    }

}
