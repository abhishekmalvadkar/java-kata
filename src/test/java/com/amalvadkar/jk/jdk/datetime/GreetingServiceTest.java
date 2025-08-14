package com.amalvadkar.jk.jdk.datetime;

import com.amalvadkar.jk.common.AbstractJavaTest;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

public class GreetingServiceTest extends AbstractJavaTest {

    @Test
    void should_return_good_morning() {
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2025, 7, 4, 9, 0).atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        GreetingService greetingService = new GreetingService(fixedClock);
        assertThat(greetingService.greet()).isEqualTo("Good Morning");
    }

    @Test
    void should_return_good_afternoon() {
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2025, 7, 4, 13, 0).atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        GreetingService greetingService = new GreetingService(fixedClock);
        assertThat(greetingService.greet()).isEqualTo("Good Afternoon");
    }

    @Test
    void should_return_good_evening() {
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2025, 7, 4, 19, 0).atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        GreetingService greetingService = new GreetingService(fixedClock);
        assertThat(greetingService.greet()).isEqualTo("Good Evening");
    }
}
