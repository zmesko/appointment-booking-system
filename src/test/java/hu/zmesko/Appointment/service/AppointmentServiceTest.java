package hu.zmesko.Appointment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.zmesko.Appointment.model.Appointment;
import hu.zmesko.Appointment.repository.AppointmentRepository;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    private Appointment appointment = new Appointment(1,
            "Filip",
            "filip@gmail.com",
            "",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now());

    @Mock
    AppointmentRepository appointmentRepository;

    @InjectMocks
    AppointmentService appointmentService;

    @Test
    public void testFindAllAppointments() {

        Appointment appointment2 = new Appointment(2,
                "John",
                "filip@gmail.com",
                "",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now());

        List<Appointment> mockList = List.of(appointment, appointment2);

        when(appointmentRepository.findAll()).thenReturn(mockList);

        List<Appointment> result = appointmentService.findAllAppointments();

        assertEquals(2, result.size());
        assertEquals("Filip", result.get(0).getName());
        assertEquals("John", result.get(1).getName());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    public void testFindAppointmentById() {
        int id = 1;

        when(appointmentService.findAppointmentById(id)).thenReturn(Optional.of(appointment));

        Optional<Appointment> result = appointmentService.findAppointmentById(id);

        assertTrue(result.isPresent());
        assertEquals("Filip", result.get().getName());
        assertEquals("filip@gmail.com", result.get().getEmail());
        verify(appointmentRepository).findById(id);
    }

    @Test
    public void testFindAppointmentByIdNotFound() {
        int id = 999;

        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Appointment> result = appointmentService.findAppointmentById(id);

        assertFalse(result.isPresent());
    }

    @Test
    public void testAddAppointment() {

        appointmentService.addAppointment(appointment);

        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    public void testDeleteAppointmentById() {
        int idToDelete = 1;

        appointmentService.deleteAppointmentById(idToDelete);

        verify(appointmentRepository, times(1)).deleteById(idToDelete);

    }

    @Test
    public void testUpdateAppointmentById() throws Exception{

        Appointment updatedAppointment = new Appointment(1,
            "John",
            "filip@gmail.com",
            "",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now());
        int id = 1;

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));

        appointmentService.updateAppointmentById(id, updatedAppointment);

        assertEquals(id, updatedAppointment.getId());
        verify(appointmentRepository).save(updatedAppointment);
        
    }
}
