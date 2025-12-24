package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 互动行为查询条件类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ComplainQueryDto extends QueryDto {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 商品ID
     */
    private Integer productId;
    /**
     * 订单号
     */
    private String code;
    /**
     * 管理员是否处理申诉
     */
    private Boolean adminComplainStatus;
    /**
     * 查询自己收到的订单的申诉
     */
    private List<Integer> productIds;
}
