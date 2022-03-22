package pl.lottogenerator.lottonumbergenerator;

import java.time.LocalDate;

interface NumberGenerator {

    void generateWinningNumbers(LocalDate drawingDate);
}
