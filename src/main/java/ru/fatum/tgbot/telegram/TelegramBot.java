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
import ru.fatum.tgbot.logic.MessageHandler;

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
    private final MessageHandler messageHandler;

    public TelegramBot(BotConfig config) throws TelegramApiException {
        this.messageHandler = new MessageHandler(this);
        this.config = config;
        List <BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "launch the bot"));
        listofCommands.add(new BotCommand("/savememory", "save your memories"));
        listofCommands.add(new BotCommand("/memories", "get your saved memories"));
        this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
    }

    @Override
    public void onUpdateReceived(Update update) {
        messageHandler.handleUpdate(update);
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
