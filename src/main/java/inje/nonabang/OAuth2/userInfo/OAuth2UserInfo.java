package inje.nonabang.OAuth2.userInfo;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getNickname();

    public abstract String getImageUrl();

    public abstract String getEmail();

    public static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return new GoogleOAuth2UserInfo(attributes);
    }

    public static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        return new KakaoOAuth2UserInfo(attributes);
    }
}
