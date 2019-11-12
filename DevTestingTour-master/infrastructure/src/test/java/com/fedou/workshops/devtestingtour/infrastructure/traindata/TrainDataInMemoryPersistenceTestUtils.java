package com.fedou.workshops.devtestingtour.infrastructure.traindata;

import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TrainDataInMemoryPersistenceTestUtils extends TrainDataInMemoryPersistence implements TrainDataPersistenceTestUtils {

    @Override
    public void saveAll(List<TrainData> trainData) {
        trainDataDAO.saveAll(trainData);
    }

    @Override
    public List<TrainData> findByTrainAndBooking(String train, String booking) {
        return trainDataDAO.findByTrainAndBooking(train, booking);
    }
}