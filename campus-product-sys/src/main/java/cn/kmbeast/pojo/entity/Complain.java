package cn.kmbeast.pojo.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 申诉实体类，对应数据库complain表
 */
@Data
public class Complain implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申诉主键id
     */
    private Integer id;

    /**
     * 订单id（数据库注释标注为"卖家id"，按字段名order_id修正）
     */
    private Integer orderId;
    /**
     * 图片列表（建议存储JSON格式的图片URL数组）
     */
    private String pictureList;

    /**
     * 申诉信息描述
     */
    private String detail;

    /**
     * 管理员处理意见
     */
    private String adminComment;

    /**
     * 用户是否发起申诉（1=是，0=否）
     */
    private Boolean customerComplainStatus;

    /**
     * 管理员是否已经处理申诉（1=已处理，0=未处理）
     */
    private Boolean adminComplainStatus;

    /**
     * 发起申诉时间
     */
    private Date createTime;

    /**
     * 管理员处理时间
     */
    private Date processTime;
}