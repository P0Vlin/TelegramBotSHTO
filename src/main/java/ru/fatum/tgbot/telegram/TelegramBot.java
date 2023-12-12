package ru.fatum.tgbot.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.fatum.tgbot.config.BotConfig;
//import ru.fatum.tgbot.model.User;
//import ru.fatum.tgbot.model.UserRepository;
import java.util.ArrayList;
import java.util.List;
import ru.fatum.tgbot.tglogic.KeyboardUtils;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    //@Autowired
    //private UserRepository userRepository;

    private enum BotState {
        REGULAR, W8START, MEMORGET, ASKTOTAKETEST, GIVEMEMSTORAGE, qFIRST;
    }
    private BotState currentState = BotState.W8START;
    private boolean isBotEnabled = false;
    final BotConfig config;
    static final String HELP_TEXT = "help text lol";

    public TelegramBot(BotConfig config) throws TelegramApiException {
        this.config = config;
        List <BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "launch the bot"));
        listofCommands.add(new BotCommand("/savememory", "save your memories"));
        listofCommands.add(new BotCommand("/memories", "get your saved memories"));
        this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/start")) {
                isBotEnabled = true;
                currentState = BotState.REGULAR;
            }

            if (!isBotEnabled){
                return;
            }

            if (messageText.startsWith("/help")) {
                KeyboardUtils.sendMessage(this,chatId, HELP_TEXT);
            }

            switch (currentState) {
                /*case MEMORGET:
                    if (messageText.startsWith("Retain Memory")) {
                        sendResponseWithKeyboard(chatId, "Would you like to take a test to determine your current mood?", getKeyboardForAskTest());
                        currentState = BotState.ASKTOTAKETEST;
                        break;
                    }
                    else if (messageText.startsWith("Recall Memories")) {
                        currentState = BotState.GIVEMEMSTORAGE;
                        break;
                    }
                    else{
                        sendResponseWithKeyboard(chatId, "What do you want to do?" , getKeyboardForDef());
                        currentState = BotState.MEMORGET;
                        break;
                    }*/

                case ASKTOTAKETEST:
                    if(messageText.startsWith("Yes | Take Test")){
                        KeyboardUtils.sendResponseWithKeyboard(this,chatId, "To Be or Not To Be?", KeyboardUtils.getKeyboardFor1q());
                        currentState = BotState.qFIRST;
                        break;
                    }
                    //ЕЩЁ ОДИН ИФ
                    else {
                        KeyboardUtils.sendResponseWithKeyboard(this,chatId, "Would you like to take a test to determine your current mood?", KeyboardUtils.getKeyboardForAskTest());
                        return;
                    }

                case REGULAR:
                    if (messageText.startsWith("/savememory")){
                        KeyboardUtils.sendResponseWithKeyboard(this,chatId, "Would you like to take a test to determine your current mood?", KeyboardUtils.getKeyboardForAskTest());
                        currentState = BotState.ASKTOTAKETEST;
                        break;
                    }
                    if (messageText.startsWith("/memories")){
                        //Возможно в будующем можно будет сделать чтобы бот предлагал воспоминания по годам, когда их станет очень много, только как это сделать динамически я пока что не знаю
                        currentState = BotState.GIVEMEMSTORAGE;
                        break;
                    }
                    else{
                        KeyboardUtils.sendMessage(this,chatId,"This bot can help you to save your beautiful memories \n" +
                                "To control this bot you can use these commands:\n" +
                                "/savememory - to remember something important for you\n" +
                                "/memories - get your saved memories" );
                        return;
                    }

                default:
                    KeyboardUtils.sendMessage(this,chatId, "Please choose one of the options");
                    break;
            }
        }
    }
    @Override
    public String getBotUsername() {
        return BotConfig.botName;
    }
    @Override
    public String getBotToken(){
        return BotConfig.token;
    }
}
