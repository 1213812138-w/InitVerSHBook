package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.ComplainQueryDTO;
import cn.kmbeast.pojo.entity.Complain;
import cn.kmbeast.pojo.vo.ComplainVO;

import java.util.List;

/**
 * 申诉的业务逻辑接口
 */
public interface ComplainService {

    Result<String> save(Complain complain);

    Result<String> update(Complain complain);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<ComplainVO>> query(ComplainQueryDTO complainQueryDTO);

    Result<List<ComplainVO>> queryComplainList(ComplainQueryDTO complainQueryDTO);

    Result<String> handleComplain(Integer complainId, String adminComment);

    Result<List<ComplainVO>> queryByOrderIds(ComplainQueryDTO complainQueryDTO);

    // 新增：根据订单ID查询单个申诉
    Result<ComplainVO> queryByOrderId(Integer orderId);

    // 新增：根据ID查询申诉
    Result<ComplainVO> queryById(Integer id);

    // 新增：根据ID删除申诉
    Result<String> delete(Integer id);
}