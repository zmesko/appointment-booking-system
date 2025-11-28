package hu.zmesko.Appointment.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import hu.zmesko.Appointment.model.Appointment;
import hu.zmesko.Appointment.service.AppointmentService;

@WebMvcTest(AppointmentContoller.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AppointmentControllerTest {

        private Appointment appointment = new Appointment(1, 
                                                        "Filip", 
                                                        "filip@gmail.com", 
                                                        "",
                                                        LocalDateTime.now().plusDays(1), 
                                                        LocalDateTime.now());

        private Appointment appointment2 = new Appointment(2, 
                                                        "John", 
                                                        "john@gmail.com", 
                                                        "",
                                                        LocalDateTime.now().plusDays(1), 
                                                        LocalDateTime.now());

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private AppointmentService appointmentService;

        private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        @Test
        public void testFindAll() throws Exception {
                List<Appointment> mockList = List.of(appointment, appointment2);

                when(appointmentService.findAllAppointments()).thenReturn(mockList);

                mockMvc.perform(get("/api/appointment"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].name").value("Filip"))
                                .andExpect(jsonPath("$[1].name").value("John"));

                verify(appointmentService).findAllAppointments();
        }

        @Test
        public void testFindById() throws Exception {
                int id = 1;

                when(appointmentService.findAppointmentById(id)).thenReturn(Optional.of(appointment));

                mockMvc.perform(get("/api/appointment/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("Filip"));

                verify(appointmentService).findAppointmentById(id);
        }

        @Test
        public void testFindById_NotFound() throws Exception {
                int id = 99;

                when(appointmentService.findAppointmentById(id)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/appointment/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(content().string("null"));

                verify(appointmentService).findAppointmentById(id);
        }


        @Test
        public void testAddAppointment() throws Exception {

                String json = objectMapper.writeValueAsString(appointment);

                mockMvc.perform(post("/api/appointment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isOk());

                verify(appointmentService).addAppointment(any(Appointment.class));
        }

        @Test
        public void testUpdateById() throws Exception {

                int id = 1;

                Appointment updatedAppointment = new Appointment(1, 
                                                                "Updated Filip", 
                                                                "filip@gmail.com", 
                                                                "",
                                                                LocalDateTime.now().plusDays(1), 
                                                                LocalDateTime.now());

                String json = objectMapper.writeValueAsString(updatedAppointment);

                doNothing().when(appointmentService).updateAppointmentById(id, updatedAppointment);

                mockMvc.perform(put("/api/appointment/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Appointment updated"));

                verify(appointmentService).updateAppointmentById(eq(id), any(Appointment.class));

        }

        @Test
        public void testUpdateById_NotFound() throws Exception {

                int id = 999;

                Appointment updatedAppointment = new Appointment(1, 
                                                                "Missed Filip", 
                                                                "filip@gmail.com", 
                                                                "",
                                                                LocalDateTime.now().plusDays(1), 
                                                                LocalDateTime.now());

                String json = objectMapper.writeValueAsString(updatedAppointment);

                doThrow(new RuntimeException("Id not found!")).when(appointmentService).updateAppointmentById(eq(id), any(Appointment.class));

                mockMvc.perform(put("/api/appointment/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Id not found!"));

                verify(appointmentService).updateAppointmentById(eq(id), any(Appointment.class));

        }

        @Test
        public void testDeleteById() throws Exception {
                int id = 1;

                doNothing().when(appointmentService).deleteAppointmentById(id);

                mockMvc.perform(delete("/api/appointment/{id}", id))
                        .andExpect(status().isOk());

                verify(appointmentService).deleteAppointmentById(id);
        }

         @Test
        public void testDeleteById_NotFound() throws Exception {
                int id = 99;

                doThrow(new RuntimeException("Id not found!")).when(appointmentService).deleteAppointmentById(id);

                mockMvc.perform(delete("/api/appointment/{id}", id))
                        .andExpect(status().isBadRequest());

                verify(appointmentService).deleteAppointmentById(id);
        }
}
