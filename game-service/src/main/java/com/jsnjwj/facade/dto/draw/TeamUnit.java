package com.jsnjwj.facade.dto.draw;

import com.jsnjwj.facade.entity.SignSingleEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamUnit extends DrawUnit {

	private Long teamId;

	public TeamUnit(Long key, Long orgId, List<SignSingleEntity> value) {
		super();
	}

}
