package com.newcode.meeting.dto;

import org.springframework.security.core.GrantedAuthority;

public enum   Role implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
