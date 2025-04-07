package com.jsnjwj.facade.dto.draw;

import com.jsnjwj.facade.entity.SignSingleEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DrawUnit {

	protected Long orgId; // 组织ID

	protected List<SignSingleEntity> players; // 选手列表

}
