package store.domain;

import java.time.LocalDate;

public class DefaultPromotion extends Promotion {

    public DefaultPromotion() {
        super("null", 0, 0, LocalDate.MIN, LocalDate.MIN);
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
