package inje.nonabang.enumSet;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    GUEST("ROLE_GUEST"), USER("ROLE_USER");
    private final String key;
}