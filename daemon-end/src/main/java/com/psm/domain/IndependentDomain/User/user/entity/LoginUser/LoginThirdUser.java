package com.psm.domain.IndependentDomain.User.user.entity.LoginUser;

import com.psm.domain.IndependentDomain.User.user.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginThirdUser implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = -979521410864292039L;

    private OAuth2ThirdAccountDTO oAuth2ThirdAccount;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return oAuth2ThirdAccount.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
