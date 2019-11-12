package com.fedou.workshops.devtestingtour.domaine.ticketoffice.train;

public interface TrainDataService {

    void reserve(String trainId, String bookingReference, BookableSeats seats);

    Train getTrainById(String trainId);
}
