package pl.lottogenerator.lottonumbergenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lottogenerator.lottonumbergenerator.LottoNumberGeneratorFacade;
import pl.lottogenerator.lottonumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDate;
import java.util.Set;

@RestController
class WinningNumberController {

    private final LottoNumberGeneratorFacade lottoNumberGeneratorFacade;

    @Autowired
    public WinningNumberController(LottoNumberGeneratorFacade lottoNumberGeneratorFacade) {
        this.lottoNumberGeneratorFacade = lottoNumberGeneratorFacade;
    }

    @GetMapping("/winningnumbers")
    ResponseEntity<WinningNumbersDto> winningNumbers(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Integer day) {
        LocalDate dateDto = LocalDate.of(year, month, day);
        Set<Integer> winningNumbers = lottoNumberGeneratorFacade.getWinningNumbers(dateDto);
        WinningNumbersDto winningNumbersDto = new WinningNumbersDto();
        winningNumbersDto.setWinningNumbers(winningNumbers);

        if (winningNumbers.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(winningNumbersDto);
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(winningNumbersDto);
    }
}
