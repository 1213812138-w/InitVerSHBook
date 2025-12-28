package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.ComplainMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.ComplainQueryDTO;
import cn.kmbeast.pojo.dto.query.extend.OrdersQueryDto;
import cn.kmbeast.pojo.entity.Complain;
import cn.kmbeast.pojo.vo.ComplainVO;
import cn.kmbeast.pojo.vo.OrdersVO;
import cn.kmbeast.service.ComplainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ComplainServiceImpl implements ComplainService {

    @Resource
    private ComplainMapper complainMapper;

    private final static Boolean COMPLAIN_UNHANDLED = false;
    private final static Boolean COMPLAIN_HANDLED = true;

    @Override
    @Transactional
    public Result<String> save(Complain complain) {
        if (complain.getOrderId() == null) {
            return ApiResult.error("订单ID不能为空");
        }
        if (complain.getDetail() == null || complain.getDetail().trim().isEmpty()) {
            return ApiResult.error("申诉理由不能为空");
        }

        // 检查是否已存在申诉
        ComplainVO existing = complainMapper.queryByOrderId(complain.getOrderId());
        if (existing != null) {
            return ApiResult.error("该订单已发起过申诉，请勿重复提交");
        }

        createComplainInfo(complain);
        complainMapper.save(complain);
        return ApiResult.success("申诉创建成功，等待管理员处理");
    }

    private void createComplainInfo(Complain complain) {
        complain.setCustomerComplainStatus(true);
        complain.setAdminComplainStatus(COMPLAIN_UNHANDLED);
        complain.setCreateTime(new Date());
    }

    @Override
    @Transactional
    public Result<String> update(Complain complain) {
        if (complain.getId() == null) {
            return ApiResult.error("申诉ID不能为空");
        }
        // 如果状态改为已处理，自动设置处理时间
        if (complain.getAdminComplainStatus() != null && complain.getAdminComplainStatus() == COMPLAIN_HANDLED) {
            complain.setProcessTime(new Date());
        }
        complainMapper.update(complain);
        return ApiResult.success("申诉修改成功");
    }

    @Override
    @Transactional
    public Result<String> batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return ApiResult.error("请选择要删除的申诉ID");
        }
        complainMapper.batchDelete(ids);
        return ApiResult.success("申诉删除成功");
    }

    @Override
    public Result<List<ComplainVO>> query(ComplainQueryDTO complainQueryDTO) {
        int totalCount = complainMapper.queryCount(complainQueryDTO);
        List<ComplainVO> complainVOList = complainMapper.query(complainQueryDTO);
        return ApiResult.success(complainVOList, totalCount);
    }

    @Override
    public Result<List<ComplainVO>> queryComplainList(ComplainQueryDTO complainQueryDTO) {
        complainQueryDTO.setCurrent(null);
        complainQueryDTO.setSize(null);
        List<ComplainVO> complainVOList = complainMapper.query(complainQueryDTO);
        return ApiResult.success(complainVOList);
    }

    @Override
    @Transactional
    public Result<String> handleComplain(Integer complainId, String adminComment) {
        if (complainId == null) {
            return ApiResult.error("申诉ID不能为空");
        }
        if (adminComment == null || adminComment.trim().isEmpty()) {
            return ApiResult.error("管理员处理意见不能为空");
        }
        Complain complain = new Complain();
        complain.setId(complainId);
        complain.setAdminComment(adminComment);
        complain.setAdminComplainStatus(COMPLAIN_HANDLED);
        complain.setProcessTime(new Date());
        complainMapper.update(complain);
        return ApiResult.success("申诉处理成功");
    }

    @Override
    public Result<List<ComplainVO>> queryByOrderIds(ComplainQueryDTO complainQueryDTO) {
        List<Integer> orderIds = complainQueryDTO.getOrderIds();
        if (orderIds == null || orderIds.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        List<ComplainVO> complainVOList = complainMapper.queryByOrderIds(complainQueryDTO);
        return ApiResult.success(complainVOList);
    }

    @Override
    public Result<ComplainVO> queryByOrderId(Integer orderId) {
        if (orderId == null) {
            return ApiResult.error("订单ID不能为空");
        }
        ComplainVO vo = complainMapper.queryByOrderId(orderId);
        return vo != null ? ApiResult.success(vo) : ApiResult.error("未找到对应申诉");
    }

    @Override
    public Result<ComplainVO> queryById(Integer id) {
        if (id == null) {
            return ApiResult.error("申诉ID不能为空");
        }
        ComplainVO vo = complainMapper.queryById(id);
        return vo != null ? ApiResult.success(vo) : ApiResult.error("未找到对应申诉");
    }

    @Override
    @Transactional
    public Result<String> delete(Integer id) {
        if (id == null) {
            return ApiResult.error("申诉ID不能为空");
        }
        complainMapper.delete(id);
        return ApiResult.success("申诉删除成功");
    }
}