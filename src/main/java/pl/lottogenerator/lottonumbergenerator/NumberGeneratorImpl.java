package pl.lottogenerator.lottonumbergenerator;

import pl.lottogenerator.lottonumbergenerator.dto.GenerateConfigurationDto;

import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

class NumberGeneratorImpl implements NumberGenerator {

    private final GenerateConfiguration generateConfiguration;
    private final WinningNumberRepository winningNumberRepository;

    private static final Logger logger = Logger.getLogger(NumberGeneratorImpl.class.getName());

    NumberGeneratorImpl(GenerateConfiguration generateConfiguration, WinningNumberRepository winningNumberRepository) {
        this.generateConfiguration = generateConfiguration;
        this.winningNumberRepository = winningNumberRepository;
    }

    public void generateWinningNumbers() {
        GenerateConfigurationDto generateConfigurationDto = generateConfiguration.getGenerateConfiguration();
        Integer amountOfNumbers = generateConfigurationDto.getAmountOfNumbers();
        Integer lowestNumber = generateConfigurationDto.getLowestNumber();
        Integer highestNumber = generateConfigurationDto.getHighestNumber();
        int randomNumberBound = (highestNumber - lowestNumber) + 1;

        Set<Integer> generatedNumbers = new TreeSet<>();
        while (generatedNumbers.size() < amountOfNumbers) {
            int randomNumber = (int) (Math.random() * randomNumberBound) + lowestNumber;
            generatedNumbers.add(randomNumber);
        }
        winningNumberRepository.save(generatedNumbers);
        logger.info("Internal request to generate numbers. New winning numbers: " + generatedNumbers);
    }
}
