package hu.zmesko.Appointment.repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import hu.zmesko.Appointment.model.Appointment;

@DataJpaTest
public class AppointmentsRepositoryTest {

        @Autowired
        private AppointmentsRepository appointmentsRepository;

        @Test
        public void Should_ReturnAppointmentsLocalDateTime_When_GivenYearAndMonth() {

                Appointment appointmentJan = Appointment.builder()
                                .name("Sándor")
                                .email("sandor@gmail.com")
                                .bookedAppointment(LocalDateTime.of(2025, 01, 10, 10, 30))
                                .build();
                appointmentsRepository.save(appointmentJan);

                Appointment appointmentFeb = Appointment.builder()
                                .name("Kálmán")
                                .email("kalman@gmail.com")
                                .bookedAppointment(LocalDateTime.of(2025, 02, 8, 8, 30))
                                .build();
                appointmentsRepository.save(appointmentFeb);

                Appointment appointmentJanOther = Appointment.builder()
                                .name("Sándor")
                                .email("sandor@gmail.com")
                                .bookedAppointment(LocalDateTime.of(2025, 01, 15, 12, 30))
                                .build();
                appointmentsRepository.save(appointmentJanOther);

                List<LocalDateTime> results = appointmentsRepository.findByYearOfMonth(2025, 1);

                assertEquals(2, results.size());
                assertTrue(results.contains(appointmentJan.getBookedAppointment()));
                assertTrue(results.contains(appointmentJanOther.getBookedAppointment()));
        }
}
