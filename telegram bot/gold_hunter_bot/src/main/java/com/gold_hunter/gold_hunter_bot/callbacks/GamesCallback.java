package com.gold_hunter.gold_hunter_bot.callbacks;

import com.gold_hunter.gold_hunter_bot.models.TelegramUser;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramUserRepository;
import com.gold_hunter.gold_hunter_bot.utils.Emojis;
import com.gold_hunter.gold_hunter_bot.utils.GameLists;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamesCallback {

    public EditMessageText gameSelect(CallbackQuery callbackQuery, TelegramUserRepository userRepository) {

        String chatId = callbackQuery.getMessage().getChatId().toString();
        TelegramUser user = userRepository.findByUserId(chatId);
        List<String> games = user.getGames();

        for (int i = 0; i < GameLists.games.size(); i++) {
            if (callbackQuery.getData().equals("game_select-" + i)) {
                if (games.contains(GameLists.games.get(i))) {
                    games.remove(GameLists.games.get(i));
                } else {
                    games.add(GameLists.games.get(i));
                }
                break;
            }
        }

        user.setGames(games);
        userRepository.save(user);

        //обновление кнопок
        EditMessageText editMessage = new EditMessageText();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        editMessage.setChatId(chatId);
        editMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessage.setText("Выберите игры, для которых хотите получать заказы");

        List<String> userGames = new ArrayList<>();
        if (userRepository.existsByUserId(chatId)) {
            userGames = userRepository.findByUserId(chatId).getGames();
        }

        ArrayList<String> gameList = gameList(userGames);

        for (int i = 0; i < gameList.size(); i++) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();

            keyboardButton.setText(gameList.get(i));
            keyboardButton.setCallbackData("game_select-" + i);
            rowsInline.add(Collections.singletonList(keyboardButton));
        }

        markupInline.setKeyboard(rowsInline);
        editMessage.setReplyMarkup(markupInline);

        return editMessage;
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
