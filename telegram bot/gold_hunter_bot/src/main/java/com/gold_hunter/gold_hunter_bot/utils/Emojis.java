package com.gold_hunter.gold_hunter_bot.utils;

import com.vdurmont.emoji.EmojiParser;

public enum Emojis {
    HEAVY_MARK(EmojiParser.parseToUnicode(":white_check_mark:")),
    HEAVY_X(EmojiParser.parseToUnicode(":x:"));

    private final String emojiName;

    Emojis(String emojiName) {
        this.emojiName = emojiName;
    }


    @Override
    public String toString() {
        return emojiName;
    }
}
