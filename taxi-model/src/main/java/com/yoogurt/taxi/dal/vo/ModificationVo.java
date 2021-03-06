package com.yoogurt.taxi.dal.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class ModificationVo {

    private String contextId;

    @NotNull(message = "账单用户id不能为空")
    private String userId;

    @NotNull(message = "收款人id不能为空")
    private String inUserId;

    @NotNull(message = "付款人id不能为空")
    private String outUserId;

    @NotNull(message = "交易金额不能为空")
    private BigDecimal money;

    @NotNull(message = "交易类型不能为空")
    private Integer type;

    private Integer payment;
}
