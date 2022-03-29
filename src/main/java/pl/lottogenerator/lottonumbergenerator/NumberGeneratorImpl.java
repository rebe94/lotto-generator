package pl.lottogenerator.lottonumbergenerator;

import pl.lottogenerator.lottonumbergenerator.dto.GenerateConfigurationDto;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class NumberGeneratorImpl implements NumberGenerator {

    private final GenerateConfiguration generateConfiguration;
    private final WinningNumbersRepository winningNumbersRepository;

    private static final Logger LOGGER = getLogger(NumberGeneratorImpl.class.getName());

    NumberGeneratorImpl(GenerateConfiguration generateConfiguration, WinningNumbersRepository winningNumbersRepository) {
        this.generateConfiguration = generateConfiguration;
        this.winningNumbersRepository = winningNumbersRepository;
    }

    public void generateWinningNumbers(LocalDate drawDate) {
        if (winningNumbersRepository.findByDrawDate(drawDate).isPresent()) {
            LOGGER.warn("Failed attempt of generating winning numbers. Winning numbers for " + drawDate + " already exist.");
            return;
        }
        GenerateConfigurationDto generateConfigurationDto = generateConfiguration.getGenerateConfiguration();
        if (generateConfigurationDto.getValidationMessage().equals(GenerateConfigurationDto.ValidationMessage.FAILED)) {
            LOGGER.error("Couldn't generate winning numbers as scheduled for " + drawDate);
            return;
        }

        Set<Integer> generatedNumbers = generateNumbers(generateConfigurationDto);
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .numbers(generatedNumbers)
                .drawDate(drawDate)
                .build();
        winningNumbersRepository.insert(winningNumbers);
        LOGGER.info("Numbers are generated for " + drawDate + " as scheduled. The winning numbers: " + generatedNumbers);
    }

    private Set<Integer> generateNumbers(GenerateConfigurationDto generateConfigurationDto) {
        Integer amountOfNumbers = generateConfigurationDto.getAmountOfNumbers();
        Integer lowestNumber = generateConfigurationDto.getLowestNumber();
        Integer highestNumber = generateConfigurationDto.getHighestNumber();
        int randomNumberBound = (highestNumber - lowestNumber) + 1;

        Set<Integer> generatedNumbers = new TreeSet<>();
        while (generatedNumbers.size() < amountOfNumbers) {
            int randomNumber = (int) (Math.random() * randomNumberBound) + lowestNumber;
            generatedNumbers.add(randomNumber);
        }
        return generatedNumbers;
    }
}
