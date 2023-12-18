package ru.fatum.tgbot.logic;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.fatum.tgbot.telegram.TelegramBot;
import ru.fatum.tgbot.tglogic.KeyboardUtils;

public class MessageHandler {

    private final TelegramBot telegramBot;

    private enum BotState {
        REGULAR, W8START, MEMORGET, ASKTOTAKETEST, GIVEMEMSTORAGE, qFIRST;
    }

    static final String HELP_TEXT = "help text lol";
    private BotState currentState = BotState.W8START;
    private boolean isBotEnabled = false;

    public MessageHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Вместо напрямую обрабатывать логику в onUpdateReceived, вызываем метод обработки в MessageHandler
            processMessage(messageText, chatId);
        }
    }

    private void processMessage(String messageText, long chatId) {
        if (messageText.startsWith("/start")) {
            isBotEnabled = true;
            currentState = BotState.REGULAR;
        }

        if (!isBotEnabled){
            return;
        }

        if (messageText.startsWith("/help")) {
            KeyboardUtils.sendMessage(telegramBot,chatId, HELP_TEXT);
        }

        switch (currentState) {
            case ASKTOTAKETEST:
                if(messageText.startsWith("Yes | Take Test")){
                    KeyboardUtils.sendResponseWithKeyboard(telegramBot,chatId, "To Be or Not To Be?", KeyboardUtils.getKeyboardFor1q());
                    currentState = BotState.qFIRST;
                    break;
                }
                //ЕЩЁ ОДИН ИФ
                else {
                    KeyboardUtils.sendResponseWithKeyboard(telegramBot,chatId, "Would you like to take a test to determine your current mood?", KeyboardUtils.getKeyboardForAskTest());
                    return;
                }

            case REGULAR:
                if (messageText.startsWith("/savememory")){
                    KeyboardUtils.sendResponseWithKeyboard(telegramBot,chatId, "Would you like to take a test to determine your current mood?", KeyboardUtils.getKeyboardForAskTest());
                    currentState = BotState.ASKTOTAKETEST;
                    break;
                }
                if (messageText.startsWith("/memories")){
                    //Возможно в будующем можно будет сделать чтобы бот предлагал воспоминания по годам, когда их станет очень много, только как это сделать динамически я пока что не знаю
                    currentState = BotState.GIVEMEMSTORAGE;
                    break;
                }
                else{
                    KeyboardUtils.sendMessage(telegramBot,chatId,"This bot can help you to save your beautiful memories \n" +
                            "To control this bot you can use these commands:\n" +
                            "/savememory - to remember something important for you\n" +
                            "/memories - get your saved memories" );
                    return;
                }

            default:
                KeyboardUtils.sendMessage(telegramBot,chatId, "Please choose one of the options");
                break;
        }
    }
}
