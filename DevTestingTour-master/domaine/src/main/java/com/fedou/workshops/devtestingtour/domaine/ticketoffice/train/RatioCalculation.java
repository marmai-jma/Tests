package com.fedou.workshops.devtestingtour.domaine.ticketoffice.train;

final class RatioCalculation {
    private RatioCalculation()  throws Exception {
        throw new Exception("this class holds utility methods only, no instances wanted");
    }

    static boolean isUnder70PercentWhenBookingOf(int totalSeats, int totalFreeSeats, int numberOfSeats) {
        return !isAbove70PercentWhenBookingOf(totalSeats, totalFreeSeats, numberOfSeats);
    }

    static boolean isAbove70PercentWhenBookingOf(int totalSeats, int freeSeats, int seatsToBook) {
        if (totalSeats <= 0) return true; // should not book seats in train without seats (Fret ???)
        int actualBookedSeats = totalSeats - freeSeats;
        float ratio = (float) (actualBookedSeats + seatsToBook) / (float) totalSeats; // divide with numbers converted in float as afterwards is too late
        return ratio > 0.70;
    }
}
