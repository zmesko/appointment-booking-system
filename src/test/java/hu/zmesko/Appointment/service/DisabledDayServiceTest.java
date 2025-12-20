package hu.zmesko.Appointment.service;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.zmesko.Appointment.model.DisabledDay;
import hu.zmesko.Appointment.repository.DisabledDaysRepository;

@ExtendWith(MockitoExtension.class)
public class DisabledDayServiceTest {

    DisabledDay disabledDay = new DisabledDay(1, "", LocalDate.of(2025, 11, 05));
    DisabledDay disabledDay2 = new DisabledDay(2, "", LocalDate.of(2025, 11, 06));

    @Mock
    private DisabledDaysRepository disabledDayRepository;

    @InjectMocks
    private DisabledDayService disabledDayService;

    @Test
    public void Should_ReturnAllDisabledDays_When_RepositoryContainsMultiplyDisabledDay() {

        List<DisabledDay> mockList = List.of(disabledDay, disabledDay2);
        when(disabledDayRepository.findAll()).thenReturn(mockList);

        List<DisabledDay> result = disabledDayService.getAllDisabledDays();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        verify(disabledDayRepository, times(1)).findAll();
    }

    @Test
    public void Should_AddDisabledDay_When_RequestIsValid() {
        disabledDayService.createDisabledDay(disabledDay);

        verify(disabledDayRepository, times(1)).save(disabledDay);
    }

    @Test
    public void Should_DeleteDisabledDay_When_IdIsValid(){
        int idToDelete = 1;

        disabledDayService.deleteDisabledDayById(idToDelete);

        verify(disabledDayRepository, times(1)).deleteById(idToDelete);;
    }
}
