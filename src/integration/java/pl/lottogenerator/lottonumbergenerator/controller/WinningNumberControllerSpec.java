package pl.lottogenerator.lottonumbergenerator.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.SocketUtils;
import pl.lottogenerator.lottonumbergenerator.BaseIntegrationSpec;
import pl.lottogenerator.lottonumbergenerator.LottoNumberGeneratorConfiguration;
import pl.lottogenerator.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lottogenerator.lottonumbergenerator.WinningNumberRepository;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("SpringTest")
@SpringBootTest
@AutoConfigureMockMvc
class WinningNumberControllerSpec extends BaseIntegrationSpec {

    private static final int port = SocketUtils.findAvailableTcpPort();
    private static final String configurationServiceUrlForTest = "http://localhost:" + port + "/";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;
    @Autowired
    private WinningNumberRepository winningNumberRepository;
    private WireMockServer wireMockServer;

    private final int year = 2000;
    private final int month = 1;
    private final int day = 1;
    private final LocalDate DRAW_DATE = LocalDate.of(year, month, day);
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
        winningNumberRepository.deleteAll();
        wireMockServer.stop();
    }

    @Test
    public void returns_winning_numbers_with_correct_configuration_when_receives_request_of_getting_winning_numbers() throws Exception {
        // given
        requestToGetGenerateConfiguration();
        lottoNumberGeneratorFacade.generateWinningNumbers(DRAW_DATE);

        // when
        // then
        mockMvc.perform(get("/winningnumbers")
                        .param("year", Integer.toString(year))
                        .param("month", Integer.toString(month))
                        .param("day", Integer.toString(day)))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.winningNumbers.size()", is(amountOfNumbers)))
                .andExpect(jsonPath("$.winningNumbers.min()", greaterThanOrEqualTo((double) lowestNumber)))
                .andExpect(jsonPath("$.winningNumbers.max()", lessThanOrEqualTo((double) highestNumber)));

    }

    @Test
    public void returns_empty_set_of_winning_numbers_when_receives_request_of_getting_winning_numbers_for_draw_day_which_has_not_took_place_yet() throws Exception {
        // given
        requestToGetGenerateConfiguration();
        // when
        // then
        mockMvc.perform(get("/winningnumbers")
                        .param("year", Integer.toString(year))
                        .param("month", Integer.toString(month))
                        .param("day", Integer.toString(day)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.winningNumbers.size()", is(0)));
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