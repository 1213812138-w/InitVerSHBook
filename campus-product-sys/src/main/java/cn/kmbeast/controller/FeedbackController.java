package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.FeedbackQueryDto;
import cn.kmbeast.pojo.entity.Feedback;
import cn.kmbeast.pojo.vo.FeedbackVO;
import cn.kmbeast.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    /**
     * 用户提交反馈
     *
     * @param feedback 参数
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/submit")
    @ResponseBody
    public Result<String> submit(@RequestBody Feedback feedback) {
        return feedbackService.submit(feedback);
    }
    /**
     * 管理员回复反馈
     */
    @PostMapping("/reply")
    public Result<String> replyFeedback(@RequestBody Feedback feedback) {
        Integer feedbackId = feedback.getId();
        return feedbackService.replyFeedback(feedback);
    }

    /**
     * 查询反馈信息
     *
     * @param feedbackQueryDto 查询参数
     * @return Result<List < FeedbackVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<FeedbackVO>> query(@RequestBody FeedbackQueryDto feedbackQueryDto) {
        return feedbackService.query(feedbackQueryDto);
    }
}

