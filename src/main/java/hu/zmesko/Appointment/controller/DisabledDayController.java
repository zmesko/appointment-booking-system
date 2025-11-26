package hu.zmesko.Appointment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.zmesko.Appointment.model.DisabledDay;
import hu.zmesko.Appointment.service.DisabledDayService;

@RestController
@RequestMapping("/api/disabledday")
@CrossOrigin
public class DisabledDayController {
    @Autowired
    private DisabledDayService disabledDayService;

    @GetMapping
    public List<DisabledDay> getAllDisabledDays() {
        return disabledDayService.getAllDisabledDays();
    }

    @PostMapping
    public DisabledDay createDisabledDay(@RequestBody DisabledDay disabledDay) {
        return disabledDayService.createDisabledDay(disabledDay);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisabledDayById(@PathVariable Integer id) {

        try {
            disabledDayService.deleteDisabledDayById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
