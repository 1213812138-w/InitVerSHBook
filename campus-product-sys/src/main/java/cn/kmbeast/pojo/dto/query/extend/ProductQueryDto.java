package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 商品的查询条件类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductQueryDto extends QueryDto {
    /**
     * 商品名
     */
    private String name;
    /**
     * 所属商品类别id
     */
    private Integer categoryId;
    /**
     * 用户Id  VO类 用到其他表的信息
     */
    private Integer userId;
    /**
     * 是否支持砍价
     */
    private Boolean isBargain;
    /**
     * 作者名
     */
    private String author;
    /**
     * 主题词
     */
    private String subject;
}
