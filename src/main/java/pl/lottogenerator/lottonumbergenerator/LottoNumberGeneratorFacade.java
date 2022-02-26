package pl.lottogenerator.lottonumbergenerator;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

public class LottoNumberGeneratorFacade {

    private final WinningNumberRepository winningNumberRepository;
    private final NumberGenerator numberGenerator;

    LottoNumberGeneratorFacade(WinningNumberRepository winningNumberRepository, NumberGenerator numberGenerator) {
        this.winningNumberRepository = winningNumberRepository;
        this.numberGenerator = numberGenerator;
    }

    public Set<Integer> getWinningNumbers() {
        return winningNumberRepository.getNumbers();
    }

     @Scheduled(cron = "*/30 * * * * *")
     //@Scheduled(cron = "0 0 19 * * *")
     void generateWinningNumbers() {
        numberGenerator.generateWinningNumbers();
    }
}