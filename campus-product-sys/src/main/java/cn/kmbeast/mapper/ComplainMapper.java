package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.ComplainQueryDTO;
import cn.kmbeast.pojo.entity.Complain;
import cn.kmbeast.pojo.vo.ComplainVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 申诉持久化接口
 */
public interface ComplainMapper {

    /**
     * 新增
     *
     * @param complain 实体信息
     * @return int 受影响行数
     */
    int save(Complain complain);

    /**
     * 分页查询申诉信息
     *
     * @param complainQueryDTO 分页查询参数
     * @return List<ComplainVO>
     */
    List<ComplainVO> query(ComplainQueryDTO complainQueryDTO);

    /**
     * 查询满足分页查询的记录总数
     *
     * @param complainQueryDTO 分页查询参数
     * @return int 数据总数
     */
    int queryCount(ComplainQueryDTO complainQueryDTO);

    /**
     * 修改
     *
     * @param complain 申诉信息
     * @return int 受影响行数
     */
    int update(Complain complain);

    /**
     * 批量删除申诉信息
     *
     * @param ids 申诉ID集合
     */
    void batchDelete(@Param(value = "ids") List<Integer> ids);

    /**
     * 根据订单id列表查询申诉信息
     *
     * @param complainQueryDTO 查询参数
     * @return List<ComplainVO>
     */
    List<ComplainVO> queryByOrderIds(ComplainQueryDTO complainQueryDTO);

    /**
     * 根据id查询申诉信息
     *
     * @param id 申诉主键id
     * @return ComplainVO
     */
    ComplainVO queryById(@Param("id") Integer id);

    /**
     * 根据订单id查询申诉信息
     *
     * @param orderId 订单id
     * @return Complain
     */
    ComplainVO queryByOrderId(@Param("orderId") Integer orderId);

    /**
     * 根据id删除申诉信息
     *
     * @param id 申诉主键id
     */
    void delete(@Param("id") Integer id);

    
}