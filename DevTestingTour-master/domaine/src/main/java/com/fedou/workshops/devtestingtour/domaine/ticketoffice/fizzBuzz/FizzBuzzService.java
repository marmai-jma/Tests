package com.fedou.workshops.devtestingtour.domaine.ticketoffice.fizzBuzz;

public class FizzBuzzService {
    public String fizzBuzz (int number){

        if (number%3 == 0){
            return "FIZZ";
        }
        if(number%5 == 0){
            return "BUZZ";
        }
        return ""+number;
    }
}
