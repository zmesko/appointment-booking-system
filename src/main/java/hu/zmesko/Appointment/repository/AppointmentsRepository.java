package hu.zmesko.Appointment.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.zmesko.Appointment.model.Appointment;


public interface AppointmentsRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT a.bookedAppointment FROM Appointment a WHERE FUNCTION('YEAR', a.bookedAppointment) = :year AND FUNCTION('MONTH', a.bookedAppointment) = :month")
    List<LocalDateTime> findByYearOfMonth(@Param("year") int year,
                                        @Param("month") int month);
}
