package ru.fatum.tgbot._interface;

import ru.fatum.tgbot.logic.BotRequest;

public interface MessageHandler {
    void handle(BotRequest request, AnswerWriter writer);
}