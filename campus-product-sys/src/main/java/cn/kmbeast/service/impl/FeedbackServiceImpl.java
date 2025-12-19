package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.InteractionMapper;
import cn.kmbeast.mapper.OrdersMapper;
import cn.kmbeast.mapper.FeedbackMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.OrdersQueryDto;
import cn.kmbeast.pojo.dto.query.extend.FeedbackQueryDto;
import cn.kmbeast.pojo.dto.update.OrdersDTO;
import cn.kmbeast.pojo.em.InteractionEnum;
import cn.kmbeast.pojo.entity.Interaction;
import cn.kmbeast.pojo.entity.Orders;
import cn.kmbeast.pojo.entity.Feedback;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.OrdersDeliverDto;
import cn.kmbeast.pojo.vo.OrdersVO;
import cn.kmbeast.pojo.vo.FeedbackVO;
import cn.kmbeast.service.FeedbackService;
import cn.kmbeast.service.FeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品类别业务逻辑接口实现类
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;


    /**
     * 提交反馈
     *
     * @param feedback 参数
     * @return Result<String> 后台通用返回封装类
     */
    @Override
    public Result<String> submit(Feedback feedback) {
        if (!StringUtils.hasText(feedback.getTitle())) {
            return ApiResult.error("标题不能为空");
        }
        if (!StringUtils.hasText(feedback.getImageList())) {
            return ApiResult.error("请上传反馈图片");
        }
        if (!StringUtils.hasText(feedback.getContent())){
            return ApiResult.error("请填写反馈内容");
        }
        feedback.setIsReply(0);
        feedback.setUserId(LocalThreadHolder.getUserId());
        feedback.setCreateTime(LocalDateTime.now());
        feedback.setReplyTime(null);
        feedbackMapper.submit(feedback);
        return ApiResult.success("反馈新增成功");
    }



    /**
     * 查询
     *
     * @param feedbackQueryDto 查询参数
     * @return Result<List < FeedbackVO>> 后台通用返回封装类
     */
    @Override
    public Result<List<FeedbackVO>> query(FeedbackQueryDto feedbackQueryDto) {
        int totalCount = feedbackMapper.queryCount(feedbackQueryDto);
        List<FeedbackVO> feedbackVOList = feedbackMapper.query(feedbackQueryDto);
        return ApiResult.success(feedbackVOList, totalCount);
    }



    /**
     * 回复反馈
     *
     * @param feedback 参数
     * @return Result<List < ChartVO>> 响应结果
     */
    @Override
    public Result<String> replyFeedback(Feedback feedback)
    {
        System.out.print(feedback.getReplyContent());
        if(feedback.getIsReply() == 1)
        {

            return ApiResult.error("请勿重复回复");
        }
        feedback.setIsReply(1);
        feedback.setReplyTime(LocalDateTime.now());
        feedbackMapper.updateReply(feedback);
        return ApiResult.success("反馈回复成功");
    }




}
