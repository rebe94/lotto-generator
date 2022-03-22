package pl.lottogenerator.lottonumbergenerator;

import pl.lottogenerator.lottonumbergenerator.dto.GenerateConfigurationDto;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class NumberGeneratorImpl implements NumberGenerator {

    private final GenerateConfiguration generateConfiguration;
    private final WinningNumberRepository winningNumberRepository;

    private static final Logger LOGGER = getLogger(NumberGeneratorImpl.class.getName());

    NumberGeneratorImpl(GenerateConfiguration generateConfiguration, WinningNumberRepository winningNumberRepository) {
        this.generateConfiguration = generateConfiguration;
        this.winningNumberRepository = winningNumberRepository;
    }

    public void generateWinningNumbers(LocalDate drawingDate) {
        if (winningNumberRepository.findByDrawingDate(drawingDate).isPresent()) {
            LOGGER.warn("Failed attempt of generating winning numbers. Winning numbers for " + drawingDate + " already exist.");
            return;
        }

        Set<Integer> generatedNumbers = generateNumbers(generateConfiguration.getGenerateConfiguration());
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .numbers(generatedNumbers)
                .drawingDate(drawingDate)
                .build();
        winningNumberRepository.insert(winningNumbers);
        LOGGER.info("Numbers are generated for " + drawingDate + " as scheduled. The winning numbers: " + generatedNumbers);
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
