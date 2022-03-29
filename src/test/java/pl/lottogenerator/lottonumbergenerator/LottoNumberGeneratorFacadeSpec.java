package pl.lottogenerator.lottonumbergenerator;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.util.SocketUtils;

import java.time.LocalDate;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Tag("WithoutSpringTest")
class LottoNumberGeneratorFacadeSpec {

    private final int port = SocketUtils.findAvailableTcpPort();
    private final String urlServiceForTest = "http://localhost:" + port + "/";

    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade =
            new LottoNumberGeneratorConfiguration().lottoNumberGeneratorFacadeForTests(urlServiceForTest);
    private WireMockServer wireMockServer;

    private final int year = 2000;
    private final int month = 1;
    private final int day = 1;
    private final LocalDate DRAW_DATE = LocalDate.of(year, month, day);
    private final LocalDate BEFORE_DRAW_DATE = LocalDate.of(year, month, day);
    private final int amountOfNumbers = 6;
    private final int lowestNumber = 1;
    private final int highestNumber = 99;

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
    public void returns_winning_numbers_with_correct_configuration_when_gives_draw_day_which_has_took_place_already() {
        // given
        requestToGetGenerateConfiguration();
        lottoNumberGeneratorFacade.generateWinningNumbers(DRAW_DATE);
        // when
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers(DRAW_DATE);
        // then
        assertAll(
                () -> assumingThat(winningNumbers != null,
                        () -> assertThat(winningNumbers.size(), equalTo(amountOfNumbers))),
                () -> assumingThat(winningNumbers != null,
                        () -> assertThat(winningNumbers.stream().max(Integer::compareTo).get(), lessThanOrEqualTo(highestNumber))),
                () -> assumingThat(winningNumbers != null,
                        () -> assertThat(winningNumbers.stream().min(Integer::compareTo).get(), greaterThanOrEqualTo(lowestNumber)))
        );
    }

    @Test
    public void returns_empty_set_of_winning_numbers_when_gives_draw_day_which_has_not_took_place_yet() throws Exception {
        // given
        requestToGetGenerateConfiguration();
        // when
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers(BEFORE_DRAW_DATE);
        // then
        assumingThat(winningNumbers != null, () -> assertThat(winningNumbers.size(), equalTo(0)));
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