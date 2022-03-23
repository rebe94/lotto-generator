package pl.lottogenerator.lottonumbergenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenerateConfigurationDto {

    private Integer amountOfNumbers, lowestNumber, highestNumber;
}
