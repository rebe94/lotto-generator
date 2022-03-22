package pl.lottogenerator.lottonumbergenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;
import pl.lottogenerator.lottonumbergenerator.dto.GenerateConfigurationDto;

import java.time.LocalDate;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Tag("WithoutSpringTest")
class LottoNumberGeneratorFacadeSpec {

    private final int port = SocketUtils.findAvailableTcpPort();
    private final String urlServiceForTest = "http://localhost:" + port + "/";

    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade =
            new LottoNumberGeneratorConfiguration().lottoNumberGeneratorFacadeForTests(urlServiceForTest);
    private WireMockServer wireMockServer;

    private final int amountOfNumbers = 6;
    private final int lowestNumber = 1;
    private final int highestNumber = 99;
    private final LocalDate DRAW_DATE = LocalDate.of(2000, 1, 1);

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().port(port));
        wireMockServer.start();
        WireMock.configureFor(port);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void module_should_draw_numbers_according_to_generate_configuration_and_return_correct_random_numbers() {
        // given
        requestToGetGenerateConfiguration();
        // when
        lottoNumberGeneratorFacade.generateWinningNumbers(DRAW_DATE);
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers(DRAW_DATE);
        // then
        assertAll(
                () -> assertNotNull(winningNumbers),
                () -> assumingThat(winningNumbers != null,
                        () -> assertThat(amountOfNumbers, equalTo(winningNumbers.size()))),
                () -> assumingThat(winningNumbers != null,
                        () -> assertThat(winningNumbers.stream().max(Integer::compareTo).get(), lessThanOrEqualTo(highestNumber))),
                () -> assumingThat(winningNumbers != null,
                        () -> assertThat(winningNumbers.stream().min(Integer::compareTo).get(), greaterThanOrEqualTo(lowestNumber)))
        );
    }

    private void requestToGetGenerateConfiguration() {
        WireMock.stubFor(WireMock.get(urlPathEqualTo("/configuration"))
                .willReturn(WireMock.aResponse()
                        .withBody("{\"amountOfNumbers\":" + amountOfNumbers + "," +
                                "\"lowestNumber\":" + lowestNumber + "," +
                                "\"highestNumber\":" + highestNumber + "}")
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")));
    }
}