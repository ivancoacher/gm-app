package com.jsnjwj.facade.strategy;

import java.math.BigDecimal;
import java.util.List;

/**
 * 得分求和
 */
public class StrategyD implements ScoreStrategy {

    @Override
    public BigDecimal getScore(List<BigDecimal> scores) {
        return scores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
