package com.example.currensy.Util;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class MessageUtil {
    public static CurrentMessage getMessage(Long chatId, String text) {
        CurrentMessage currentMessage = new CurrentMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);

        currentMessage.setSendMessage(sendMessage);
        currentMessage.setType(MessageType.SEND_MESSAGE);
        return currentMessage;
    }
}
