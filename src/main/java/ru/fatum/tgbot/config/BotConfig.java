package ru.fatum.tgbot.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@Getter
@PropertySource("application.properties")
public class BotConfig {
    public static final String botName = "meremain_bot";
    public static final String token = "6452054634:AAGmJo0h6IR8zhRWBanDw2PkyUjjvFgGCJE";
}
