package hu.zmesko.Appointment.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.zmesko.Appointment.model.Appointment;
import hu.zmesko.Appointment.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
@CrossOrigin
public class AppointmentContoller {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("")
    public List<Appointment> findAllAppointments() {

        return appointmentService.findAllAppointments();
    }

    @GetMapping("/{id}")
    public Optional<Appointment> findAppointmentById(@PathVariable int id) {
        return appointmentService.findAppointmentById(id);
    }

    @GetMapping("/year/{year}/month/{month}")
    public List<LocalDateTime> findAppointmentByYearOfMonth(@PathVariable int year, @PathVariable int month) {
        return appointmentService.findAppointmentByYearOfMonth(year, month);
    }

    @PostMapping("")
    public void addAppointment(@RequestBody Appointment appointment) {
        appointmentService.addAppointment(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointmentById(@PathVariable int id, @RequestBody Appointment updatedAppointment) {
        try {
            appointmentService.updateAppointmentById(id, updatedAppointment);
            return new ResponseEntity<>("Appointment updated", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Id not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentById(@PathVariable int id) {
    try {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.ok().build();
    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
}
