package com.fedou.workshops.devtestingtour.domaine.ticketoffice.train;

import org.junit.jupiter.api.Test;

import static com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.RatioCalculation.isAbove70PercentWhenBookingOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RatioCalculationTest {

    @Test
    void isAbove70PercentWhenBookingOf_should_do_the_math() {
        assertAll(
                () -> assertThat(isAbove70PercentWhenBookingOf(10, 4, 1)).describedAs("4").isFalse(),
                () -> assertThat(isAbove70PercentWhenBookingOf(10, 3, 1)).describedAs("3").isTrue(),
                () -> assertThat(isAbove70PercentWhenBookingOf(10, 2, 1)).describedAs("2").isTrue(),
                () -> assertThat(isAbove70PercentWhenBookingOf(100, 31, 1)).describedAs("31").isFalse(),
                () -> assertThat(isAbove70PercentWhenBookingOf(100, 30, 1)).describedAs("30").isTrue(),
                () -> assertThat(isAbove70PercentWhenBookingOf(100, 29, 1)).describedAs("29").isTrue()
        );
    }
}