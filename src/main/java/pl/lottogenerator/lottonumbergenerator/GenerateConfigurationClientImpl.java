package pl.lottogenerator.lottonumbergenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lottogenerator.lottonumbergenerator.dto.GenerateConfigurationDto;

import java.util.UUID;

class GenerateConfigurationClientImpl implements GenerateConfiguration {

    private final RestTemplate rest;
    @Value("${name.configuration.service.url}")
    private final String configurationServiceUrl;

    public GenerateConfigurationClientImpl(RestTemplate rest, String configurationServiceUrl) {
        this.rest = rest;
        this.configurationServiceUrl = configurationServiceUrl;
    }

    public GenerateConfigurationDto getGenerateConfiguration() {
        String uri = configurationServiceUrl + "/configuration";
        HttpHeaders headers = new HttpHeaders();
        headers.add("requestId", UUID.randomUUID().toString());
        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<GenerateConfigurationDto> response =
                rest.exchange(
                        uri,
                        HttpMethod.GET,
                        httpEntity,
                        GenerateConfigurationDto.class);
        return response.getBody();
    }
}
