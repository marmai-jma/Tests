package com.fedou.workshops.devtestingtour.infrastructure.traindata;

import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.BookableSeats;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.Coach;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.Train;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.TrainDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.*;

import static java.util.Collections.emptyList;

@Repository
@Transactional
@Primary
public class TrainDataInMemoryPersistence implements TrainDataService {

    @Autowired
    protected TrainDataDAO trainDataDAO;

    @Override
    public void reserve(String trainId, String booking, BookableSeats seats) {
        trainDataDAO.updateBooking(trainId, seats.getCoach(), seats.getSeats(), booking);
    }

    @Override
    public Train getTrainById(String trainId) {
        List<TrainData> trainData = trainDataDAO.findByTrain(trainId);
        Map<String, List<Integer>> freeSeatsBuilder = new HashMap<>();
        Map<String, Integer> totalSeatsBuilder = new HashMap<>();
        trainData.forEach(seat -> {
                    if (seat.booking == null) {
                        List<Integer> seats = freeSeatsBuilder.computeIfAbsent(seat.coach, key -> new LinkedList<>());
                        seats.add(seat.seat);
                    }
                    Integer totalSeats = 1 + totalSeatsBuilder.computeIfAbsent(seat.coach, key -> 0);
                    totalSeatsBuilder.put(seat.coach, totalSeats);
                }
        );
        List<Coach> coaches = new ArrayList<>();
        totalSeatsBuilder.forEach(
                (key, value) -> coaches.add(
                        new Coach(
                                key,
                                value,
                                freeSeatsBuilder.getOrDefault(key, emptyList()))));
        return new Train(trainId, coaches);
    }

}
