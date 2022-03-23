package pl.lottogenerator.lottonumbergenerator.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lottogenerator.lottonumbergenerator.LottoNumberGeneratorFacade;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("SpringTest")
@SpringBootTest
@AutoConfigureMockMvc
class WinningNumberControllerSpec {

    @MockBean
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;
    @Autowired
    private MockMvc mockMvc;

    private final int year = 2000;
    private final int month = 1;
    private final int day = 1;
    private final LocalDate DRAW_TOOK_PLACE_DATE = LocalDate.of(year, month, day);
    private final LocalDate BEFORE_DRAW_DATE = LocalDate.of(year, month, day);


    @Test
    public void returns_winning_numbers_with_correct_configuration_when_gives_request_of_draw_day_which_has_took_place_already() throws Exception {
        // given
        given(lottoNumberGeneratorFacade.getWinningNumbers(DRAW_TOOK_PLACE_DATE)).willReturn(Set.of(1,2,3,4,5,6));
        final int amountOfNumbers = 6;
        final int lowestNumber = 1;
        final int highestNumber = 99;
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
    public void returns_empty_set_of_winning_numbers_when_gives_request_of_draw_day_which_has_not_took_place_yet() throws Exception {
        // given
        given(lottoNumberGeneratorFacade.getWinningNumbers(BEFORE_DRAW_DATE)).willReturn(Collections.emptySet());
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
}