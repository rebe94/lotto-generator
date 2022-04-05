package pl.lottogenerator.lottonumbergenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LottoNumberGeneratorConfiguration {

    @Value("${configuration.service.url}")
    private String configurationServiceUrl;

    @Bean
    GenerateConfiguration generateConfiguration() {
        return new GenerateConfigurationClientImpl(new RestTemplate(), configurationServiceUrl);
    }

    @Bean
    LottoNumberGeneratorFacade lottoNumberGeneratorFacade(GenerateConfiguration generateConfiguration, WinningNumbersRepository winningNumbersRepository) {
        NumberGenerator numberGenerator = new NumberGeneratorImpl(generateConfiguration, winningNumbersRepository);
        return new LottoNumberGeneratorFacade(winningNumbersRepository, numberGenerator);
    }

    @Bean
    WinningNumbersDataLoader winnerDataLoader(WinningNumbersRepository winningNumbersRepository) {
        return new WinningNumbersDataLoader(winningNumbersRepository);
    }

    public LottoNumberGeneratorFacade lottoNumberGeneratorFacadeForTests(String configurationServiceUrl){
        GenerateConfiguration generateConfiguration = new GenerateConfigurationClientImpl(new RestTemplate(), configurationServiceUrl);
        return lottoNumberGeneratorFacade(generateConfiguration, new InMemoryWinningNumbersRepository());
    }
}
