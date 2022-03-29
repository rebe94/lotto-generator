package pl.lottogenerator.lottonumbergenerator;

import lombok.Builder;
import lombok.Getter;
import pl.lottogenerator.lottonumbergenerator.dto.GenerateConfigurationDto;

@Builder
@Getter
class GenerateConfigurationMessageProvider {

    public static GenerateConfigurationDto valid(Integer amountOfNumbers, Integer lowestNumber, Integer highestNumber) {
        return GenerateConfigurationDto.builder()
                .validationMessage(GenerateConfigurationDto.ValidationMessage.VALID)
                .amountOfNumbers(amountOfNumbers)
                .lowestNumber(lowestNumber)
                .highestNumber(highestNumber)
                .build();
    }

    public static GenerateConfigurationDto failed() {
        return GenerateConfigurationDto.builder()
                .validationMessage(GenerateConfigurationDto.ValidationMessage.FAILED)
                .build();
    }
}
