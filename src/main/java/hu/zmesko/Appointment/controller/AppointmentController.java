package hu.zmesko.Appointment.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointment")
@CrossOrigin
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

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

    @PostMapping("/booking")
    public void addAppointment(@RequestBody Appointment appointment) {
        appointmentService.addAppointment(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointmentById(@PathVariable int id,
            @RequestBody Appointment updatedAppointment) {
        appointmentService.updateAppointmentById(id, updatedAppointment);
        return new ResponseEntity<>("Appointment updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointmentById(@PathVariable int id) {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.ok("Delete successful");
    }
}
