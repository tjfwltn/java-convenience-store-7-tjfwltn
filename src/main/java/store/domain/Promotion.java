package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;
import java.util.Objects;

public class Promotion {

    private String name;
    private int purchaseAmount;
    private int giftAmount;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int purchaseAmount, int giftAmount, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.purchaseAmount = purchaseAmount;
        this.giftAmount = giftAmount;
        this.startDate = startDate;
        this.endDate = endDate;
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
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isPromotionDay() {
        LocalDate today = LocalDate.from(DateTimes.now());
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Promotion promotion = (Promotion) object;
        return purchaseAmount == promotion.purchaseAmount && giftAmount == promotion.giftAmount && Objects.equals(name, promotion.name) && Objects.equals(startDate, promotion.startDate) && Objects.equals(endDate, promotion.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, purchaseAmount, giftAmount, startDate, endDate);
    }

    @Override
    public String toString() {
        return getName();
    }
}
