package pl.lottogenerator.lottonumbergenerator;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.util.SocketUtils;

import java.time.LocalDate;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Tag("SpringTest")
@SpringBootTest
class GetsWinningNumbersBeforeDrawSpecIntegration extends BaseSpecIntegration {

    private static final int port = SocketUtils.findAvailableTcpPort();
    private static final String configurationServiceUrlForTest = "http://localhost:" + port + "/";

    @Autowired
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;
    @Autowired
    private WinningNumbersRepository winningNumbersRepository;
    private WireMockServer wireMockServer;

    private final int year = 2000;
    private final int month = 1;
    private final int day = 1;
    private final LocalDate BEFORE_DRAW_DATE = LocalDate.of(year, month, day);
    private final int amountOfNumbers = 6;
    private final int lowestNumber = 1;
    private final int highestNumber = 99;

    @TestConfiguration
    public static class IntegrationTestConfiguration {

        @Bean
        LottoNumberGeneratorFacade lottoNumberGeneratorFacade() {
            return new LottoNumberGeneratorConfiguration()
                    .lottoNumberGeneratorFacadeForTests(configurationServiceUrlForTest);
        }
    }

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().port(port));
        wireMockServer.start();
        WireMock.configureFor(port);
    }

    @AfterEach
    void tearDown() {
        winningNumbersRepository.deleteAll();
        wireMockServer.stop();
    }

    @Test
    public void returns_empty_set_of_winning_numbers_when_gives_draw_day_which_has_not_took_place_yet() throws Exception {
        // given
        requestToGetGenerateConfiguration();
        // when
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers(BEFORE_DRAW_DATE);
        // then
        assumingThat(winningNumbers != null,
                () -> assertThat(winningNumbers.size(), equalTo(0)));
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