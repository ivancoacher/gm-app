package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface CommonMapper<T> extends BaseMapper<T> {

	/**
	 * 全量插入,等价于insert
	 * @param entityList
	 * @return
	 */
	int insertBatchSomeColumn(List<T> entityList);

}
