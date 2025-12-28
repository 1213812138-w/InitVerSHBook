package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.ComplainQueryDTO;
import cn.kmbeast.pojo.entity.Complain;
import cn.kmbeast.pojo.vo.ComplainVO;
import cn.kmbeast.service.ComplainService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 申诉控制器（完全匹配ComplainService接口）
 */
@RestController
@RequestMapping("/api/campus-product-sys/v1.0/complain")
public class ComplainController {

    @Resource
    private ComplainService complainService;

    /**
     * 保存申诉信息
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody Complain complain) {
        return complainService.save(complain);
    }

    @GetMapping("/test")
    public String test() {
        return "ComplainController 已加载！处理接口正常！";
    }

    /**
     * 修改申诉信息
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody Complain complain) {
        return complainService.update(complain);
    }

    /**
     * 批量删除申诉信息
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return complainService.batchDelete(ids);
    }

    /**
     * 查询用户自己发起的申诉数据
     */
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<ComplainVO>> queryUser(@RequestBody ComplainQueryDTO complainQueryDTO) {
        complainQueryDTO.setUserId(LocalThreadHolder.getUserId());
        return complainService.query(complainQueryDTO);
    }

    /**
     * 查询申诉列表数据（无分页）
     */
    @PostMapping(value = "/queryComplainList")
    @ResponseBody
    public Result<List<ComplainVO>> queryComplainList(@RequestBody ComplainQueryDTO complainQueryDTO) {
        return complainService.queryComplainList(complainQueryDTO);
    }

    /**
     * 分页查询申诉列表
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<ComplainVO>> query(@RequestBody ComplainQueryDTO complainQueryDTO) {
        return complainService.query(complainQueryDTO);
    }

    /**
     * 根据订单ID列表查询申诉信息
     */
    @PostMapping(value = "/queryByOrderIds")
    @ResponseBody
    public Result<List<ComplainVO>> queryByOrderIds(@RequestBody ComplainQueryDTO complainQueryDTO) {
        return complainService.queryByOrderIds(complainQueryDTO);
    }

    /**
     * 根据订单ID查询申诉信息
     */
    @PostMapping(value = "/queryByOrderId")
    @ResponseBody
    public Result<ComplainVO> queryByOrderId(@RequestParam Integer orderId) {
        return complainService.queryByOrderId(orderId);
    }

    /**
     * 根据ID删除申诉信息
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public Result<String> delete(@RequestParam Integer id) {
        return complainService.delete(id);
    }
}