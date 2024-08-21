package com.topic.bots.common;

import com.topic.bots.database.entity.Sys;
import com.topic.bots.filters.NullMessage;

import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
public class CommonCache {

    public static final NullMessage NULL_MESSAGE = new NullMessage();


    private static Sys system;

    public static void setSystem(Sys system) {
        CommonCache.system = system;
    }

    public static Sys getSystem() {
        return CommonCache.system;
    }





    private static final Map<Long, Integer> USER_TOPIC_ID = new HashMap<>(128);
    private static final Map<Integer, Long> TOPIC_USER_ID = new HashMap<>(128);

    public static void setUserTopicId(Long userId, Integer topicId) {
        USER_TOPIC_ID.put(userId, topicId);
        TOPIC_USER_ID.put(topicId, userId);
    }

    public static Integer getTopicId(Long userId) {
        return USER_TOPIC_ID.get(userId);
    }

    public static Long getUserId(Integer topicId) {
        return TOPIC_USER_ID.get(topicId);
    }

    public static void removeTopic(Long userId) {
        Integer topicId = USER_TOPIC_ID.remove(userId);
        if (Objects.nonNull(topicId)) {
            TOPIC_USER_ID.remove(topicId);
        }
    }




    private static final Set<Long> BANNED_USERS = new HashSet<>(128);
    public static void banned (Long userId) {
        BANNED_USERS.add(userId);
    }
    public static boolean hasBanned (Long userId) {
        return BANNED_USERS.contains(userId);
    }

    public static void unbanned (Long userId) {
        BANNED_USERS.remove(userId);
    }






    private static final Map<String, String> KEYWORDS = new HashMap<>(128);

    public static void setKeywords(String keyword, String content) {
        KEYWORDS.put(keyword, content);
    }
    public static String getKeywords(String keyword) {
        return KEYWORDS.get(keyword);
    }
    public static void removeKeywords(String keyword) {
        KEYWORDS.remove(keyword);
    }





    public static void flush() {
        USER_TOPIC_ID.clear();
        TOPIC_USER_ID.clear();
        BANNED_USERS.clear();
        KEYWORDS.clear();
    }
}
