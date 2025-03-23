package com.jsnjwj.facade.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 去掉两个最高最低取平均
 */
public class StrategyE implements ScoreStrategy {

	@Override
	public BigDecimal getScore(List<BigDecimal> scores) {
		if (scores.size() <= 4)
			return BigDecimal.ZERO; // 不足以去掉两个最高和最低

		List<BigDecimal> sortedScores = scores.stream().sorted().collect(Collectors.toList());

		// 去掉两个最高和两个最低
		List<BigDecimal> trimmedScores = sortedScores.subList(2, sortedScores.size() - 2);

		BigDecimal sum = trimmedScores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		return sum.divide(BigDecimal.valueOf(trimmedScores.size()), RoundingMode.HALF_UP);
	}

}
