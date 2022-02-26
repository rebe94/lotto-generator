package pl.lottogenerator.lottonumbergenerator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.lottogenerator.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lottogenerator.lottonumbergenerator.dto.WinningNumbersDto;

import java.util.Set;
import java.util.logging.Logger;

@RestController
class WinningNumberController {

    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    @Autowired
    public WinningNumberController(LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        this.lottoNumberGeneratorFacade = lottoNumberGeneratorFacade;
    }

    @GetMapping("/winningnumbers")
    ResponseEntity<WinningNumbersDto> winningNumbers() {
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers();
        WinningNumbersDto winningNumbersDto = new WinningNumbersDto();
        winningNumbersDto.setWinningNumbers(winningNumbers);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(winningNumbersDto);
    }
}
