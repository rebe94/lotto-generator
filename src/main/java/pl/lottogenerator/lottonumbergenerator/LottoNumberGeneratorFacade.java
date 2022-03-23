package pl.lottogenerator.lottonumbergenerator;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class LottoNumberGeneratorFacade {

    private final WinningNumbersRepository winningNumbersRepository;
    private final NumberGenerator numberGenerator;

    LottoNumberGeneratorFacade(WinningNumbersRepository winningNumbersRepository, NumberGenerator numberGenerator) {
        this.winningNumbersRepository = winningNumbersRepository;
        this.numberGenerator = numberGenerator;
    }

    public Set<Integer> getWinningNumbers(LocalDate drawingDate) {
        Optional<WinningNumbers> winningNumbers = winningNumbersRepository.findByDrawingDate(drawingDate);
        if (winningNumbers.isEmpty()) {
            return Collections.emptySet();
        }
        return winningNumbers.get().getNumbers();
    }

    @Scheduled(cron = "0 0 19 * * *")
    private void generateWinningNumbersScheduled() {
        LocalDate drawingDateScheduled = LocalDate.now();
        generateWinningNumbers(drawingDateScheduled);
    }

    public void generateWinningNumbers(LocalDate drawingDate) {
        numberGenerator.generateWinningNumbers(drawingDate);
    }
}