package com.fedou.workshops.devtestingtour.trainreservation;

import com.fedou.workshops.devtestingtour.domaine.ticketoffice.Reservation;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.BookableSeats;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.Coach;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.Train;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.TrainDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MakeReservationTest {
    @Mock
    TrainDataService trainDataService;

    @InjectMocks
    MakeReservation service;

    @Test
    void should_fail_booking_for_no_seats() {
        Reservation reservation = service.book("trainId", 0);

        assertAll(
                () -> verifyNoInteractions(trainDataService),
                () -> assertThat(reservation)
                        .isEqualToComparingFieldByField(new Reservation(null, null, null))
        );
    }

    @Test
    void should_book_available_seats() {
        String trainId = "trainId";
        doReturn(new Train(trainId, singletonList(new Coach("A", 10, asList(3, 5, 6, 7, 8, 9, 10)))))
                .when(trainDataService).getTrainById(trainId);

        Reservation reservation = service.book(trainId, 1);

        thenBookingIsDoneAndReturned(trainId, reservation.getBookingReference(), reservation, new BookableSeats("A", asList(3)));
    }

    @Test
    void should_fail_booking_when_no_seat_found() {
        String trainId = "trainId";

        doReturn(new Train(trainId, singletonList(new Coach("A", 1, emptyList()))))
                .when(trainDataService).getTrainById(trainId);

        Reservation reservation = service.book(trainId, 1);

        thenNoBookingDoneAndEmptyBookingReturned(trainId, reservation);
    }

    @Test
    void should_book_sibling_seats() {
        String trainId = "trainId";

        doReturn(new Train(trainId, singletonList(new Coach("A", 5, asList(1, 3, 4, 5)))))
                .when(trainDataService).getTrainById(trainId);

        Reservation reservation = service.book(trainId, 2);

        thenBookingIsDoneAndReturned(trainId, reservation.getBookingReference(), reservation, new BookableSeats("A", asList(1, 3)));
    }

    @Test
    void book_all_seats_on_single_coach() {
        String trainId = "trainId";
        doReturn(new Train(trainId, asList(
                new Coach("A", 10, asList(6, 7, 8, 9, 10)),
                new Coach("B", 10, asList(4, 5, 6, 7, 8, 9, 10))
        ))).when(trainDataService).getTrainById(trainId);

        Reservation reservation = service.book(trainId, 6);

        thenBookingIsDoneAndReturned(trainId, reservation.getBookingReference(), reservation,
                new BookableSeats("B", asList(4, 5, 6, 7, 8, 9)));
    }

    private void thenBookingIsDoneAndReturned(String trainId, String bookingReference, Reservation reservation, BookableSeats bookedSeats) {
        ArgumentCaptor<BookableSeats> argument = ArgumentCaptor.forClass(BookableSeats.class);
        assertAll(
                () -> verify(trainDataService).reserve(eq(trainId), eq(bookingReference), argument.capture()),
                () -> assertThat(argument.getValue()).isEqualToComparingFieldByField(bookedSeats),
                () -> assertThat(reservation)
                        .isEqualToIgnoringNullFields(new Reservation(trainId, bookingReference, null)),
                () -> assertThat(reservation.getSeats())
                        .isEqualToComparingFieldByField(bookedSeats)
        );
    }

    private void thenNoBookingDoneAndEmptyBookingReturned(String trainId, Reservation reservation) {
        assertAll(
                () -> verify(trainDataService, times(0))
                        .reserve(anyString(), anyString(), any(BookableSeats.class)),
                () -> assertThat(reservation)
                        .isEqualToComparingFieldByField(new Reservation(trainId, "", null))
        );
    }

}
