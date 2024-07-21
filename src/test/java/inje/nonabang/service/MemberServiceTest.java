package inje.nonabang.service;

import static org.junit.jupiter.api.Assertions.*;

import inje.nonabang.dto.LoginRequest;
import inje.nonabang.dto.MemberDTO;
import inje.nonabang.entity.Authority;
import inje.nonabang.entity.Member;
import inje.nonabang.repository.AuthorityRepository;
import inje.nonabang.repository.MemberRepository;
import inje.nonabang.utils.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // Given
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("test@example.com");
        memberDTO.setMemberPassword("password");

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.toMemberEntity(memberDTO, authority, passwordEncoder);

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(authorityRepository.save(any(Authority.class))).thenReturn(authority);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // When
        memberService.save(memberDTO);

        // Then
        verify(authorityRepository, times(1)).save(any(Authority.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testLoginSuccess() {
        // Given
        String email = "test@example.com";
        String password = "password";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMemberEmail(email);
        loginRequest.setMemberPassword(password);

        Member member = Member.builder()
                .memberEmail(email)
                .memberPassword(password)
                .build();

        when(memberRepository.findByMemberEmail(email)).thenReturn(Optional.of(member));

        // When
        MemberDTO result = memberService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getMemberEmail());
    }

    @Test
    void testLoginFailure() {
        // Given
        String email = "test@example.com";
        String password = "wrongpassword";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMemberEmail(email);
        loginRequest.setMemberPassword(password);

        Member member = Member.builder()
                .memberEmail(email)
                .memberPassword("password")
                .build();

        when(memberRepository.findByMemberEmail(email)).thenReturn(Optional.of(member));

        // When
        MemberDTO result = memberService.login(loginRequest);

        // Then
        assertNull(result);
    }

    @Test
    void testGetUserWithAuthorities() {
        // Given
        String username = "testUser";

        Member member = Member.builder()
                .memberName(username)
                .build();

        when(memberRepository.findOneWithAuthoritiesByMemberName(username)).thenReturn(Optional.of(member));

        // When
        Optional<Member> result = memberService.getUserWithAuthorities(username);

        // Then
        assertTrue(result.isPresent());
        assertEquals(username, result.get().getMemberName());
    }


}
