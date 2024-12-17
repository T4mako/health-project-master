package com.keylab.healthproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keylab.healthproject.dao.PersonData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 *
 * mapper 接口
 *
 * @author T4mako
 * @date 2024/10/29 15:57
 */
@Mapper
public interface PersonDataMapper extends BaseMapper<PersonData> {
    @Select("select pd.*,LEFT(h.name, 2) as city  " +
            "from person_data pd " +
            "left join hospital h " +
            "on h.dept_id = " +
            "(select dep_id from community c where id = pd.dept_id)")
    List<Map<String, Object>> allUserBaseInfo();

    @Select("SELECT * FROM person_data WHERE dept_id = #{deptId}")
    List<PersonData> selectByDeptId(long deptId);
}
