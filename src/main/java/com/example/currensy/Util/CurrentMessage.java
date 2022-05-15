package com.example.currensy.Util;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

@Getter
@Setter
public class CurrentMessage {
    private SendMessage sendMessage;
    private SendPhoto sendPhoto;

    private MessageType type;
}
