package com.yoogurt.taxi.account.service;

import com.yoogurt.taxi.common.bo.Money;
import com.yoogurt.taxi.common.vo.ResponseObj;
import com.yoogurt.taxi.dal.beans.FinanceAccount;
import com.yoogurt.taxi.dal.condition.account.AccountUpdateCondition;

public interface FinanceAccountService {
    FinanceAccount get(Long userId);
    ResponseObj createAccount(Long accountNo, Money receivableDeposit, Long userId);

    /**
     * 更新账户，具体功能有：1.充值（提现申请），2.提现（回调，即将冻结金额扣除，账户不动）
     * 3.罚款，4.补偿，5.订单收入
     * @param condition
     * @return
     */
    ResponseObj updateAccount(AccountUpdateCondition condition);

}