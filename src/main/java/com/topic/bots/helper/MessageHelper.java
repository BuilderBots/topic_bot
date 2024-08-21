package com.topic.bots.helper;

import cn.hutool.core.util.StrUtil;
import com.topic.bots.database.entity.Sys;
import com.topic.bots.listener.AsyncBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author zyred
 * @since v 0.0.1
 */
public class MessageHelper {

    public static SendMessage commonAsync (Message message, Long userId, Integer topicId, String text, String parseMode) {
        if (message.hasText()) {
            return SendMessage.builder()
                    .chatId(userId)
                    .text(StrUtil.isNotBlank(text) ? text : message.getText())
                    .messageThreadId(topicId)
                    .parseMode(parseMode)
                    .disableWebPagePreview(true)
                    .build();
        }
        if (message.hasAnimation()) {
            AsyncBotApiMethod.addMessage(
                    SendAnimation.builder()
                            .animation(new InputFile(message.getAnimation().getFileId()))
                            .chatId(userId)
                            .caption(StrUtil.isNotBlank(text) ? text : message.getCaption())
                            .messageThreadId(topicId)
                            .parseMode(parseMode)
                            .build()
            );
            return null;
        }
        if (message.hasAudio()) {
            AsyncBotApiMethod.addMessage(
                    SendAudio.builder()
                            .audio(new InputFile(message.getAudio().getFileId()))
                            .caption(StrUtil.isNotBlank(text) ? text : message.getCaption())
                            .chatId(userId)
                            .messageThreadId(topicId)
                            .parseMode(parseMode)
                            .build()
            );
            return null;
        }
        if (message.hasDice()) {
            AsyncBotApiMethod.addMessage(
                    SendDice.builder()
                            .emoji(message.getDice().getEmoji())
                            .chatId(userId)
                            .messageThreadId(topicId)
                            .build()
            );
            return null;
        }
        if (message.hasDocument()) {
            AsyncBotApiMethod.addMessage(
                    SendDocument.builder()
                            .document(new InputFile(message.getDocument().getFileId()))
                            .chatId(userId)
                            .caption(StrUtil.isNotBlank(text) ? text : message.getCaption())
                            .messageThreadId(topicId)
                            .parseMode(parseMode)
                            .build()
            );
            return null;
        }
        if (message.hasVideo()) {
            AsyncBotApiMethod.addMessage(
                    SendVideo.builder()
                            .video(new InputFile(message.getVideo().getFileId()))
                            .chatId(userId)
                            .caption(StrUtil.isNotBlank(text) ? text : message.getCaption())
                            .messageThreadId(topicId)
                            .parseMode(parseMode)
                            .build()
            );
            return null;
        }
        if (message.hasPhoto()) {
            // 取最大的一张图片进行转发
            List<PhotoSize> photos = message.getPhoto();
            PhotoSize photo = photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(photos.get(0));

            AsyncBotApiMethod.addMessage(
                    SendPhoto.builder()
                            .photo(new InputFile(photo.getFileId()))
                            .caption(StrUtil.isNotBlank(text) ? text : message.getCaption())
                            .chatId(userId)
                            .messageThreadId(topicId)
                            .parseMode(parseMode)
                            .build()
            );
            return null;
        }
        if (message.hasSticker()) {
            Sticker sticker = message.getSticker();
            if (Boolean.TRUE.equals(sticker.getIsAnimated())) {
                AsyncBotApiMethod.addMessage(
                        SendAnimation.builder()
                                .animation(new InputFile(sticker.getFileId()))
                                .chatId(userId)
                                .messageThreadId(topicId)
                                .build()
                );
            } else if (Boolean.TRUE.equals(sticker.getIsVideo())){
                AsyncBotApiMethod.addMessage(
                        SendVideo.builder()
                                .video(new InputFile(sticker.getFileId()))
                                .chatId(userId)
                                .messageThreadId(topicId)
                                .build()
                );
            }
            return null;
        }
        return null;
    }

}
