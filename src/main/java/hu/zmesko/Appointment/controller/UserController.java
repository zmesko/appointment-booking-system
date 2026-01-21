package hu.zmesko.Appointment.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
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
import hu.zmesko.Appointment.security.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
@ControllerAdvice
public class UserController {

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final AuthenticationManager authenticationManager;

    private final AppUserService appUserService;

    private final LoginAttemptService loginAttemptService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest,
            HttpServletResponse response) {

        String username = authRequest.getUsername();

        if (loginAttemptService.isBlocked(username)) {
            return ResponseEntity.status(429).body("Too many failed attempts. Try again later.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));
            loginAttemptService.loginSucceeded(username);
        } catch (BadCredentialsException e) {
            loginAttemptService.loginFailed(username);
            return ResponseEntity.status(401).body("Bad credentials");
        } catch (AuthenticationException e) {
            loginAttemptService.loginFailed(username);
            return ResponseEntity.status(401).body("Authentication faild");
        }

        String refreshToken = jwtService.generateRefreshToken(username);
        refreshTokenService.putRefreshTokenInRedis(refreshToken);

        ResponseCookie cookie = buildResponseCookie(refreshToken);

        String accessToken = jwtService.generateAccessToken(authRequest.getUsername());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body((new JwtResponse(accessToken)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshTokenCookie) {

        if (refreshTokenCookie == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token missing");
        }

        if (!refreshTokenService.isTokenInRedis(refreshTokenCookie)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshTokenCookie);
        String newRefreshToken = refreshTokenService.rotateRefreshToken(refreshTokenCookie);
        ResponseCookie cookie = buildResponseCookie(newRefreshToken);

        String accessToken = jwtService.generateAccessToken(username);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new JwtResponse(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> deleteRefreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshTokenCookie) {

        if (refreshTokenCookie == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token missing");
        }

        if (!refreshTokenService.isTokenInRedis(refreshTokenCookie)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        refreshTokenService.deleteRefreshToken(refreshTokenCookie);

        return ResponseEntity.ok().body("Logout successful");
    }

    @PostMapping("/registration")
    public ResponseEntity<String> createUser(@RequestBody AppUser appUser) {
        appUserService.createAppUser(appUser.getUsername(), appUser.getPassword(), appUser.getRoles());
        return ResponseEntity.ok("Registration successful");
    }

    private ResponseCookie buildResponseCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/auth")
                .sameSite("None")
                .maxAge((jwtService.extractExpiration(refreshToken).getTime() - System.currentTimeMillis()) / 1000)
                .build();
    }
}