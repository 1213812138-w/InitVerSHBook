package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.Complain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * 申诉VO类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ComplainVO extends Complain {
    /**
     * 商家名
     */
    private String sellerName;

     /**
     * 管理员处理意见（新增）
     */
    private String adminComment;
    
    /**
     * 客户名
     */
    private String customerName;
    /**
     * 商品名
     */
    private String productName;
    /**
     * 金额
     */
    private BigDecimal totalPrice;
    /**
     * 新旧程度
     */
    private Integer oldLevel;
    /**
     * 类别
     */
    private String categoryName;
    /**
     * 书本的封面图片集
     */
    private String coverList;
    /**
     * 书本的单价
     */
    private BigDecimal price;
    /**
     * 订单编号
     */
    private String code;
}