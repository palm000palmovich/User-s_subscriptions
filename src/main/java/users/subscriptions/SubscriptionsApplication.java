package users.subscriptions;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition
@SpringBootApplication
public class SubscriptionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionsApplication.class, args);
	}

}
