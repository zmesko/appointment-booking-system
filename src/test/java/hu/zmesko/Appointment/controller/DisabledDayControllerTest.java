package hu.zmesko.Appointment.controller;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import hu.zmesko.Appointment.exception.GlobalExceptionHandler;
import hu.zmesko.Appointment.exception.IdNotFoundException;
import hu.zmesko.Appointment.model.DisabledDay;
import hu.zmesko.Appointment.service.DisabledDayService;

@WebMvcTest(controllers = DisabledDayController.class, useDefaultFilters = false)
@Import({
                DisabledDayController.class,
                GlobalExceptionHandler.class
})
@AutoConfigureMockMvc(addFilters = false)
public class DisabledDayControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private DisabledDayService disabledDayService;

        private DisabledDay disabledDay = new DisabledDay(1, "John", LocalDate.of(2025, 10, 10));

        private DisabledDay disabledDay2 = new DisabledDay(2, "Filip", LocalDate.of(2025, 10, 11));

        private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        @Test
        public void Should_ReturnAllDisabledDays_When_RepositoryContainsMultiplyDisabledDay() throws Exception {

                List<DisabledDay> mockList = List.of(disabledDay, disabledDay2);

                when(disabledDayService.getAllDisabledDays()).thenReturn(mockList);

                mockMvc.perform(get("/api/disabledday"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].name").value("John"))
                                .andExpect(jsonPath("$[1].name").value("Filip"));

                verify(disabledDayService).getAllDisabledDays();
        }

        @Test
        public void Should_AddDisabledDay_When_RequestIsValid() throws Exception {

                String json = objectMapper.writeValueAsString(disabledDay);

                mockMvc.perform(post("/api/disabledday")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isOk());

                verify(disabledDayService).createDisabledDay(any(DisabledDay.class));
        }

        @Test
        public void Should_DeleteDisabledDay_When_IdIsValid() throws Exception {

                int id = 1;

                doNothing().when(disabledDayService).deleteDisabledDayById(id);

                mockMvc.perform(delete("/api/disabledday/{id}", id))
                                .andExpect(status().isOk());

                verify(disabledDayService).deleteDisabledDayById(id);
        }

        @Test
        public void Should_DeleteDisabledDay_When_IdIsNotValid() throws Exception {

                int id = 99;

                doThrow(new IdNotFoundException()).when(disabledDayService).deleteDisabledDayById(id);

                mockMvc.perform(delete("/api/disabledday/{id}", id))
                                .andExpect(status().isBadRequest());

                verify(disabledDayService).deleteDisabledDayById(id);
        }
}
