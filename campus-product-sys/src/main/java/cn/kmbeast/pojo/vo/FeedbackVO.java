package cn.kmbeast.pojo.vo;


import cn.kmbeast.pojo.entity.Feedback;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 前端展示的反馈信息
 */
@Data
public class FeedbackVO extends Feedback {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;

}
