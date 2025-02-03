package com.gold_hunter.gold_hunter_bot;

import com.gold_hunter.gold_hunter_bot.utils.GameLists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GoldHunterBotApplication {

    public static void main(String[] args) {
        GameLists gameLists = new GameLists();
        gameLists.fillingGames();
        gameLists.fillingDeliveries();

        SpringApplication.run(GoldHunterBotApplication.class, args);
    }
}
