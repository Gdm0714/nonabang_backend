package inje.nonabang.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import inje.nonabang.dto.MemberDTO;
import inje.nonabang.enumSet.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Set;

@Entity
@Builder
@Getter
@Table(name = "member_table")
@NoArgsConstructor
@AllArgsConstructor
public class Member { //table 역할

    @JsonIgnore
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

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;


    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "id" , referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;


    public static Member toMemberEntity(MemberDTO memberDTO , Authority authority, PasswordEncoder passwordEncoder){
=======
    private MemberRole role;

    private String provider;

    private String providerId;

    public static Member toMemberEntity(MemberDTO memberDTO){
        return Member.builder()
                .authorities(Collections.singleton(authority))
                .memberNumber(memberDTO.getMemberNumber())
                .memberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()))
                .memberName(memberDTO.getMemberName())
                .memberEmail(memberDTO.getMemberEmail())
                .build();
    }

}
