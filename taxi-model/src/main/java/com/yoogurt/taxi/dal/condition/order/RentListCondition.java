package com.yoogurt.taxi.dal.condition.order;

import com.yoogurt.taxi.common.condition.SortWithPageableCondition;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 列表形式展现租单信息，将地图上的检索条件带入进来。
 * 增加了排序和分页参数。
 * -------------------------------------------------
 * 排序字段：price-租金， rent_time-出租时长， score-评价
 * 一次排序只能有一个排序字段，三者不可同时生效。
 * -------------------------------------------------
 */
@Getter
@Setter
public class RentListCondition extends SortWithPageableCondition {

    /**
     * 指定范围的最大经度
     */
    private Double maxLng;

    /**
     * 指定范围的最小经度
     */
    private Double minLng;

    /**
     * 指定范围的最大纬度
     */
    private Double maxLat;

    /**
     * 指定范围的最大纬度
     */
    private Double minLat;

    /**
     * 距离，默认1km。
     * 此字段已废弃
     */
    private Integer distance;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 对查询条件进行必要的逻辑验证。
     * 简单的验证可以加注解，复杂的验证交给validate()
     *
     * @return true 验证通过，false 验证不通过
     */
    @Override
    public boolean validate() {
        String sortName = super.getSortName();
        String sortOrder = super.getSortOrder();
        return super.validate() && (startTime == null || endTime == null || startTime.compareTo(endTime) <= 0)
                && (StringUtils.isBlank(sortName) || "price".equalsIgnoreCase(sortName) || "rent_time".equalsIgnoreCase(sortName) || "score".equalsIgnoreCase(sortName))
                && (StringUtils.isBlank(sortOrder) || "ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder));
    }
}
