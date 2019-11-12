package com.fedou.workshops.devtestingtour.infrastructure.traindata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface TrainDataDAO extends JpaRepository<TrainData, String> {
    List<TrainData> findByTrain(String train);
    List<TrainData> findByTrainAndBooking(String train, String booking);
    @Modifying
    @Query(value = "UPDATE Train SET booking=?4 WHERE train=?1 AND coach=?2 AND seat IN ?3", nativeQuery = true)
    void updateBooking(String trainId, String coach, List<Integer> seats, String booking);
}
