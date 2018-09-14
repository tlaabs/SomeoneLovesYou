package com.slu.security;

import com.slu.domain.MemberVO;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(MemberVO user) {
        return new JwtUser(
                user.getUserid(),
                user.getUserpwd(),
                user.getUsername(),
                user.getEmail());

    }

}
