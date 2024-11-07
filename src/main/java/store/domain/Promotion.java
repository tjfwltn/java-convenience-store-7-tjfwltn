package store.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Promotion {

    private String name;
    private int purchaseAmount;
    private int giftAmount;
    private LocalDate StartDate;
    private LocalDate EndDate;

    public Promotion(String name, int purchaseAmount, int giftAmount, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.purchaseAmount = purchaseAmount;
        this.giftAmount = giftAmount;
        StartDate = startDate;
        EndDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getPurchaseAmount() {
        return purchaseAmount;
    }

    public int getGiftAmount() {
        return giftAmount;
    }

    public LocalDate getStartDate() {
        return StartDate;
    }

    public LocalDate getEndDate() {
        return EndDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Promotion promotion = (Promotion) object;
        return purchaseAmount == promotion.purchaseAmount && giftAmount == promotion.giftAmount && Objects.equals(name, promotion.name) && Objects.equals(StartDate, promotion.StartDate) && Objects.equals(EndDate, promotion.EndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, purchaseAmount, giftAmount, StartDate, EndDate);
    }

    @Override
    public String toString() {
        return getName();
    }
}
