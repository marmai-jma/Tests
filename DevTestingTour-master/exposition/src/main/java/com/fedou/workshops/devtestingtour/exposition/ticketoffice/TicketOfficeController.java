package com.fedou.workshops.devtestingtour.exposition.ticketoffice;

import com.fedou.workshops.devtestingtour.domaine.ticketoffice.Reservation;
import com.fedou.workshops.devtestingtour.trainreservation.MakeReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/reservation")
public class TicketOfficeController {
    private MakeReservation makeReservation;

    @Autowired
    public TicketOfficeController(MakeReservation makeReservation)
    {
        this.makeReservation = makeReservation;
    }

    @RequestMapping(method = POST, path = "/makeReservation")
    @ResponseStatus(CREATED)
    public ReservationDTO makeReservation(@RequestBody ReservationRequestDTO req) throws Forbidden {
        String trainId = req.getTrainId();
        if (trainId == null || trainId.trim().isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "trainId should be defined");
        }
        int numberOfSeats = req.getNumberOfSeats();
        if (numberOfSeats <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "seats to book must be a strictly positive number");
        }
        Reservation booking = makeReservation.book(trainId, numberOfSeats);
        return ReservationDTO.toDTO(booking);
    }

}
