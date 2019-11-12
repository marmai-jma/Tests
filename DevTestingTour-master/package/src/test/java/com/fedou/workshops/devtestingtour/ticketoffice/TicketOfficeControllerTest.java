package com.fedou.workshops.devtestingtour.ticketoffice;

import com.fedou.workshops.devtestingtour.TrainReservationApplicationUseCasesTest;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.BookableSeats;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.Coach;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.Train;
import com.fedou.workshops.devtestingtour.exposition.ticketoffice.ReservationDTO;
import com.fedou.workshops.devtestingtour.exposition.ticketoffice.ReservationRequestDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static com.fedou.workshops.devtestingtour.exposition.ticketoffice.ReservationDTO.toDTO;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO: from API within spring to domaine required APIs
 */
@ExtendWith(SpringExtension.class)
public class TicketOfficeControllerTest extends TrainReservationApplicationUseCasesTest {

    @WithMockUser
    @Test
    public void no_seats_found() throws Exception {
        String trainId = "express_2000";

        ReservationRequestDTO bookingRequest = new ReservationRequestDTO(trainId, 1);

        ReservationDTO actualReservation = whenMakeReservationFor(bookingRequest);

        ReservationDTO expectedReservation = new ReservationDTO(
                trainId,
                "",
                emptyList());

        assertThat(actualReservation)
                        .isEqualToComparingFieldByField(expectedReservation);
    }

    @WithMockUser
    @Test
    public void make_a_simple_reservation() throws Exception {
        String trainId = "express_2000";
        Train train = theAcceptanceTrain(trainId);
        BookableSeats bookedSeats = new BookableSeats("A", asList(1, 2));

        givenTheTrainDataServerWillProvide(train);

        ReservationRequestDTO bookingRequest = new ReservationRequestDTO(trainId, 2);

        ReservationDTO actualReservation = whenMakeReservationFor(bookingRequest);

        String bookingId = verifyTrainDataServiceReceivesReservation(trainId, bookedSeats);

        ReservationDTO expectedReservation = new ReservationDTO(
                trainId,
                bookingId,
                toDTO(bookedSeats));

        assertThat(actualReservation)
                .isEqualToIgnoringNullFields(expectedReservation);
    }

    @WithMockUser
    @Test
    public void fail_a_reservation_when_train_is_full() throws Exception {
        String trainId = "express_2000";
        Train train = theAcceptanceTrainWithOneFreeSeat(trainId);

        givenTheTrainDataServerWillProvide(train);
        verifyNoMoreInteractions(trainDataService);

        ReservationRequestDTO bookingRequest = new ReservationRequestDTO(trainId, 2);

        ReservationDTO actualReservation = whenMakeReservationFor(bookingRequest);

        ReservationDTO expectedReservation = new ReservationDTO(
                trainId,
                "",
                emptyList());

        assertThat(actualReservation)
                .isEqualToIgnoringNullFields(expectedReservation);
    }

    public Train theAcceptanceTrain(String trainId) {
        List<Coach> coaches = new ArrayList<>();
        coaches.add(new Coach("A", 10, asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
        coaches.add(new Coach("B", 10, asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
        return new Train(trainId, coaches);
    }

    private Train theAcceptanceTrainWithOneFreeSeat(String trainId) {
        List<Coach> coaches = new ArrayList<>();
        coaches.add(new Coach("A", 10, emptyList()));
        coaches.add(new Coach("B", 10, asList(10)));
        return new Train(trainId, coaches);
    }

    private String verifyTrainDataServiceReceivesReservation(String trainId, BookableSeats bookedSeats) {
        ArgumentCaptor<String> booking = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<BookableSeats> seats = ArgumentCaptor.forClass(BookableSeats.class);
        verify(trainDataService).reserve(
                eq(trainId),
                booking.capture(),
                seats.capture());
        Assertions.assertThat(seats.getValue())
                .isEqualToComparingFieldByField(bookedSeats);
        return booking.getValue();
    }

    private void givenTheTrainDataServerWillProvide(Train train) {
        doReturn(train).when(trainDataService).getTrainById(train.getTrainId());
    }


    private ReservationDTO whenMakeReservationFor(ReservationRequestDTO bookingRequest) throws Exception {
        MvcResult result = mvc.perform(
                post("/reservation/makeReservation")
                        .contentType(APPLICATION_JSON)
                        .content(getRequestObjectAsJson(bookingRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        return getResponseContentAs(result, ReservationDTO.class);
    }


}
