package hu.zmesko.Appointment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.zmesko.Appointment.model.Appointment;
import hu.zmesko.Appointment.repository.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> findAppointmentById(int id) {
        return appointmentRepository.findById(id);
    }

    public void addAppointment(Appointment newAppointment) {
        appointmentRepository.save(newAppointment);
    }

    public void deleteAppointmentById(int id) {
        appointmentRepository.deleteById(id);
    }

    public void updateAppointmentById(int id, Appointment updatedAppointment) throws Exception {

        if (findAppointmentById(id).isPresent()) {
            updatedAppointment.setId(id);
            addAppointment(updatedAppointment);
        } else {
            throw new Exception("id not found");
        }

    }
}
