package com.fedou.workshops.devtestingtour.domaine.ticketoffice.train;

import java.util.List;

public class BookableSeats {
    private final String coach;
    private final List<Integer> seats;

    public BookableSeats(String coach, List<Integer> seats) {
        this.coach = coach;
        this.seats = seats;
    }

    public String getCoach() {
        return coach;
    }

    public List<Integer> getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BookableSeats{");
        sb.append("coach='").append(coach).append('\'');
        sb.append(", seats=").append(seats);
        sb.append('}');
        return sb.toString();
    }
}
