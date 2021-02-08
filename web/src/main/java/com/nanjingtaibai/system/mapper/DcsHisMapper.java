package com.nanjingtaibai.system.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nanjingtaibai.system.entity.DcsHis;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zyz
 * @since 2020-12-18
 */
@DS("slave_3")
public interface DcsHisMapper extends BaseMapper<DcsHis> {


}
