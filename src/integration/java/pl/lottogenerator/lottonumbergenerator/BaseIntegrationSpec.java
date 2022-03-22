package pl.lottogenerator.lottonumbergenerator;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import pl.lottogenerator.AppRunner;

@SpringBootTest(
        classes = AppRunner.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "application.environment=integration"
)
@TestPropertySource(locations = "classpath:test.properties")
abstract public class BaseIntegrationSpec {
}
