package hu.zmesko.Appointment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.zmesko.Appointment.model.Appointment;
import hu.zmesko.Appointment.repository.AppointmentsRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentsRepository appointmentRepository;

    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> findAppointmentById(int id) {
        return appointmentRepository.findById(id);
    }

    public List<LocalDateTime> findAppointmentByYearOfMonth(int year, int month) {
        return appointmentRepository.findByYearOfMonth(year, month);
    }

    public void addAppointment(Appointment newAppointment) {
        appointmentRepository.save(newAppointment);
    }

    public void deleteAppointmentById(int id) {
        appointmentRepository.deleteById(id);
    }

    public void updateAppointmentById(Integer id, Appointment updatedAppointment) throws Exception {

        if (findAppointmentById(id).isPresent()) {
            updatedAppointment.setId(id);
            addAppointment(updatedAppointment);
        } else {
            throw new Exception("id not found");
        }

    }

}
