package com.jsnjwj.common.utils;

import java.util.Objects;

public class ThreadLocalUtil {

    private static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    private static final ThreadLocal<Long> gameThreadLocal = new ThreadLocal<>();

    public ThreadLocalUtil() {
        // TODO document why this constructor is empty
    }

    public static void addCurrentUser(Long userId) {
        userThreadLocal.set(userId);
    }

    public static void addCurrentGame(Long gameId) {
        gameThreadLocal.set(gameId);
    }

    public static Long getCurrentUserId() {
        Long currentUserId = userThreadLocal.get();
        return Objects.isNull(currentUserId) ? 0L : currentUserId;
    }

    public static Long getCurrentGameId() {
        Long currentGameId = gameThreadLocal.get();
        return Objects.isNull(currentGameId) ? 0L : currentGameId;
    }

    public static void remove() {
        userThreadLocal.remove();
        gameThreadLocal.remove();
    }

}
