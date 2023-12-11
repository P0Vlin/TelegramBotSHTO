package ru.fatum.tgbot.logic;

import ru.fatum.tgbot._interface.AnswerWriter;
import ru.fatum.tgbot._interface.MessageHandler;

public class EchoMessageHandler implements MessageHandler {
    @Override
    public void handle(BotRequest request, AnswerWriter writer) {
        String userMessage = request.getRequestText();
        String chatId = request.getChatId();
        BotResponse response = new BotResponse(userMessage, chatId);
        writer.writeAnswer(response);
    }
}