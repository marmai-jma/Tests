package com.fedou.workshops.devtestingtour.infrastructure.traindata;

import javax.persistence.*;

@Entity
@Table(name = "Train",
uniqueConstraints = @UniqueConstraint(columnNames = {"train", "coach", "seat"}))
public class TrainData {
    @Id
    @GeneratedValue
    private Integer rowId;

    String train;
    String coach;
    Integer seat;

    String booking;

    public TrainData() {
        super();
    }

    public TrainData(String train, String coach, Integer seat) {
        this(train, coach, seat, null);
    }

    public TrainData(String train, String coach, Integer seat, String booking) {
        this.train = train;
        this.coach = coach;
        this.seat = seat;
        this.booking = booking;
    }

    public String getCoach() {
        return coach;
    }

    public Integer getSeat() {
        return seat;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TrainData{");
        sb.append("rowId=").append(rowId);
        sb.append(", train='").append(train).append('\'');
        sb.append(", coach='").append(coach).append('\'');
        sb.append(", seat=").append(seat);
        sb.append(", booking='").append(booking).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
