package com.yoogurt.taxi.account.service;

import com.yoogurt.taxi.dal.beans.FinanceRecord;

import java.util.List;

public interface FinanceRecordService {
    FinanceRecord save(FinanceRecord record);
    int remove(Long id);
    int delete(Long id);
    int deleteByBillNo(String billNo);
    FinanceRecord get(Long id);
    List<FinanceRecord> getBillRecord(String userId, Long billId, String billNo);
}
