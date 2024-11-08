package store.domain;

import java.time.LocalDate;

public class DefaultPromotion extends Promotion {

    public DefaultPromotion() {
        super("", 0, 0, LocalDate.MIN, LocalDate.MAX);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }
}
