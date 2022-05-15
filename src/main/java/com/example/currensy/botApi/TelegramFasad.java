package com.example.currensy.botApi;

import com.example.currensy.Util.CurrentMessage;
import com.example.currensy.Util.MessageUtil;
import com.example.currensy.entity.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

@Service
public class TelegramFasad {
    private List<Currency> currencyList;

    public CurrentMessage handleUpdate(Update update) {
        CurrentMessage ans = new CurrentMessage();
        if (update.hasMessage()) {
            return handleMessage(update.getMessage());
        }
        ans = MessageUtil.getMessage(update.getMessage().getChatId(), "mistake");
        return ans;
    }

    private CurrentMessage handleMessage(Message message) {
        String input = "";

        if (message.hasText()) {
            input = message.getText();
        }

        if (input.equals("/start")) {
            return MessageUtil.getMessage(message.getChatId(), "Hush kelibsiz");
        }

        if (input.equals("/course")) {
            return getCurrentCourrency(message);
        }

        return handCalculate(message);
    }

    private CurrentMessage getCurrentCourrency(Message message) {
        currencyList = new LinkedList<>();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.uznews.uz/api/v1/main/currencies";


        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode result = mapper.readTree(response.getBody()).get("result");
            JsonNode currencies = result.get("currencies");

            for (int i = 0; i < currencies.size(); i++) {
                ObjectNode jsonNodes = (ObjectNode) currencies.get(i);
                Currency currency = new Currency();
                currency.setName(String.valueOf(jsonNodes.get("Ccy")));
                String rate = String.valueOf(jsonNodes.get("Rate"));
                rate = rate.substring(1, rate.length() - 1);
                currency.setRate(new BigDecimal(rate));
                String diff = String.valueOf(jsonNodes.get("Diff"));
                diff = diff.substring(1, diff.length() - 1);
                currency.setDiff(new BigDecimal(diff));
                String name_uz = String.valueOf(jsonNodes.get("CcyNm_UZ"));
                name_uz = name_uz.substring(1, name_uz.length()-1);
                currency.setName_uz(name_uz);
                String name_ru = String.valueOf(jsonNodes.get("CcyNm_RU"));
                name_ru = name_ru.substring(1, name_ru.length()-1);
                currency.setName_ru(name_ru);
                String name_usd = String.valueOf(jsonNodes.get("CcyNm_UZC"));
                name_usd = name_usd.substring(1,name_usd.length()-1);
                currency.setName_uzs(name_usd);
                currencyList.add(currency);
            }
        } catch (JsonProcessingException e) {
            return MessageUtil.getMessage(message.getChatId(), "xatolik yuz berdi \n " +
                    "tez orada bartaraf eamiz");
        }

        StringBuilder responseText = new StringBuilder();
        responseText.append("xozirgi kurs");
        for (Currency c : currencyList) {
            responseText.append("\n" + c.getName_uzs()).append(" (").append(c.getName()).append(")\n");

            responseText.append("1 " + c.getName()).append("  ").append(c.getRate()).append(" Uzs");
            responseText.append("\n");

        }


        return MessageUtil.getMessage(message.getChatId(), responseText.toString());
    }


    private CurrentMessage handCalculate(Message message) {
        try {
            Integer number = Integer.valueOf(message.getText());
            // TODO: conversation
            StringBuilder responseText = new StringBuilder();
            for (Currency c : currencyList) {
                responseText.append("\n" + c.getName_uzs()).append(" (").append(c.getName()).append(")\n");
                // TODO divide Uzs to 1 c.getRate()
                BigDecimal s = new BigDecimal(number).divide(c.getRate(), 3 , RoundingMode.FLOOR);
                responseText.append(number + " ").append("Uzs  ").append(s).append(" " + c.getName());
                responseText.append("\n");
            }
           return MessageUtil.getMessage(message.getChatId(), responseText.toString());
        } catch (Exception e) {
            return MessageUtil.getMessage(message.getChatId(), "buyruq topilmadi");
        }
    }


}
