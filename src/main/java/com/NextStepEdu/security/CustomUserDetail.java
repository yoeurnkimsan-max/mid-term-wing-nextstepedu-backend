package com.NextStepEdu.security;

import com.NextStepEdu.models.AccountStatus;
import com.NextStepEdu.models.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class CustomUserDetail implements UserDetails {
    private UserModel user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles();
    }

    @Override
    public  String getPassword() {
        return user.getPassword()   ;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }


}
