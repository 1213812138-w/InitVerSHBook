package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.FeedbackQueryDto;
import cn.kmbeast.pojo.entity.Feedback;
import cn.kmbeast.pojo.entity.Feedback;
import cn.kmbeast.pojo.vo.FeedbackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品类别持久化接口
 */
public interface FeedbackMapper {

    /**
     * 新增
     *
     * @param feedback 实体信息
     * @return int 受影响行数
     */
    int submit(Feedback feedback);

    /**
     * 分页查询商品类别信息
     *
     * @param feedbackQueryDto 分页查询参数
     * @return List<Feedback>
     */
    List<FeedbackVO> query(FeedbackQueryDto feedbackQueryDto);

    /**
     * 查询满足分页查询的记录总数
     *
     * @param feedbackQueryDto 分页查询参数
     * @return int 数据总数
     */
    int queryCount(FeedbackQueryDto feedbackQueryDto);

    /**
     * 更新回复内容
     *
     * @param feedback 反馈信息
     * @return int 受影响行数
     */
    int updateReply(Feedback feedback);



}
