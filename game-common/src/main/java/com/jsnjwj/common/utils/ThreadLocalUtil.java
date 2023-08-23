package com.jsnjwj.common.utils;

import java.util.Objects;

public class ThreadLocalUtil {
    private static final ThreadLocal<Long> userThreadLocal = new ThreadLocal();
    private static final ThreadLocal<Long> companyThreadLocal = new ThreadLocal();

    public ThreadLocalUtil() {
    }

    public static void addCurrentUser(Long userId) {
        userThreadLocal.set(userId);
    }

    public static void addCurrentCompany(Long companyId) {
        companyThreadLocal.set(companyId);
    }


    public static Long getCurrentUserId() {
        Long currentUserId = userThreadLocal.get();
        return Objects.isNull(currentUserId) ? 0L : currentUserId;
    }

    public static Long getCurrentCompanyId() {
        if (getCurrentUserId() <= 0L) {
            return 0L;
        } else {
            Long currentCompanyId = (Long) companyThreadLocal.get();
            return Objects.isNull(currentCompanyId) ? 0L : currentCompanyId;
        }
    }

    public static void remove() {
        userThreadLocal.remove();
        companyThreadLocal.remove();
    }
}
