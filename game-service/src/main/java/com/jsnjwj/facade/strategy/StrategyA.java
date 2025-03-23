package com.jsnjwj.facade.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

/**
 * 去掉最高最低去平均
 */
public class StrategyA implements ScoreStrategy {

	@Override
	public BigDecimal getScore(List<BigDecimal> scores) {
		if (scores.size() <= 2)
			return BigDecimal.ZERO; // 不足以去掉最高最低
		BigDecimal max = Collections.max(scores);
		BigDecimal min = Collections.min(scores);
		BigDecimal sum = scores.stream()
			.filter(score -> !score.equals(max) && !score.equals(min))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return sum.divide(BigDecimal.valueOf(scores.size() - 2), RoundingMode.HALF_UP);
	}

}
