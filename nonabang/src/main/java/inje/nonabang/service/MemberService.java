package inje.nonabang.service;


import inje.nonabang.dto.MemberDTO;
import inje.nonabang.entity.Member;
import inje.nonabang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // controller와 같이 final 멤버변수 생성자 만드는 역할

public class MemberService {

    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO){
        //repository save 메서드 호출
        Member memberEntity = Member.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }
}
