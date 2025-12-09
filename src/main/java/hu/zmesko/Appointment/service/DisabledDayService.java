package hu.zmesko.Appointment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.zmesko.Appointment.model.DisabledDay;
import hu.zmesko.Appointment.repository.DisabledDaysRepository;

@Service
public class DisabledDayService {
    @Autowired
    private DisabledDaysRepository disabledDayRepository;


    public List<DisabledDay> getAllDisabledDays() {
        return disabledDayRepository.findAll();
    }

    public DisabledDay createDisabledDay(DisabledDay disabledDay) {
        return disabledDayRepository.save(disabledDay);
    }

    public void deleteDisabledDayById(Integer id) {
        disabledDayRepository.deleteById(id);
    }

}
