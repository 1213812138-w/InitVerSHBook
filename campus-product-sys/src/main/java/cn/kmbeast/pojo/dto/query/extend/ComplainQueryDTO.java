package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 申诉的查询条件类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ComplainQueryDTO extends QueryDto {
    /**
     * 申诉主键ID
     */
    private Integer id;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 订单号（便于通过订单号关联查询申诉）
     */
    private String orderCode;

    /**
     * 用户是否发起申诉（true=已发起，false=未发起）
     */
    private Boolean customerComplainStatus;

    /**
     * 管理员是否已经处理申诉（true=已处理，false=未处理）
     */
    private Boolean adminComplainStatus;

    /**
     * 用户ID（查询指定用户的申诉）
     */
    private Integer userId;

    /**
     * 订单ID列表（批量查询多个订单的申诉）
     */
    private List<Integer> orderIds;
}