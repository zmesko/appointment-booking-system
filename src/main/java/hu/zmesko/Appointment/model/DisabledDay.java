package hu.zmesko.Appointment.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisabledDay {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "disabledday_seq")
    @SequenceGenerator(name = "disabledday_seq", sequenceName = "disabledday_seq", allocationSize = 1)
    private Integer id;
    private String name;
    private LocalDate disabledDay;

}
