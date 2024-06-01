package site.joshua.am;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication
public class AmApplication {

	@PostConstruct
	void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(AmApplication.class, args);

		LocalDateTime now = LocalDateTime.now();
		System.out.println("현재시간" + now);
	}

}
