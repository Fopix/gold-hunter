package com.gold_hunter.gold_hunter_bot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameLists {

    public static ArrayList<String> games = new ArrayList<>();

    public static Map<String, String> gameDeliveries = new HashMap<>();

    public void fillingGames() {
        games.add("Aion RuOff");
        games.add("Albion Online");
        games.add("ArcheAge");
        games.add("ArcheAge Unchained");
        games.add("Astellia Online");
        games.add("Blade and Soul RuOff");
        games.add("Escape from Tarkov");
        games.add("EVE Echoes");
        games.add("EVE Online");
        games.add("Fallout 76");
        games.add("Final Fantasy XIV");
        games.add("Guild Wars 2");
        games.add("Karos Online");
        games.add("Lineage 2");
        games.add("Lineage 2 Classic");
        games.add("Lineage 2 Essence");
        games.add("Lost Ark");
        games.add("Neverwinter");
        games.add("Path of Exile");
        games.add("Perfect World");
        games.add("Ragnarok Online");
        games.add("Rocket League");
        games.add("Royal Quest");
        games.add("RuneScape");
        games.add("Stalcraft");
        games.add("Stalker Online");
        games.add("The Elder Scrolls Online");
        games.add("Trove");
        games.add("Warspear Online");
        games.add("World of Warcraft");
        games.add("World of Warcraft Classic");
        games.add("WoW Classic Burning Crusade");
        games.add("Аллоды Онлайн");
    }

    public void fillingDeliveries() {
        gameDeliveries.put("Aion RuOff", "Выкупить \"Тренировочный меч\" по цене заказа");
        gameDeliveries.put("Albion Online", "Обменяться в указанном городе, в банке в углу на W (по компасу)");
        gameDeliveries.put("Blade and Soul RuOff", "Выкупить \"Сущность бессмертного гандзи\" по цене заказа");
        gameDeliveries.put("Escape from Tarkov", "Выкупить \"Банка консервированной сайры\" по цене заказа");
        gameDeliveries.put("EVE Echoes", "Вам необходимо отправить заказ через частный контракт");
        gameDeliveries.put("EVE Online", "Вам необходимо найти и выкупить общедоступный контракт заказчика на сумму заказа");
        gameDeliveries.put("Fallout 76", "Вам необходимо отправить приглашение в друзья и группу, телепортироваться к заказчику, предложить обмен, и выкупить у него вещь по цене заказа");
        gameDeliveries.put("Final Fantasy XIV", "Обменяться в Таналане у эфирита");
        gameDeliveries.put("Karos Online", "Обменяться в городе Бернео возле телепорта Кристина");
        gameDeliveries.put("Neverwinter", "Выкупить \"Целебный эликсир Ледяной кузни\" по цене заказа");
        gameDeliveries.put("Path of Exile", "Пригласить заказчика в группу, переместиться к нему в убежище и там передать заказ");
        gameDeliveries.put("Perfect World", "Найти кота с ником заказчика в Городе оборотней у Старейшины Ущелья феникса и выкупить товар на сумму заказа");
        gameDeliveries.put("R2 Online", "Обменяться в деревне Эшборн возле алхимика Шарлин");
        gameDeliveries.put("Ragnarok Online", "Обменяться в Пронтере возле продавца зоотоваров");
        gameDeliveries.put("Revelation Online", "Обменяться в Астерионе возле аукциона");
        gameDeliveries.put("Rocket League", "Вам необходимо отправить приглашение в друзья и лобби. Предложить обмен и передать кредиты");
        gameDeliveries.put("RuneScape", "Обменяться в центре города Varrok");
        gameDeliveries.put("Stalker Online", "Обменяться в деревне Гурман рядом с медицинской палаткой");
        gameDeliveries.put("Tera Online", "В Велике у Платформы Пегаса");
        gameDeliveries.put("Trove", "Выкупить \"Ship: SS Dinghy\" по цене заказа");
        gameDeliveries.put("Warspear Online", "В Ирсельнорте в Броневом Утесе возле порта");
    }
}
