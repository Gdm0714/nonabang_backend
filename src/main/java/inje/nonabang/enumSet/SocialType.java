package inje.nonabang.enumSet;

import lombok.Getter;

@Getter
public enum SocialType {
    KAKAO("kakao"),
    GOOGLE("google");

    private final String key;

    SocialType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
