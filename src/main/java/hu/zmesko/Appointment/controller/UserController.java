package hu.zmesko.Appointment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.zmesko.Appointment.model.AppUser;
import hu.zmesko.Appointment.model.AuthRequest;
import hu.zmesko.Appointment.model.JwtResponse;
import hu.zmesko.Appointment.security.service.AppUserService;
import hu.zmesko.Appointment.security.service.JwtService;
import hu.zmesko.Appointment.security.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
@ControllerAdvice
public class UserController {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final AppUserService appUserService;

    private final LoginAttemptService loginAttemptService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest,
            HttpServletResponse response) {
        if (loginAttemptService.isBlocked(authRequest.getUsername())) {
            return ResponseEntity.status(429).body("Too many failed attempts. Try again later.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            loginAttemptService.loginSucceeded(authRequest.getUsername());
        } catch (BadCredentialsException e) {
            loginAttemptService.loginFaild(authRequest.getUsername());
            return ResponseEntity.badRequest().body("Bad credentials");
        } catch(AuthenticationException e) {
            loginAttemptService.loginFaild(authRequest.getUsername());
            return ResponseEntity.status(401).body("Authentication faild");
        }

        return ResponseEntity.ok(new JwtResponse(jwtService.generateToken(authRequest.getUsername())));
    }

    @PostMapping("/registration")
    public ResponseEntity<String> createUser(@RequestBody AppUser appUser) {
        appUserService.createAppUser(appUser.getUsername(), appUser.getPassword(), appUser.getRoles());
        return ResponseEntity.ok("Registration successful");
    }
}