package inje.nonabang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import inje.nonabang.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder

public class MemberDTO {

    @NotBlank
    @Size(min = 3 , max = 50)
    private String memberEmail;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(min = 3 , max = 100)
    private String memberPassword;

    @NotBlank
    @Size(min = 3 , max = 50)
    private String memberName;

    @NotBlank
    @Size(min = 3 , max = 50)
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
