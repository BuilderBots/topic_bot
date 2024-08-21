package com.topic.bots.handlers.group;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.topic.bots.common.Commands;
import com.topic.bots.common.CommonCache;
import com.topic.bots.common.KeyboardCommon;
import com.topic.bots.config.BotProperties;
import com.topic.bots.database.entity.Keywords;
import com.topic.bots.database.entity.Sys;
import com.topic.bots.database.service.KeywordsService;
import com.topic.bots.database.service.SystemService;
import com.topic.bots.handlers.AbstractHandler;
import com.topic.bots.handlers.InitHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *      后台群命令设置
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Component
public class BackgroundHandler extends AbstractHandler {

    @Resource private InitHandler initHandler;
    @Resource private BotProperties properties;
    @Resource private SystemService systemService;
    @Resource private KeywordsService keywordsService;

    @Override
    protected boolean support(Update update) {
        return update.hasMessage()
                && this.properties.background(update.getMessage().getChatId());
    }

    @Override
    protected BotApiMethod<?> execute(Update update) {
        Message message = update.getMessage();
        List<String> commands = StrUtil.split(message.getText(), Commands.WILL);

        if (StrUtil.equals(commands.get(0), "设置")) {
            return this.processorSetting(commands, message);
        }

        // 刷新缓存
        if (StrUtil.equals(message.getText(), "flush") && commands.size() == 1) {
            this.initHandler.flush();
            return ok(message, "缓存已刷新");
        }

        // 关键词添加#关键词#内容
        if (StrUtil.startWith(commands.get(0), "关键词添加") && commands.size() == 3) {
            long count = this.keywordsService.count(
                    Wrappers.<Keywords>lambdaQuery()
                            .eq(Keywords::getKeyword, commands.get(1))
            );

            if (count > 0) {
                return fail(message, "关键词已存在");
            }

            Keywords keywords = new Keywords();
            keywords.setKeyword(commands.get(1));
            keywords.setContent(commands.get(2));
            this.keywordsService.save(keywords);

            CommonCache.setKeywords(keywords.getKeyword(), keywords.getContent());
            return ok(message);
        }

        // 关键词删除#关键词#内容
        if (StrUtil.startWith(commands.get(0), "关键词删除")  && commands.size() == 2) {
            boolean remove = this.keywordsService.remove(
                    Wrappers.<Keywords>lambdaQuery()
                            .eq(Keywords::getKeyword, commands.get(1))
            );
            if (!remove) {
                return fail(message, "关键词不存在");
            }

            CommonCache.removeKeywords(commands.get(1));
            return ok(message);
        }

        return null;
    }

    private BotApiMethod<?> processorSetting(List<String> commands, Message message) {
        Sys sys = systemService.selectSystem();
        Sys update = new Sys(sys.getBackgroundId());

        // 设置#欢迎语#xxxxx
        if (StrUtil.equals(commands.get(1), "欢迎语")) {
            update.setGreetings(commands.get(2));
        }
        // 设置#键盘#按钮1,按钮2\n按钮3,按钮4
        if (StrUtil.equals(commands.get(1), "键盘")) {
            ReplyKeyboardMarkup keyboard = KeyboardCommon.parseKeyboard(commands.get(2));
            String json = KeyboardCommon.toJson(keyboard);
            update.setReplyKeyboard(json);
        }
        // 设置#封号#❌抱歉，您已被封号
        if (StrUtil.equals(commands.get(1), "封号")) {
            update.setBannedText(commands.get(2));
        }
        // 设置#客服群#-1002150462985
        if (StrUtil.equals(commands.get(1), "客服群")) {
            long forwardId = Long.parseLong(commands.get(2));
            if (forwardId > 0) {
                return fail(message, "请检查客服群ID是否正确");
            }
            update.setForwardId(forwardId);
        }
        // 设置#结束语#本次服务已经结束，欢迎下次光临
        if (StrUtil.equals(commands.get(1), "结束语")) {
            update.setFinishText(commands.get(2));
        }
        // 设置#话题#客户 | %nickname% | %userId% | 新对话
        if (StrUtil.equals(commands.get(1), "话题")) {
            update.setTopicName(commands.get(2));
        }
        // 设置#消息头#10086人工客服
        if (StrUtil.equals(commands.get(1), "消息头")) {
            update.setMessageTitle("*" + commands.get(2) + "*");
        }
        // 设置#小尾巴#技术支持来自@developrobots
        if (StrUtil.equals(commands.get(1), "小尾巴")) {
            update.setMessageTail("_" + commands.get(2) + "_");
        }
        // 设置#话题复用#开启
        if (StrUtil.equals(commands.get(1), "话题复用")) {
            boolean enable = StrUtil.equals(commands.get(2), "开启");
            update.setTopicReuse(enable);
        }
        this.systemService.updateAny(update);
        return ok(message);
    }
}
