package pl.lottogenerator.lottonumbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
class LottoNumberGeneratorConfiguration {

    @Bean
    WinningNumberRepository winningNumberRepository() {
        return new InMemoryWinningNumberRepository();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    GenerateConfiguration generateConfiguration(RestTemplate restTemplate) {
        return new GenerateConfigurationProxyImpl(restTemplate);
    }

    @Bean
    LottoNumberGeneratorFacade lottoNumberGeneratorFacade(GenerateConfiguration generateConfiguration, WinningNumberRepository winningNumberRepository) {
        NumberGenerator numberGenerator = new NumberGeneratorImpl(generateConfiguration, winningNumberRepository);
        return new LottoNumberGeneratorFacade(winningNumberRepository, numberGenerator);
    }

    LottoNumberGeneratorFacade lottoNumberGeneratorFacadeForTests(GenerateConfiguration generateConfiguration){
        return lottoNumberGeneratorFacade(generateConfiguration, new InMemoryWinningNumberRepository());
    }
}
