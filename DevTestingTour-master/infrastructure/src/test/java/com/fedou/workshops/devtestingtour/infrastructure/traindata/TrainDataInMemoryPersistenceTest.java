package com.fedou.workshops.devtestingtour.infrastructure.traindata;

import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.Coach;
import com.fedou.workshops.devtestingtour.domaine.ticketoffice.train.Train;
import com.fedou.workshops.devtestingtour.infrastructure.RelationalPersistenceTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ContextConfiguration(classes = RelationalPersistenceTestConfiguration.class)
@ExtendWith(SpringExtension.class)
public class TrainDataInMemoryPersistenceTest {

//    @Autowired
//    private DataSource dataSource;
//    @Autowired
//    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TrainDataPersistenceTestUtils trainDataPersistence;

    @Test
    void saveAll_should_insert_multiple_rows() {
        List<TrainData> trainData = asList(
                new TrainData("train", "coach", 1),
                new TrainData("train", "coach", 2, "booking")
        );
        List<TrainData> expectedTrainData = asList(
                new TrainData("train", "coach", 1),
                new TrainData("train", "coach", 2, "booking")
        );
        trainDataPersistence.saveAll(trainData);
        // trainData.forEach(entityManager::detach);
        entityManager.flush();
        Query query = entityManager.createNativeQuery("select * from TRAIN", TrainData.class);
        List actualTrainData = query.getResultList();
        assertAll(
                () -> assertThat(actualTrainData.get(0)).isEqualToIgnoringGivenFields(expectedTrainData.get(0), "rowId"),
                () -> assertThat(actualTrainData.get(1)).isEqualToIgnoringGivenFields(expectedTrainData.get(1), "rowId")
                // why following  does not work ? () -> assertThat(actualTrainData).usingElementComparatorIgnoringFields("rowId").containsOnly(trainData);
        );
    }

    @Test
    void getTrainById_select_the_good_train() {
        asList(
                new TrainData("other", "coach", 1),
                new TrainData("trainId", "coach", 1),
                new TrainData("trainId", "coach", 2)
        ).forEach(row -> entityManager.persist(row));
        entityManager.flush();
        Train train = trainDataPersistence.getTrainById("trainId");
        assertThat(train).usingRecursiveComparison().isEqualTo(
                new Train("trainId", asList(
                        new Coach("coach", 2, asList(1, 2))
                ))
        );
    }

    @Test
    void getTrainById_convert_trainData_to_train() {
        asList(
                new TrainData("trainId", "coach", 1, "bookingId"),
                new TrainData("trainId", "coach", 2),
                new TrainData("trainId", "coach", 3),
                new TrainData("trainId", "coach2", 1),
                new TrainData("trainId", "coach2", 2)
        ).forEach(row -> entityManager.persist(row));
        entityManager.flush();
        Train actualTrain = trainDataPersistence.getTrainById("trainId");
        Train expectedTrain = new Train("trainId", asList(
                new Coach("coach", 3, asList(2, 3)),
                new Coach("coach2", 2, asList(1, 2))
        ));
        LoggerFactory.getLogger(this.getClass()).info("actual   train : " + actualTrain);
        LoggerFactory.getLogger(this.getClass()).info("expected train : " + expectedTrain);
        assertThat(actualTrain)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedTrain);
    }
}
