package com.example.currensy;

import com.example.currensy.Util.CurrentMessage;
import com.example.currensy.Util.MessageType;
import com.example.currensy.botApi.TelegramFasad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class CurrencyBot extends TelegramLongPollingBot {
    private String token = "token";
    private String username = "username";
    @Autowired
    TelegramFasad telegramFasad;
    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        CurrentMessage currentMessage = telegramFasad.handleUpdate(update);

        if(currentMessage != null && currentMessage.getType() != null) {
            executeMessage(currentMessage);
        }
    }

    private void executeMessage(CurrentMessage currentMessage) {
        MessageType type = currentMessage.getType();
        try {
            if(type.equals(MessageType.SEND_MESSAGE)) {
                execute(currentMessage.getSendMessage());
            }

            if(type.equals(MessageType.SEND_PHOTO)) {
                execute(currentMessage.getSendPhoto());
            }
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
