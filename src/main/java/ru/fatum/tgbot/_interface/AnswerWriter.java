package ru.fatum.tgbot._interface;

import ru.fatum.tgbot.logic.BotResponse;

public interface AnswerWriter {
    void writeAnswer(BotResponse response);
}