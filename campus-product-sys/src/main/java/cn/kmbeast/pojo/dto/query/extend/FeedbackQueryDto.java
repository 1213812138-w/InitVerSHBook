package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品的查询条件类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FeedbackQueryDto extends QueryDto {


    private Integer userId;     // 用户编号
    private String type;        // 反馈类型（可选）
    private Integer isReply;    // 是否已回复（0/1，可选）

}
