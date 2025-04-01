package com.jsnjwj.facade.dto.draw;

import com.jsnjwj.facade.entity.SignSingleEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualUnit extends DrawUnit{
    private Long playerId;

    public IndividualUnit(SignSingleEntity player) {
        super();
    }
}
