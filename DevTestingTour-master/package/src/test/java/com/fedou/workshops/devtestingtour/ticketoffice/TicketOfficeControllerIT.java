package com.fedou.workshops.devtestingtour.ticketoffice;

import com.fedou.workshops.devtestingtour.TrainReservationApplicationFastIntegrationTest;
import com.fedou.workshops.devtestingtour.exposition.ticketoffice.ReservationDTO;
import com.fedou.workshops.devtestingtour.exposition.ticketoffice.ReservationRequestDTO;
import com.fedou.workshops.devtestingtour.infrastructure.traindata.TrainData;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO: from API within spring to H2 DB included
 */
@ExtendWith(SpringExtension.class)
class TicketOfficeControllerIT extends TrainReservationApplicationFastIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(TicketOfficeControllerIT.class);

    @WithMockUser
    @Test
    public void make_a_simple_reservation() throws Exception {
        String trainId = "express_2000";
        List<TrainData> trainData = theAcceptanceTrain(trainId);
        List<String> bookedSeats = asList("1A", "2A");

        givenTheTrainDataServerWillProvide(trainId, trainData);

        ReservationRequestDTO bookingRequest = new ReservationRequestDTO(trainId, 2);

        ReservationDTO actualReservation = whenMakeReservationFor(bookingRequest);

        List<TrainData> actualReservationInDB = trainDataInMemory.findByTrainAndBooking(actualReservation.getTrain_id(), actualReservation.getBooking_reference());

        logger.info("actualReservation    :"+actualReservation);
        logger.info("actualReservationInDB:"+actualReservationInDB);

        assertAll(
                () -> assertThat(actualReservationInDB)
                        .extracting((TrainData current) -> ""+current.getSeat()+current.getCoach())
                        .containsOnlyElementsOf(actualReservation.getSeats()),
                () -> assertThat(actualReservationInDB)
                        .extracting("train", "booking")
                        .containsOnlyElementsOf(
                                asList(new Tuple(trainId, actualReservation.getBooking_reference())))
        );
    }

    private void givenTheTrainDataServerWillProvide(String trainId, List<TrainData> trainData) {
        trainDataInMemory.saveAll(trainData);
    }

    public List<TrainData> theAcceptanceTrain(String train) {
        List<TrainData> seatDatas = new ArrayList<>();
        for (String coach : new String[]{"A", "B"}) {
            IntStream.range(1, 11).forEach(seatNumber -> {
                seatDatas.add(
                        new TrainData(
                                train,
                                coach,
                                seatNumber));
            });
        }

        return seatDatas;
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


//
//    @WithMockUser
//    @Test
//    public void no_seats_found() throws Exception {
//        String trainId = "express_2000";
//        TrainData trainData = new TrainData();
//        trainData.seats = new SeatData[]{
//                new SeatData("booked", "A", 1),
//                new SeatData("booked", "A", 2),
//                new SeatData("booked", "A", 3)
//        };
//        String bookingId = "something";
//        List<String> bookedSeats = emptyList();
//
//        givenTheTrainDataServerWillProvide(trainId, trainData);
//        ReservationRequestDTO bookingRequest = new ReservationRequestDTO(trainId, 1);
//        givenTheTrainDataServerWillBeCalledForAReservation();
//
//        ReservationDTO actualReservation = whenMakeReservationFor(bookingRequest);
//
//        ReservationDTO expectedReservation = new ReservationDTO(
//                trainId,
//                "",
//                bookedSeats);
//        assertAll(
//                this::thenTheTrainDataServerDoNotReceivesReservation,
//                () -> assertThat(actualReservation)
//                        .isEqualToComparingFieldByField(expectedReservation)
//        );
//    }
//
//    @BeforeEach
//    public void resetMocks() {
//        reset();
//    }
//
//    TrainData theAcceptanceTrain() {
//        TrainData trainData = new TrainData();
//        ArrayList<SeatData> seatDatas = new ArrayList<>();
//        for (String coach : new String[]{"A", "B"}) {
//            IntStream.range(1, 11).forEach(seatNumber -> {
//                seatDatas.add(
//                        new SeatData(
//                                null,
//                                coach,
//                                seatNumber));
//            });
//        }
//
//        trainData.seats = seatDatas.toArray(new SeatData[seatDatas.size()]);
//        return trainData;
//    }
//
//    private void givenTheBookingReferenceServerWillProvide(String bookingId) {
//        givenThat(get("/booking_reference").willReturn(ok(bookingId)));
//    }
//
//    private void givenTheTrainDataServerWillProvide(String trainId, TrainData trainData) throws JsonProcessingException {
//        givenThat(get("/data_for_train/" + trainId)
//                .willReturn(aResponse()
//                        .withHeader("content-type", "application/json")
//                        .withBody(getRequestObjectAsJson(trainData))));
//    }
//
//    private void givenTheTrainDataServerWillBeCalledForAReservation() {
//        givenThat(post("/reserve").willReturn(ok()));
//    }
//
//    private ReservationDTO whenMakeReservationFor(ReservationRequestDTO bookingRequest) throws Exception {
//        MvcResult result = mvc.perform(
//                MockMvcRequestBuilders.post("/reservation/makeReservation")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(getRequestObjectAsJson(bookingRequest)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        return getResponseContentAs(result, ReservationDTO.class);
//    }
//
//    private void thenTheTrainDataServerReceives(ReservationDTO reservation) throws JsonProcessingException {
//        String body = "{ \"trainId\": [\"express_2000\"], " +
//                "\"seats\": [[\"1A\", \"2A\"]], " +
//                "\"bookingId\": [\"" + reservation.getBooking_reference() + "\"]}";
//        verify(1, postRequestedFor(urlEqualTo("/reserve"))
//                .withRequestBody(equalToJson(body)));
//    }
//
//    private void thenTheTrainDataServerDoNotReceivesReservation() {
//        verify(0, postRequestedFor(urlEqualTo("/reserve")));
//    }
}

