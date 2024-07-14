package inje.nonabang.controller;

import inje.nonabang.dto.ApiResponse;
import inje.nonabang.dto.LoginRequest;
import inje.nonabang.dto.MemberDTO;
import inje.nonabang.entity.Member;
import inje.nonabang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

  private final MemberService memberService;


    @GetMapping("/member/save")
    public String saveForm(){
        return "save";
    }



    @PostMapping("/member/save")
    public void save(@RequestBody MemberDTO memberDTO) {
        memberService.save(memberDTO);
    }


    @PostMapping("/member/login")
    public ApiResponse login(@RequestBody LoginRequest memberDTO) {

        return ApiResponse.success(memberService.login(memberDTO));

    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Member> getMyUserInfo() {
        return ResponseEntity.ok(memberService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Member> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(memberService.getUserWithAuthorities(username).get());
    }


}
