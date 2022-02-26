package pl.lottogenerator.lottonumbergenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.lottogenerator.lottonumbergenerator.dto.GenerateConfigurationDto;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
class LottoNumberGeneratorFacadeIntegrationSpec {

    @MockBean
    private GenerateConfiguration generateConfiguration;
    @Autowired
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    private final int amountOfNumbers = 6;
    private final int lowestNumber = 1;
    private final int highestNumber = 99;
    private final GenerateConfigurationDto generateConfigurationDto = new GenerateConfigurationDto
            (amountOfNumbers, lowestNumber, highestNumber);

    @Test
    @DisplayName("module should draw numbers according to generate configuration and return correct random numbers")
    public void module_should_draw_numbers_according_to_generate_configuration_and_return_correct_random_numbers() {
        // given
        given(generateConfiguration.getGenerateConfiguration()).willReturn(generateConfigurationDto);
        // when
        lottoNumberGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers();

        // then
        assertAll(
                () -> assertNotNull(winningNumbers),
                () -> assertEquals(amountOfNumbers, winningNumbers.size()),
                () -> assertThat(winningNumbers.stream().max(Integer::compareTo).get(), lessThanOrEqualTo(highestNumber)),
                () -> assertThat(winningNumbers.stream().min(Integer::compareTo).get(), greaterThanOrEqualTo(lowestNumber))
        );
    }
}