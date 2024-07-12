package inje.nonabang.dto;

import inje.nonabang.entity.Member;
import lombok.*;

@Data
@Builder

public class MemberDTO {
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String memberNumber;


    public static MemberDTO toMemberDTO(Member memberEntity) {

        return MemberDTO.builder()
                .memberNumber(memberEntity.getMemberNumber())
                .memberEmail(memberEntity.getMemberEmail())
                .memberName(memberEntity.getMemberName())
                .memberPassword(memberEntity.getMemberPassword())
                .build();
    }

}
