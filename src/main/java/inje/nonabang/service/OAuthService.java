package inje.nonabang.service;

import inje.nonabang.OAuth2.CustomOAuth2User;
import inje.nonabang.OAuth2.OAuthAttributes;
import inje.nonabang.entity.Member;
import inje.nonabang.enumSet.SocialType;
import inje.nonabang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    private static final String NAVER = "naver";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("attributes : " + attributes);
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        Member createdUser = getUser(extractAttributes, socialType);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getMemberEmail(),
                createdUser.getRole()
        );
    }

    private SocialType getSocialType(String registrationId) { // 소셜 타입을 가져오는 메소드
        if(NAVER.equals(registrationId)) { // 네이버인 경우
            return SocialType.NAVER;
        }
        return SocialType.GOOGLE; // 그 외의 경우는 구글로 간주
    }

    private Member getUser(OAuthAttributes attributes, SocialType socialType) { // 사용자를 가져오는 메소드
        Member findUser = memberRepository.findBySocialTypeAndSocialId(socialType,
                attributes.getOAuth2UserInfo().getId()).orElse(null); // 소셜 타입과 소셜 ID로 사용자를 찾음

        if(findUser == null) { // 사용자가 없는 경우
            return saveUser(attributes, socialType); // 사용자를 저장
        }
        return findUser; // 사용자를 반환
    }

    private Member saveUser(OAuthAttributes attributes, SocialType socialType) {
        Member createdUser = attributes.toEntity(socialType, attributes.getOAuth2UserInfo());
        return memberRepository.save(createdUser);
    }

    public Member saverUser(Member member){
        return memberRepository.save(member);
    }
}
