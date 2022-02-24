package pl.lottogenerator.lottonumbergenerator;

import pl.lottogenerator.lottonumbergenerator.dto.GenerateConfiguration;

import java.util.Set;
import java.util.TreeSet;

public class LottoNumberGeneratorFacade {

    public Set<Integer> generateWinningNumbers(GenerateConfiguration generateConfiguration) {
        int randomNumberBound = (generateConfiguration.getHighestNumber() - generateConfiguration.getLowestNumber()) + 1;
        Set<Integer> winningNumbers = new TreeSet<>();
        while (winningNumbers.size() < generateConfiguration.getAmountOfNumbers()) {
            int randomNumber = (int) (Math.random() * randomNumberBound) + generateConfiguration.getLowestNumber();
            winningNumbers.add(randomNumber);
        }
        return winningNumbers;
    }
}