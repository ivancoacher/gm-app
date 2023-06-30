package com.jsnjwj.compare.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TextInResponse {

	private Integer code;

	private String message;

	private String version;

	private String duration;

	private TextInfResult result;

	@Data
	public static class TextInfResult {

		private Integer angle;

		private Integer width;

		private Integer height;

		private List<ResultLines> lines;

		private List<ResultAreas> areas;

		private List<ResultTables> tables;

		@Data
		private static class ResultLines {

			private String text;

			private Integer score;

			private String type;

			private List<Integer> position;

			private Integer angle;

			private Integer direction;

			private Integer handwritten;

			private List<BigDecimal> char_scores;

			private List<List<Integer>> char_centers;

			private List<List<Integer>> char_positions;

			private List<List<String>> char_candidates;

			private List<List<List<BigDecimal>>> char_candidates_score;

			private Integer area_index;

			private String area_type;

		}

		@Data
		private static class ResultAreas {

		}

		@Data
		private static class ResultTables {

		}

	}

}
