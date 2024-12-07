package com.jsnjwj.facade.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 得分去平均
 */
public class StrategyC implements ScoreStrategy {
    @Override
    public BigDecimal getScore(List<BigDecimal> scores) {
        if (scores.isEmpty()) return BigDecimal.ZERO; // 防止除零异常

        BigDecimal sum = scores.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(scores.size()), RoundingMode.HALF_UP);
    }
}
