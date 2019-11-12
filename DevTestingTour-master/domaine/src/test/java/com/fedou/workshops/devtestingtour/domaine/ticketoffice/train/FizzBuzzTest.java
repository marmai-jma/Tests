package com.fedou.workshops.devtestingtour.domaine.ticketoffice.train;

import com.fedou.workshops.devtestingtour.domaine.ticketoffice.fizzBuzz.FizzBuzzService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FizzBuzzTest {
    private FizzBuzzService fizzBuzzService = new FizzBuzzService();

    @Test
    public void produces_number_when_not_divisible_by_3(){
        String result = fizzBuzzService.fizzBuzz(1);
        Assertions.assertThat(result).isEqualTo("1");
        result = fizzBuzzService.fizzBuzz(2);
        Assertions.assertThat(result).isEqualTo("2");
    }

    @Test
    public void produces_number_when_divisible_by_3(){
        String result = fizzBuzzService.fizzBuzz(3);
        Assertions.assertThat(result).isEqualTo("FIZZ");
    }
}
