package cn.kmbeast.pojo.dto.update;

import cn.kmbeast.pojo.entity.Complain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 申诉基础DTO类，用于接收前端创建/修改申诉的参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ComplainDTO extends Complain {
    // 仅继承Complain实体类字段，无额外查询字段，专注于申诉的创建/修改
}