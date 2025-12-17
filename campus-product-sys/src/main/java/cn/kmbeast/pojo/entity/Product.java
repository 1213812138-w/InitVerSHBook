package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
商品实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
        /**
         *
        ID
         */
        private Integer id;
       /**
       * 商品名
         */
        private String name;
        /**
         * 简介
         */
        private String detail;
    /**
     * 商品封面列表
     */
    private String coverList;
    /**
     作者
     */
    private String author;
    /**
     * 主题词
     */
    private String subject;
    /**
     * 新旧程度
     */
    private Integer oldLevel;
    /**
     * 所属商品类别id
     */
    private Integer categoryId;
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 库存
     */
    private Integer inventory;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 是否支持砍价
     */
    private Boolean isBargain;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
