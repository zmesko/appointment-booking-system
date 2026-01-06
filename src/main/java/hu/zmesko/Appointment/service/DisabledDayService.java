package hu.zmesko.Appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hu.zmesko.Appointment.exception.IdNotFound;
import hu.zmesko.Appointment.model.DisabledDay;
import hu.zmesko.Appointment.repository.DisabledDaysRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DisabledDayService {
    
    private final DisabledDaysRepository disabledDayRepository;


    public List<DisabledDay> getAllDisabledDays() {
        return disabledDayRepository.findAll();
    }

    public DisabledDay createDisabledDay(DisabledDay disabledDay) {
        return disabledDayRepository.save(disabledDay);
    }

    public void deleteDisabledDayById(int id) {
        if (disabledDayRepository.findById(id).isPresent()) {
            disabledDayRepository.deleteById(id);
        } else {
            throw new IdNotFound();
        }
        
    }

}
