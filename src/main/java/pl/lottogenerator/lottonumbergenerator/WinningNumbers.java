package pl.lottogenerator.lottonumbergenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Document(collection = "winning_numbers")
class WinningNumbers {

    @Id
    private String id;
    private Set<Integer> numbers;
    private LocalDate drawDate;

    @Builder
    public WinningNumbers(Set<Integer> numbers, LocalDate drawDate) {
        this.numbers = numbers;
        this.drawDate = drawDate;
    }
}
