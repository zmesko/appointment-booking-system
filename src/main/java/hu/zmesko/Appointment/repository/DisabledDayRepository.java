package hu.zmesko.Appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.zmesko.Appointment.model.DisabledDay;

public interface DisabledDayRepository extends JpaRepository<DisabledDay, Integer> {

}
