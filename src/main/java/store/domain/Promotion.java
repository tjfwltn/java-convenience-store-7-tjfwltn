package store.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Promotion {

    private String name;
    private int buy;
    private int get;
    private LocalDate StartDate;
    private LocalDate EndDate;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        StartDate = startDate;
        EndDate = endDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Promotion promotion = (Promotion) object;
        return buy == promotion.buy && get == promotion.get && Objects.equals(name, promotion.name) && Objects.equals(StartDate, promotion.StartDate) && Objects.equals(EndDate, promotion.EndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, buy, get, StartDate, EndDate);
    }
}
