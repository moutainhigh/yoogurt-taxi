package com.yoogurt.taxi.account.dao.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yoogurt.taxi.account.dao.FinanceBillDao;
import com.yoogurt.taxi.common.dao.impl.BaseDao;
import com.yoogurt.taxi.common.factory.AppPagerFactory;
import com.yoogurt.taxi.common.factory.WebPagerFactory;
import com.yoogurt.taxi.common.pager.BasePager;
import com.yoogurt.taxi.dal.beans.FinanceBill;
import com.yoogurt.taxi.dal.condition.account.AccountListAppCondition;
import com.yoogurt.taxi.dal.condition.account.BillListWebCondition;
import com.yoogurt.taxi.dal.condition.account.ExportBillCondition;
import com.yoogurt.taxi.dal.condition.account.WithdrawListWebCondition;
import com.yoogurt.taxi.dal.mapper.FinanceBillMapper;
import com.yoogurt.taxi.dal.model.account.FinanceBillListAppModel;
import com.yoogurt.taxi.dal.model.account.FinanceBillListWebModel;
import com.yoogurt.taxi.dal.model.account.WithdrawBillListWebModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class FinanceBillDaoImpl extends BaseDao<FinanceBillMapper,FinanceBill> implements FinanceBillDao {
    @Autowired
    private FinanceBillMapper financeBillMapper;
    @Autowired
    private AppPagerFactory appPagerFactory;
    @Autowired
    private WebPagerFactory webPagerFactory;
    @Override
    public BasePager<FinanceBillListAppModel> getFinanceBillListApp(AccountListAppCondition condition) {
        PageHelper.startPage(condition.getPageNum(),condition.getPageSize()).setOrderBy(" gmt_modify desc");
        Page<FinanceBillListAppModel> financeBillListApp = financeBillMapper.getFinanceBillListApp(condition);
        return appPagerFactory.generatePager(financeBillListApp);
    }

    @Override
    public BasePager<FinanceBillListWebModel> getFinanceBillListWeb(BillListWebCondition condition) {
        PageHelper.startPage(condition.getPageNum(),condition.getPageSize()).setOrderBy(" gmt_create desc");
        Page<FinanceBillListWebModel> financeBillListWeb = financeBillMapper.getFinanceBillListWeb(condition);
        return webPagerFactory.generatePager(financeBillListWeb);
    }

    @Override
    public BasePager<WithdrawBillListWebModel> getWithdrawBillListWeb(WithdrawListWebCondition condition) {
        PageHelper.startPage(condition.getPageNum(),condition.getPageSize());
        Page<WithdrawBillListWebModel> withdrawBillListWeb = financeBillMapper.getWithdrawBillListWeb(condition);
        return webPagerFactory.generatePager(withdrawBillListWeb);
    }

    @Override
    public List<Map<String, Object>> getWithdrawListForExport(ExportBillCondition condition) {
        return financeBillMapper.getWithdrawListForExport(condition);
    }
}
