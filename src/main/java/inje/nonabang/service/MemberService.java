package inje.nonabang.service;


import inje.nonabang.dto.LoginRequest;
import inje.nonabang.dto.MemberDTO;
import inje.nonabang.entity.Member;
import inje.nonabang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor // controller와 같이 final 멤버변수 생성자 만드는 역할

public class MemberService {

    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO){
        //repository save 메서드 호출
        Member memberEntity = Member.toMemberEntity(memberDTO);
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
}
