package pl.lottogenerator.lottonumbergenerator;

import java.util.Set;

interface WinningNumberRepository {

    void save(Set<Integer> numbers);

    Set<Integer> getNumbers();
}
