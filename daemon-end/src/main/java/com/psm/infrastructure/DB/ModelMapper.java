package com.psm.infrastructure.DB;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.models_user.valueObject.Models_UserDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ModelMapper extends MPJBaseMapper<ModelDAO> {
    @Select("<script>" +
                "with ids as( " +
                    "select distinct tb_users.id as id, tb_users.create_time " +
                    "from tb_users " +
                    "<when test='userSelfId != null'> inner join tb_followers on tb_followers.tgt_user_id = tb_users.id </when>" +
                    "<when test='style != null or type != null'> " +
                        "inner join tb_models on tb_models.user_id = tb_users.id " +
                    "</when>" +
                    "where true " +
                    "<when test='userSelfId != null'> and tb_followers.src_user_id = #{userSelfId} </when>" +
                    "<when test='isIdle != null'> and tb_users.is_idle = #{isIdle} </when>" +
                    "<when test='canUrgent != null'> and tb_users.can_urgent = #{canUrgent} </when>" +
                    "<when test='style != null'> and tb_models.style = #{style} </when>" +
                    "<when test='type != null'> and tb_models.type = #{type} </when>" +
                    "order by create_time " +
                    "LIMIT #{size} OFFSET (#{current}-1) * #{size} " +
                ") "+

                "select tb_models.id, tb_models.user_id, tb_models.title, tb_models.cover, tb_models.style, tb_models.type, " +
                "tb_users.id, tb_users.name, tb_users.avatar, tb_users.sex, tb_users.public_model_num, tb_users.is_idle, tb_users.can_urgent, tb_users.create_time " +
                "from tb_users inner join tb_models on tb_models.user_id = tb_users.id " +
                "where tb_users.id in (select id from ids)" +
            "</script>")
    List<ModelDAO> selectModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type, Long userSelfId);
}
