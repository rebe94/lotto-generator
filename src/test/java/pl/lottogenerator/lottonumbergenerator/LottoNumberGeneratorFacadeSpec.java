package pl.lottogenerator.lottonumbergenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lottogenerator.model.GenerateConfiguration;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LottoNumberGeneratorFacadeSpec {

    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade =
            new LottoNumberGeneratorConfiguration().lottoNumberGeneratorFacade();

    @Test
    @DisplayName("module should draw numbers and return collection with them")
    public void module_should_draw_numbers_and_return_collection_with_proper_amount_of_numbers() {
        // given
        int amountOfNumbers = 6;
        int lowestNumber = 1;
        int highestNumber = 99;
        GenerateConfiguration generateConfiguration = GenerateConfiguration.of
                (amountOfNumbers, lowestNumber, highestNumber);

        // when
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.winningNumbers(generateConfiguration);

        // then
        assertAll(
                () -> assertNotNull(winningNumbers),
                () -> assertEquals(amountOfNumbers, winningNumbers.size()),
                () -> assertThat(winningNumbers.stream().max(Integer::compareTo).get(), lessThanOrEqualTo(highestNumber)),
                () -> assertThat(winningNumbers.stream().min(Integer::compareTo).get(), greaterThanOrEqualTo(lowestNumber))
        );
    }
}