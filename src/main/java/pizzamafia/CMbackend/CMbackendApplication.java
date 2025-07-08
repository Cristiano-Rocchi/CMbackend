package pizzamafia.CMbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class CMbackendApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(CMbackendApplication.class, args);
		ConfigurableEnvironment env = context.getEnvironment();

		String jwtSecret = env.getProperty("jwt.secret");
		System.out.println("JWT SECRET LETTO: " + jwtSecret);
	}
}
