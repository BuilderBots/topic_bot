package com.topic.bots.common;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
public class KeyboardCommon {

    private static final ObjectMapper OBJECT_MAPPER = new com.fasterxml.jackson.databind.ObjectMapper ();

    /**
     * 按钮1,按钮2\n按钮3,按钮4
     * \n： 换行，键盘换行
     *
     * @param replyKeyboard 按钮1,按钮2\n按钮3,按钮4
     * @return              键盘
     */
    public static ReplyKeyboardMarkup parseKeyboard(String replyKeyboard) {
        if (StrUtil.isBlank(replyKeyboard)) {
            return null;
        }
        List<String> lines = StrUtil.splitTrim(replyKeyboard, "\\n");
        List<List<String>> buttons = new ArrayList<>(lines.size());
        for (String line : lines) {
            List<String> button = StrUtil.split(line, ",");
            buttons.add(button);
        }
        return KeyboardCommon.parseKeyboard(buttons);
    }


    /**
     * 键盘解析为 json
     *
     * @param markup 键盘
     * @return       {"keyboard":[[{"text":"one"},{"text":"two"},{"text":"three"}],[{"text":"four"},{"text":"five"},{"text":"six"}]],"resize_keyboard":true}
     */
    @SneakyThrows
    public static String toJson (ReplyKeyboardMarkup markup) {
        if (markup == null) {
            return null;
        }
        return OBJECT_MAPPER.writeValueAsString(markup);
    }

    /**
     * json 解析为键盘
     * {"keyboard":[[{"text":"one"},{"text":"two"},{"text":"three"}],[{"text":"four"},{"text":"five"},{"text":"six"}]],"resize_keyboard":true}
     *
     * @param replyKeyboardJson json
     * @return                  键盘
     */
    @SneakyThrows
    public static ReplyKeyboardMarkup parseJson (String replyKeyboardJson) {
        if (StrUtil.isBlank(replyKeyboardJson)) {
            return null;
        }
        JavaType responseType = OBJECT_MAPPER.getTypeFactory().constructType(ReplyKeyboardMarkup.class);
        return OBJECT_MAPPER.readValue(replyKeyboardJson, responseType);
    }


    private static ReplyKeyboardMarkup parseKeyboard(List<List<String>> buttons) {
        List<KeyboardRow> rows = new ArrayList<>();
        for (List<String> buttonName : buttons) {
            KeyboardRow row = new KeyboardRow();
            for (String name : buttonName) {
                row.add(KeyboardButton.builder().text(name).build());
            }
            rows.add(row);
        }
        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .resizeKeyboard(true)
                .build();
    }
}
