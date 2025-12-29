package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    /**
     *反馈编号
     */
    private Integer id;
    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 反馈标题
     */
    private String title ;

    /**
     * 反馈内容
     */
    private String  content ;


    /**
     * 图片地址列表
     */
    private String imageList ;

    /**
     * 回复内容
     */
    private String replyContent ;

    /**
     * 回复状态
     */
    private Integer isReply=0 ;

    /**
     * 反馈类型
     */
    private String type ;
    /**
     * 反馈创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 回复时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime replyTime;
}
