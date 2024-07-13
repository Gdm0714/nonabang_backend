package inje.nonabang.entity;
import inje.nonabang.dto.MemberDTO;
import inje.nonabang.enumSet.MemberRole;
import inje.nonabang.enumSet.SocialType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Table(name = "member_table")
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity{ //table 역할

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

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String provider;

    private String providerId;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private String refreshToken;

    public static Member toMemberEntity(MemberDTO memberDTO){
        return Member.builder()
                .memberNumber(memberDTO.getMemberNumber())
                .memberPassword(memberDTO.getMemberPassword())
                .memberName(memberDTO.getMemberName())
                .memberEmail(memberDTO.getMemberEmail())
                .build();
    }

    public Member updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
        return this;
    }

}
