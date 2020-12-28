package org.budgetmanager;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public BudgetManagerTelegramBot budgetManagerTelegramBot(TelegramFacade telegramFacade) {

        BudgetManagerTelegramBot budgetManagerTelegramBot = new BudgetManagerTelegramBot(telegramFacade);
        budgetManagerTelegramBot.setBotUserName(botUserName);
        budgetManagerTelegramBot.setBotToken(botToken);
        budgetManagerTelegramBot.setWebHookPath(webHookPath);

        return budgetManagerTelegramBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
