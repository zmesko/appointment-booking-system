package hu.zmesko.Appointment.model;


import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id 
    private String username;
    private String password;
    private boolean enabled;

    @OneToMany(mappedBy = "user")
    private Set<Authority> authorities;

}
