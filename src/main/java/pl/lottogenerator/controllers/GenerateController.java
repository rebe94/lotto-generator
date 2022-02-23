package pl.lottogenerator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.lottogenerator.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lottogenerator.model.GenerateConfiguration;

import java.util.Set;
import java.util.logging.Logger;

@RestController
public class GenerateController {

    @Autowired
    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    private static final Logger logger = Logger.getLogger(GenerateController.class.getName());

    @PostMapping("/generate")
    public ResponseEntity<Integer[]> generateNumbers(
            @RequestHeader String requestId,
            @RequestBody GenerateConfiguration generateConfiguration
    ) {
        logger.info("Received request with ID " + requestId + "; " +
                "amount of numbers: " + generateConfiguration.getAmountOfNumbers() + ", " +
                "lowest number: " + generateConfiguration.getLowestNumber() + ", " +
                "highest number: " + generateConfiguration.getHighestNumber()
        );

        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.winningNumbers(generateConfiguration);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("requestId", requestId)
                .body(winningNumbers.toArray(new Integer[0]));
    }
}
