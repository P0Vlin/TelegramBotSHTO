package ru.fatum.tgbot.telegram;
import ru.fatum.tgbot.logic.BotRequest;

import org.telegram.telegrambots.meta.api.objects.Update;


public class BotConverter {
    public BotRequest makeRequest(Update update){
        String message = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();

        return new BotRequest(message, chatId);
    }
}
