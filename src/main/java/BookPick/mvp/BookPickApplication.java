package BookPick.mvp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // ← 이거 있어?

public class BookPickApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookPickApplication.class, args);
	}

}
