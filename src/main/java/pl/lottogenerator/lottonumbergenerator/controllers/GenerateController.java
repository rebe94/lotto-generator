package pl.lottogenerator.lottonumbergenerator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lottogenerator.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lottogenerator.lottonumbergenerator.dto.GenerateConfiguration;
import pl.lottogenerator.lottonumbergenerator.dto.WinningNumbers;

import java.util.logging.Logger;

@RestController
public class GenerateController {

    private LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    @Autowired
    public GenerateController(LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        this.lottoNumberGeneratorFacade = lottoNumberGeneratorFacade;
    }

    private static final Logger logger = Logger.getLogger(GenerateController.class.getName());

    @GetMapping("/generator")
    public ResponseEntity<WinningNumbers> generateNumbers(
            @RequestHeader String requestId,
            @RequestParam Integer amountOfNumbers,
            @RequestParam Integer lowestNumber,
            @RequestParam Integer highestNumber
    ) {
        logger.info("Received request with ID " + requestId + "; " +
                "amount of numbers: " + amountOfNumbers + ", " +
                "lowest number: " + lowestNumber + ", " +
                "highest number: " + highestNumber
        );

        GenerateConfiguration generateConfiguration = new GenerateConfiguration(amountOfNumbers, lowestNumber, highestNumber);
        WinningNumbers winningNumbers = new WinningNumbers();
        winningNumbers.setWinningNumbers(lottoNumberGeneratorFacade.generateWinningNumbers(generateConfiguration));

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("requestId", requestId)
                .body(winningNumbers);
    }
}
