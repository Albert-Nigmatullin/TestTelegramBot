package io.proj3ct.KazanBus.service;

import io.proj3ct.KazanBus.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken(){return config.getToken();}

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + ", please choose location!";
        log.info("Replied to user" + name);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        // Создаем 4 кнопки с различными текстами и ссылками
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // Кнопка 1
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Белинского");
        button1.setUrl("https://navi.kazantransport.ru/wap/online/?st_id=363");
        row1.add(button1);
        keyboard.add(row1);

        // Кнопка 2
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Набережная");
        button2.setUrl("https://navi.kazantransport.ru/wap/online/?st_id=208");
        row2.add(button2);
        keyboard.add(row2);

        // Кнопка 3
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Восстания (дом)");
        button3.setUrl("https://navi.kazantransport.ru/wap/online/?st_id=381");
        row3.add(button3);
        keyboard.add(row3);

        // Кнопка 4
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Восстания (работа)");
        button4.setUrl("https://navi.kazantransport.ru/wap/online/?st_id=121");
        row4.add(button4);
        keyboard.add(row4);

        // Кнопка 5
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("Ком. Здоровья");
        button5.setUrl("https://navi.kazantransport.ru/wap/online/?st_id=372");
        row5.add(button5);
        keyboard.add(row5);

        keyboardMarkup.setKeyboard(keyboard);

        sendMessageWithKeyboard(chatId, answer, keyboardMarkup);
    }

    private void sendMessageWithKeyboard(long chatId, String text, InlineKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText(text);
                message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message with keyboard: " + e.getMessage(), e);
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        }
        catch (TelegramApiException e) {
            log.error("Error sending simple message: " + e.getMessage(), e);
        }
    }
}


