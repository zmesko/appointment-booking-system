package hu.zmesko.Appointment.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hu.zmesko.Appointment.exception.UsernameAlreadyExistsException;
import hu.zmesko.Appointment.exception.WeakPasswordException;
import hu.zmesko.Appointment.model.AppUser;
import hu.zmesko.Appointment.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        AppUser appUser = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        AppUserDetails user = new AppUserDetails(appUser);

        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public AppUser createAppUser(String username, String password, String roles) {
        if (usersRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException();
        }

        validatePasswordStrength(password);
        String encodedPassword = passwordEncoder.encode(password);

        AppUser newUser = new AppUser(null, username, encodedPassword, roles);
        usersRepository.save(newUser);
        return newUser;
    }

    private void validatePasswordStrength(String password) {
        if (password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*\\d.*")) {

            throw new WeakPasswordException();
        }
    }

}
