package inje.nonabang.OAuth2.userInfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getNickname() {
        return "";
    }

    @Override
    public String getImageUrl() {
        return "";
    }

    @Override
    public String getEmail() {
        return "";
    }
}
