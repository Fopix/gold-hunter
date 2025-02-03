package com.gold_hunter.gold_hunter_bot.handlers;

import com.gold_hunter.gold_hunter_bot.repositorys.TelegramUserRepository;
import com.gold_hunter.gold_hunter_bot.utils.Emojis;
import com.gold_hunter.gold_hunter_bot.utils.GameLists;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamesHandler {

    public SendMessage gameSelection(String chatId, TelegramUserRepository userRepository) {

        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        sendMessage.setChatId(chatId);
        sendMessage.setText("Выберите игры, для которых хотите получать заказы");

        List<String> userGames = new ArrayList<>();
        if (userRepository.existsByUserId(chatId)) {
            userGames = userRepository.findByUserId(chatId).getGames();
        }

        ArrayList<String> games = gameList(userGames);

        for (int i = 0; i < games.size(); i++) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();

            keyboardButton.setText(games.get(i));
            keyboardButton.setCallbackData("game_select-" + i);
            rowsInline.add(Collections.singletonList(keyboardButton));
        }

        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

        return sendMessage;
    }

    private ArrayList<String> gameList(List<String> userGames) {
        ArrayList<String> games = new ArrayList<>(GameLists.games);

        for (int a = 0; a < games.size(); a++) {
            if (userGames.size() != 0) {
                for (int b = 0; b < userGames.size(); b++) {
                    if (games.get(a).equals(userGames.get(b))) {
                        games.set(a, games.get(a) + " " + Emojis.HEAVY_MARK);
                        break;
                    } else if (userGames.size() == b + 1) {
                        games.set(a, games.get(a) + " " + Emojis.HEAVY_X);
                    }
                }
            } else {
                games.set(a, games.get(a) + " " + Emojis.HEAVY_X);
            }
        }

        return games;
    }
}
