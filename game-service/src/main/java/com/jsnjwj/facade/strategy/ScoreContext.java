package com.jsnjwj.facade.strategy;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ScoreContext {

	private ScoreStrategy strategy;

	public void setStrategy(ScoreStrategy strategy) {
		this.strategy = strategy;
	}

	public BigDecimal executeStrategy(List<BigDecimal> scores) {
		return strategy.getScore(scores);
	}

}
