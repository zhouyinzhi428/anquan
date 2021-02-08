package com.nanjingtaibai.system.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nanjingtaibai.system.entity.Mcgs;
import com.nanjingtaibai.system.entity.Yonghu;
import org.apache.ibatis.annotations.Select;


/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zyz
 * @since 2020-12-18
 */
@DS("slave_4")
public interface McgsMapper extends BaseMapper<Mcgs> {
    @Select("select top 1 * from shuju_MCGS order by MCGS_Time desc")
    Mcgs getData();

}
