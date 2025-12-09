package hu.zmesko.Appointment.security;

import java.util.Collection;
import java.util.stream.Collectors;

/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import hu.zmesko.Appointment.model.AppUser;
import hu.zmesko.Appointment.repository.UsersRepository;

public class AppUserDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUser = usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Collection<GrantedAuthority> authorities = appUser.getRoles().stream()
                                                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                                                        .map(SimpleGrantedAuthority::new)
                                                        .collect(Collectors.toList());

        UserDetails userDetails = new User(appUser.getUsername(), appUser.getPassword(), authorities);
        

        return userDetails;
    }

}*/
