package ru.fatum.tgbot.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.fatum.tgbot._interface.MessageHandler;
import ru.fatum.tgbot.config.BotConfig;
import ru.fatum.tgbot.logic.BotRequest;
//import ru.fatum.tgbot.model.User;
//import ru.fatum.tgbot.model.UserRepository;
import ru.fatum.tgbot.telegram.BotConverter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    //@Autowired
    //private UserRepository userRepository;

    private enum BotState {
        W8START,
        MEMORGET,
        ASKTOTAKETEST,
        GIVEMEMSTORAGE,
        FINISH
    }

    private BotState currentState = BotState.W8START;
    private boolean isBotEnabled = false;

    final BotConfig config;

    static final String HELP_TEXT = "help text lol";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List <BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "get a welcome message"));
        listofCommands.add(new BotCommand("/mydata", "check my your data"));
        try{
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e){

        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/start")) {
                isBotEnabled = true;

                currentState = BotState.MEMORGET;
                sendResponseWithKeyboard(chatId, "Выберите что вы хотите сделать" , getKeyboardForDef());
            }


            if (!isBotEnabled){
                return;
            }


            if (messageText.startsWith("/help")){
                sendMessage(chatId, HELP_TEXT);
            }


            switch (currentState) {
                case MEMORGET:
                    if (messageText.startsWith("Retain Memory")) {
                        sendResponseWithKeyboard(chatId, "Would you like to take a test to determine your current mood?", getKeyboardForAskTest());
                        currentState = BotState.ASKTOTAKETEST;
                        break;
                    }
                    else if (messageText.startsWith("Recall Memories")) {
                        currentState = BotState.GIVEMEMSTORAGE;
                        break;
                    }
                    break;

                case ASKTOTAKETEST:

                    break;

                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
                    break;
            }



        }



    }

    private ReplyKeyboardMarkup getKeyboardForAskTest() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Take Test");
        row.add("Write Memory");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup getKeyboardForDef() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Retain Memory");
        row.add("Recall Memories");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private void sendResponseWithKeyboard(long chatId, String responseText, ReplyKeyboardMarkup keyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(responseText);
        sendMessage.setReplyMarkup(keyboard);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    /*private void registerUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()){
            var chatId = message.getChatId();
            var chat = message.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            //log.info("user.saved: " + user);

        }
    }
*/

    private void sendMessage(long chatId, String textToSend)  {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        }
        catch (TelegramApiException e) {

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
