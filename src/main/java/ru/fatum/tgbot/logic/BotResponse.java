package ru.fatum.tgbot.logic;


public class BotResponse {
    private final String responseText;
    private final String chatId;

    public BotResponse(String responseText, String chatId) {
        this.responseText = responseText;
        this.chatId = chatId;
    }

    public String getResponseText() {
        return responseText;
    }
    public String getChatId() {
        return chatId;
    }
}