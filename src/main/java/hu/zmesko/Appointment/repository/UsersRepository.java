package hu.zmesko.Appointment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.zmesko.Appointment.model.AppUser;



public interface UsersRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
