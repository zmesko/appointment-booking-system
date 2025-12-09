package hu.zmesko.Appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.zmesko.Appointment.model.DisabledDay;

public interface DisabledDaysRepository extends JpaRepository<DisabledDay, Integer> {

}
