package inje.nonabang.service;


import inje.nonabang.dto.LoginRequest;
import inje.nonabang.dto.MemberDTO;
import inje.nonabang.entity.Authority;
import inje.nonabang.entity.Member;
import inje.nonabang.repository.AuthorityRepository;
import inje.nonabang.repository.MemberRepository;
import inje.nonabang.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor // controller와 같이 final 멤버변수 생성자 만드는 역할

public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(MemberDTO memberDTO){
        //repository save 메서드 호출

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        authorityRepository.save(authority);


        Member memberEntity = Member.toMemberEntity(memberDTO,authority, passwordEncoder);
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(LoginRequest memberDTO){
        Optional<Member> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        System.out.println(byMemberEmail);
        if(byMemberEmail.isPresent()){
            Member member = byMemberEmail.get();
            if(member.getMemberPassword().equals(memberDTO.getMemberPassword()))
            {
                MemberDTO dto = MemberDTO.toMemberDTO(member);
                return dto;
            }
            else {
                return null;
            }
        }
        else
        {
            return null;
        }

    }

    @Transactional(readOnly = true)
    public Optional<Member> getUserWithAuthorities(String username) {
        return memberRepository.findOneWithAuthoritiesByMemberName(username);
    }

    // 현재 securityContext에 저장된 username의 정보만 가져오는 메소드
    @Transactional(readOnly = true)
    public Optional<Member> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByMemberName);

  }
}
