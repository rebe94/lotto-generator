package pl.lottogenerator.lottonumbergenerator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateConfiguration {

    private Integer amountOfNumbers, lowestNumber, highestNumber;

    public GenerateConfiguration(Integer amountOfNumbers, Integer lowestNumber, Integer highestNumber) {
        this.amountOfNumbers = amountOfNumbers;
        this.lowestNumber = lowestNumber;
        this.highestNumber = highestNumber;
    }
}
