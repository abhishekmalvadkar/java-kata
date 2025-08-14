package com.amalvadkar.jk.jdk.datetime;

import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class GreetingService {

    private final Clock clock;

    public String greet() {
        LocalDateTime currentTime = LocalDateTime.now(clock);
        int currentHour = currentTime.getHour();
        if (currentHour < 12) return "Good Morning";
        if (currentHour < 18) return "Good Afternoon";
        return "Good Evening";
    }
}
