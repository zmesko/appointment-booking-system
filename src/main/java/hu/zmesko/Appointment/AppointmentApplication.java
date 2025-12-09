package hu.zmesko.Appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentApplication.class, args);

	}

	/*@Bean
	CommandLineRunner initDatabase(AppointmentsRepository repository) {
		return args -> {
			Random r = new Random();
			for (int i = 0; i < 2000; i++) {
				String name = "János";
				String email = "janos@gmail.com";
				int year = 2024 + r.nextInt(2);
				int month = 1 + r.nextInt(12);
				int day = 1 + r.nextInt(28);
				int hour = 8 + r.nextInt(10);
				int[] minutes = { 0, 30 };
				int minute = minutes[r.nextInt(minutes.length)];
				LocalDateTime bookedTime = LocalDateTime.of(year, month, day, hour, minute);

				repository.save(new Appointment(null, name, email, null, bookedTime, LocalDateTime.now()));
			}

		};
	}*/

}
