package pl.lottogenerator.lottonumbergenerator;

import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lottogenerator.lottonumbergenerator.dto.GenerateConfigurationDto;

import static org.slf4j.LoggerFactory.getLogger;
import static pl.lottogenerator.lottonumbergenerator.GenerateConfigurationMessageProvider.failed;
import static pl.lottogenerator.lottonumbergenerator.GenerateConfigurationMessageProvider.valid;

class GenerateConfigurationClientImpl implements GenerateConfiguration {

    private static final Logger LOGGER = getLogger(GenerateConfigurationClientImpl.class.getName());

    private final RestTemplate rest;
    private final String configurationServiceUrl;

    public GenerateConfigurationClientImpl(RestTemplate rest, String configurationServiceUrl) {
        this.rest = rest;
        this.configurationServiceUrl = configurationServiceUrl;
    }

    @Getter
    private static class ReceivedGenerateConfigurationDto {
        Integer amountOfNumbers, lowestNumber, highestNumber;
    }

    public GenerateConfigurationDto getGenerateConfiguration() {
        String uri = configurationServiceUrl + "/configuration";
        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(new HttpHeaders());

        try {
            ResponseEntity<ReceivedGenerateConfigurationDto> response =
                    rest.exchange(
                            uri,
                            HttpMethod.GET,
                            httpEntity,
                            ReceivedGenerateConfigurationDto.class);
            return valid(response.getBody().getAmountOfNumbers(),
                    response.getBody().getLowestNumber(),
                    response.getBody().getHighestNumber());
        } catch (RuntimeException exception) {
            processException(exception);
        }
        return failed();
    }

    private void processException(Exception exception) {
        LOGGER.error("Http request execution failed.", exception);
    }
}
