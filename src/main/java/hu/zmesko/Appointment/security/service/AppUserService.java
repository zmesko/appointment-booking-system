package hu.zmesko.Appointment.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.zmesko.Appointment.model.AppUser;
import hu.zmesko.Appointment.repository.UsersRepository;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        AppUser appUser = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        AppUserDetails user = new AppUserDetails(appUser);

        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
