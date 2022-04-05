package pl.lottogenerator.lottonumbergenerator;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;

public class LottoNumberGeneratorFacade {

    private final WinningNumbersRepository winningNumbersRepository;
    private final NumberGenerator numberGenerator;

    LottoNumberGeneratorFacade(WinningNumbersRepository winningNumbersRepository, NumberGenerator numberGenerator) {
        this.winningNumbersRepository = winningNumbersRepository;
        this.numberGenerator = numberGenerator;
    }

    public Set<Integer> getWinningNumbers(LocalDate drawDate) {
        Optional<WinningNumbers> winningNumbers = winningNumbersRepository.findByDrawDate(drawDate);
        if (winningNumbers.isEmpty()) {
            return Collections.emptySet();
        }
        return winningNumbers.get().getNumbers();
    }

    @Scheduled(cron = "0 0 19 * * *", zone = "Europe/Warsaw")
    private void generateWinningNumbersAsScheduled() {
        LocalDate drawDateScheduled = LocalDate.now(TimeZone.getTimeZone("Europe/Warsaw").toZoneId());
        generateWinningNumbers(drawDateScheduled);
    }

    public void generateWinningNumbers(LocalDate drawDate) {
        numberGenerator.generateWinningNumbers(drawDate);
    }
}