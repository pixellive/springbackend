package com.thangthai.training.backend.schedule;

import com.thangthai.training.backend.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserSchedule {

    private UserService userService;

    public UserSchedule(UserService userService) {
        this.userService = userService;
    }

    // Schedule note
    // 1 => second
    // 2 => minute
    // 3 => hour
    // 4 => day
    // 5 => month
    // 6 => year

    /**
     * Every minute (UTC Time)
     */
    @Scheduled(cron = "0 * * * * *")
    public void testEveryMinute(){
       log.info("Hello, What's up?");
    }

    /**
     * Everyday at 00:00 (UTC Time)
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void testEveryMidNight(){

    }

    /**
     * Everyday at 21:50 (Thai Time)
     */
    @Scheduled(cron = "0 50 21 * * *", zone = "Asia/Bangkok")
    public void testEveryDayNineAM(){
        log.info("Hey Hoo!!!");
    }
}
