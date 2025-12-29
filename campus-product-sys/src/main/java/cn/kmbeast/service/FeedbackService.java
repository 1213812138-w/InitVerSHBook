package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.FeedbackQueryDto;
import cn.kmbeast.pojo.dto.query.extend.ProductQueryDto;
import cn.kmbeast.pojo.dto.update.OrdersDTO;
import cn.kmbeast.pojo.entity.Feedback;
import cn.kmbeast.pojo.entity.Product;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.FeedbackVO;
import cn.kmbeast.pojo.vo.OrdersDeliverDto;
import cn.kmbeast.pojo.vo.ProductVO;

import java.util.List;

/**
 * 反馈的业务逻辑接口
 */
public interface FeedbackService {

    Result<String> submit(Feedback feedback);
    Result<List<FeedbackVO>> query(FeedbackQueryDto feedbackQueryDto);
    Result<String> replyFeedback(Feedback feedback);
}
