package hu.zmesko.Appointment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.zmesko.Appointment.model.Appointment;
import hu.zmesko.Appointment.repository.AppointmentsRepository;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    private Appointment appointment = new Appointment(1,
            "Filip",
            "filip@gmail.com",
            "",
            LocalDateTime.of(2025, 10, 12, 12, 00),
            LocalDateTime.now());

    Appointment appointment2 = new Appointment(2,
            "John",
            "filip@gmail.com",
            "",
            LocalDateTime.of(2025, 10, 10, 10, 30),
            LocalDateTime.now());

    @Mock
    AppointmentsRepository appointmentRepository;

    @InjectMocks
    AppointmentService appointmentService;

    @Test
    public void Should_ReturnAllAppointments_When_RepositoryContainsMultiplyAppointments() {

        List<Appointment> mockList = List.of(appointment, appointment2);

        when(appointmentRepository.findAll()).thenReturn(mockList);

        List<Appointment> result = appointmentService.findAllAppointments();

        assertEquals(2, result.size());
        assertEquals("Filip", result.get(0).getName());
        assertEquals("John", result.get(1).getName());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    public void Should_ReturnAppointment_When_IdIsValid() {
        int id = 1;

        when(appointmentService.findAppointmentById(id)).thenReturn(Optional.of(appointment));

        Optional<Appointment> result = appointmentService.findAppointmentById(id);

        assertTrue(result.isPresent());
        assertEquals("Filip", result.get().getName());
        assertEquals("filip@gmail.com", result.get().getEmail());
        verify(appointmentRepository).findById(id);
    }

    @Test
    public void Should_ReturnNull_When_IdNotFound() {
        int id = 999;

        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Appointment> result = appointmentService.findAppointmentById(id);

        assertFalse(result.isPresent());
    }

    @Test
    public void Should_AddAppointment_When_RequestIsValid() {

        appointmentService.addAppointment(appointment);

        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    public void Should_DeleteAppointment_When_IdIsValid() {
        int idToDelete = 1;

        appointmentService.deleteAppointmentById(idToDelete);

        verify(appointmentRepository, times(1)).deleteById(idToDelete);

    }

    @Test
    public void Should_UpdateAppointment_When_IdIsValid() throws Exception {

        Appointment updatedAppointment = new Appointment(1,
                "John",
                "filip@gmail.com",
                "",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now());

        int id = 1;

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));

        appointmentService.updateAppointmentById(id, updatedAppointment);

        ArgumentCaptor<Appointment> captor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(captor.capture());

        Appointment saved = captor.getValue();

        assertEquals(id, saved.getId());
        assertEquals("John", saved.getName());

    }

    @Test
    public void Should_ThrowException_When_UpdatingNonExistingAppointment() throws Exception {

        Appointment updatedAppointment = new Appointment(2,
                "John",
                "filip@gmail.com",
                "",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now());

        int id = 1;

        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class,
                () -> appointmentService.updateAppointmentById(id, updatedAppointment));

        assertEquals("id not found", exception.getMessage());

        verify(appointmentRepository, never()).save(any());

    }

    @Test
    public void Should_ReturnAppointmentsLocalDate_When_GivenYearAndMonth() {
        int year = 2025;
        int month = 10;

        when(appointmentRepository.findByYearOfMonth(year, month))
                .thenReturn(List.of(appointment.getBookedAppointment(), appointment2.getBookedAppointment()));

        List<LocalDateTime> results = appointmentService.findAppointmentByYearOfMonth(year, month);

        assertTrue(!results.isEmpty());
        assertEquals(2025, results.get(0).getYear());
        assertEquals(10, results.get(0).getMonthValue());
        assertEquals(2025, results.get(1).getYear());
        assertEquals(10, results.get(1).getMonthValue());
        verify(appointmentRepository).findByYearOfMonth(year, month);
    }

    @Test
    public void Should_ReturnEmptyList_When_GivenYearAndMonthNotValid() {
        int year = 3000;
        int month = 14;

        when(appointmentRepository.findByYearOfMonth(year, month)).thenReturn(List.of());

        List<LocalDateTime> results = appointmentService.findAppointmentByYearOfMonth(year, month);

        assertTrue(results.isEmpty());
        verify(appointmentRepository).findByYearOfMonth(year, month);
    }
}
