package com.fedou.workshops.devtestingtour.domaine.ticketoffice.booking;

import java.util.UUID;

public final class BookingReferenceUtils {
    private BookingReferenceUtils() throws Exception {
        throw new Exception("this class holds utility methods only, no instances wanted");
    }

    public static String getUniqueBookingReference() {
        return UUID.randomUUID().toString();
    }
}
