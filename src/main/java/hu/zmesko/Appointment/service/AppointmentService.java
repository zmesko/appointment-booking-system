package hu.zmesko.Appointment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.zmesko.Appointment.exception.IdNotFoundException;
import hu.zmesko.Appointment.model.Appointment;
import hu.zmesko.Appointment.repository.AppointmentsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentsRepository appointmentRepository;

    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> findAppointmentById(int id) {
        return appointmentRepository.findById(id);
    }

    public List<LocalDateTime> findAppointmentByYearOfMonth(int year, int month) {
        return appointmentRepository.findByYearOfMonth(year, month);
    }

    @SuppressWarnings("null")
    public void addAppointment(Appointment newAppointment) {
        appointmentRepository.save(newAppointment);
    }

    public void deleteAppointmentById(int id) {
        if (findAppointmentById(id).isPresent()) {
            appointmentRepository.deleteById(id);
        } else {
            throw new IdNotFoundException();
        }
    }

    public void updateAppointmentById(Integer id, Appointment updatedAppointment) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException());
                
        updatedAppointment.setId(id);
        appointmentRepository.save(updatedAppointment);
    }

}
