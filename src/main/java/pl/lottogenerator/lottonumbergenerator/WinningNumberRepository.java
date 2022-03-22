package pl.lottogenerator.lottonumbergenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WinningNumberRepository extends MongoRepository<WinningNumbers, String> {

    Optional<WinningNumbers> findByDrawingDate(LocalDate drawingDate);
}
