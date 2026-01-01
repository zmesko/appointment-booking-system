package hu.zmesko.Appointment.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
@NoArgsConstructor
public class ConfigurationService {

    @Value("${jwt.secret}")
    private String jwtSecret;
}
