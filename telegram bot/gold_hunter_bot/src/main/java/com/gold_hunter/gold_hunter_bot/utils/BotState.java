package com.gold_hunter.gold_hunter_bot.utils;

import java.util.HashMap;
import java.util.Map;

public enum BotState {
    DEFAULT,
    ASK_PASSWORD,
    ASK_SUM_WITHDRAWAL_0,
    ASK_SUM_WITHDRAWAL_1,
    ASK_SUM_WITHDRAWAL_2,
    ASK_WALLET_0,
    ASK_WALLET_1,
    ASK_WALLET_2;

    public static Map<String, BotState> userState = new HashMap<>();
}
