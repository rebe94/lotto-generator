package pl.lottogenerator.lottonumbergenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

class WinningNumbersDataLoader implements ApplicationRunner {

    private final WinningNumbersRepository winningNumbersRepository;

    @Autowired
    public WinningNumbersDataLoader(WinningNumbersRepository winningNumbersRepository) {
        this.winningNumbersRepository = winningNumbersRepository;
    }

    public void run(ApplicationArguments args) {
        WinningNumbers winningNumbers1 = new WinningNumbers(Set.of(1, 2, 3, 4, 5, 6), LocalDate.of(2022, 3, 1));
        WinningNumbers winningNumbers2 = new WinningNumbers(Set.of(1, 2, 3, 4, 5, 7), LocalDate.of(2022, 3, 2));
        List<WinningNumbers> winningNumbers = Arrays.asList(winningNumbers1, winningNumbers2);
        winningNumbersRepository.saveAll(winningNumbers);
    }
}
