package hu.zmesko.Appointment.controller;

import java.util.List;

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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/disabledday")
@CrossOrigin
@RequiredArgsConstructor
public class DisabledDayController {

    private final DisabledDayService disabledDayService;

    @GetMapping
    public List<DisabledDay> getAllDisabledDays() {
        return disabledDayService.getAllDisabledDays();
    }

    @PostMapping
    public DisabledDay createDisabledDay(@RequestBody DisabledDay disabledDay) {
        return disabledDayService.createDisabledDay(disabledDay);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDisabledDayById(@PathVariable Integer id) {
        disabledDayService.deleteDisabledDayById(id);
        return ResponseEntity.ok("Delete successful");
    }
}
