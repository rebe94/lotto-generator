package pl.lottogenerator.lottonumbergenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenerateConfigurationDto {

    private Integer amountOfNumbers, lowestNumber, highestNumber;
    private ValidationMessage validationMessage;

    public enum ValidationMessage {
        VALID,
        FAILED
    }
}
