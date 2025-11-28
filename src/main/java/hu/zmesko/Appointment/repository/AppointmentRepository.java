package hu.zmesko.Appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.zmesko.Appointment.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

}
