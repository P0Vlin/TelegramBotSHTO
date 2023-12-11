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
    public static final String botName = "";
    public static final String token = "";
}
