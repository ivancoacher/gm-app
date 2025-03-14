package com.jsnjwj.facade.strategy;

import java.math.BigDecimal;
import java.util.List;

public interface ScoreStrategy {

	BigDecimal getScore(List<BigDecimal> scores);

}
