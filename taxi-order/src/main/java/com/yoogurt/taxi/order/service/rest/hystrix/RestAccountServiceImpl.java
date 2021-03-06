package com.yoogurt.taxi.order.service.rest.hystrix;

import com.yoogurt.taxi.common.enums.StatusCode;
import com.yoogurt.taxi.common.vo.RestResult;
import com.yoogurt.taxi.dal.beans.FinanceAccount;
import com.yoogurt.taxi.dal.vo.ModificationVo;
import com.yoogurt.taxi.order.service.rest.RestAccountService;
import org.springframework.stereotype.Service;

@Service
public class RestAccountServiceImpl implements RestAccountService {

    @Override
    public RestResult<FinanceAccount> getAccountByUserId(String userId, Integer userType) {
        return RestResult.fail(StatusCode.BIZ_FAILED, "获取账户信息异常");
    }

    @Override
    public RestResult updateAccount(ModificationVo vo) {
        return RestResult.fail(StatusCode.BIZ_FAILED, "更新账户信息异常");
    }
}
