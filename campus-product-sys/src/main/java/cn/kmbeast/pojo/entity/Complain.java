package cn.kmbeast.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
申诉实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Complain {
    /**
     *
    ID
     */
    private Integer id;
    /**
     *
    关联的订单id
     */
   private Integer orderId;
    /**
     *
    图片列表
     */
    private String pictureList;
    /**
     * 用户说明详细内容
     */
    private String detail;
    /**
     * 管理员处理意见
     */
    private String adminComment;
    /**
     * 用户是否发起申诉
     */
    private Boolean customerComplainStatus;
    /**
     * 管理员是否已经处理申诉
     */
    private Boolean adminComplainStatus;
    /**
     * 申诉发起时间
     */
    private LocalDateTime createTime;
    /**
     * 申诉处理完成时间
     */
    private LocalDateTime processTime;
}

