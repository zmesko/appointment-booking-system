package hu.zmesko.Appointment.security.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import hu.zmesko.Appointment.model.AppUser;

public class AppUserDetails implements UserDetails {

    private final String userName;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public AppUserDetails(AppUser appUser) {
        this.userName = appUser.getUsername();
        this.password = appUser.getPassword();
        this.authorities = appUser.getRoles() == null ? List.of()
                : List.of(appUser.getRoles().split(","))
                        .stream().map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }
}
