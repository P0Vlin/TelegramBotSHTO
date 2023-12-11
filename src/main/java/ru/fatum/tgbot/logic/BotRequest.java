package ru.fatum.tgbot.logic;

public class BotRequest {
    private final String requestText;
    private final String chatId;

    public BotRequest(String requestText, String chatId) {

        this.requestText = requestText;
        this.chatId = chatId;
    }
    public String getRequestText() {
        return requestText;
    }
    public String getChatId() {
        return chatId;
    }
}