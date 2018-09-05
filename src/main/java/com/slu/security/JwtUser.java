package com.slu.security;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by stephan on 20.03.16.
 */
public class JwtUser implements UserDetails {

    private final String userid;

    private final String email;

    public JwtUser(
          String userid,
          String email 
    ) {
        this.userid = userid;
        this.email = email;
    }

    @Override
    public String getUsername() {
        return userid;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return userid;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return new Date();
    }
}
