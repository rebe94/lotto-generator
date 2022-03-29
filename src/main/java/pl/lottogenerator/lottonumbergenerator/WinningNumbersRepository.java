package pl.lottogenerator.lottonumbergenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WinningNumbersRepository extends MongoRepository<WinningNumbers, String> {

    Optional<WinningNumbers> findByDrawDate(LocalDate drawDate);
}
