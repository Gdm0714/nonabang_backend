package inje.nonabang.controller;

import inje.nonabang.dto.ApiResponse;
import inje.nonabang.dto.LoginRequest;
import inje.nonabang.dto.MemberDTO;
import inje.nonabang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")
    public void save(@RequestBody MemberDTO memberDTO) {
        System.out.println("MemberController.save");
        System.out.println("memberDTO= " + memberDTO);


        memberService.save(memberDTO);
    }


    @PostMapping("/member/login")
    public ApiResponse login(@RequestBody LoginRequest memberDTO) {

        return ApiResponse.success(memberService.login(memberDTO));

    }


}
