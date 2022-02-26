package pl.lottogenerator.lottonumbergenerator;

import java.util.Set;
import java.util.TreeSet;

class InMemoryWinningNumberRepository implements WinningNumberRepository {

    private Set<Integer> winningNumbers = new TreeSet<>();

    @Override
    public void save(Set<Integer> newGeneratedNumbers) {
        winningNumbers = new TreeSet<>(newGeneratedNumbers);
    }

    @Override
    public Set<Integer> getNumbers() {
        return winningNumbers;
    }
}
