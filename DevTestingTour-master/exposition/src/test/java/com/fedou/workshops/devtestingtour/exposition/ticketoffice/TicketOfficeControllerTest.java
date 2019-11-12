package com.fedou.workshops.devtestingtour.exposition.ticketoffice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.Reservation;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.BookableSeats;
import com.fedou.workshops.devtestingtour.trainreservation.MakeReservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TicketOfficeController.class)
class TicketOfficeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MakeReservation makeReservation;

    @Autowired
    protected ObjectMapper mapper;


    @Test
    void makeReservation_should_not_authorize_anonymous() throws Exception {
        MvcResult mvcResult = mvc.perform(
                post("/reservation/makeReservation")
                        .contentType(APPLICATION_JSON))
                //.content("")
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
        assertThat(mvcResult).isNotNull();
    }

    @Test
    @WithMockUser("user")
    void makeReservation_should_not_handle_empty_requests() throws Exception {
        MvcResult mvcResult = mvc.perform(
                post("/reservation/makeReservation")
                        .contentType(APPLICATION_JSON))
                //.content("")
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(mvcResult).isNotNull();
    }

    @Test
    @WithMockUser("user")
    void makeReservation_should_not_handle_empty_train() throws Exception {
        mvc.perform(
                post("/reservation/makeReservation")
                        .contentType(APPLICATION_JSON)
                        .content(getRequestObjectAsJson(new ReservationRequestDTO(null, 2))))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser("user")
    void makeReservation_should_not_handle_negative_number_of_seats() throws Exception {
        mvc.perform(
                post("/reservation/makeReservation")
                        .contentType(APPLICATION_JSON)
                        .content(getRequestObjectAsJson(new ReservationRequestDTO("express_2000", -2))))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser("user")
    void makeReservation_should_handle_reservation() throws Exception {
        String trainId = "express_2000";
        String booking = "booking";
        int numberOfSeats = 2;
        doReturn(new Reservation(trainId, booking, new BookableSeats("A", asList(1, 2))))
        .when(makeReservation).book(eq(trainId), eq(numberOfSeats));

        ReservationDTO reservation = whenMakeReservationFor(new ReservationRequestDTO(trainId, numberOfSeats));

        assertThat(reservation).isEqualToComparingFieldByField(new ReservationDTO(trainId, booking, asList("1A", "2A")));
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

    @SuppressWarnings("SameParameterValue")
    protected <T> T getResponseContentAs(MvcResult result, Class<T> valueType) throws java.io.IOException {
        return mapper.readValue(result.getResponse().getContentAsString(), valueType);
    }

    protected String getRequestObjectAsJson(Object requestObject) throws JsonProcessingException {
        return mapper.writeValueAsString(requestObject);
    }

}
