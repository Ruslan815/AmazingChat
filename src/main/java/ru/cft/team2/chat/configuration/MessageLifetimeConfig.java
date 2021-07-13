package ru.cft.team2.chat.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.cft.team2.chat.service.MessageService;

@Configuration
@EnableScheduling
public class MessageLifetimeConfig {

    private final MessageService messageService;

    public MessageLifetimeConfig(MessageService messageService) {
        this.messageService = messageService;
    }

    @Scheduled(fixedDelay = 1000)
    public void scheduledDatabaseUpdate() {
        messageService.deleteOldMessages();
    }
}
