package com.yoogurt.taxi.dal.mapper;

import com.github.pagehelper.Page;
import com.yoogurt.taxi.common.mapper.SuperMapper;
import com.yoogurt.taxi.dal.beans.UserInfo;
import com.yoogurt.taxi.dal.condition.user.UserWLCondition;
import com.yoogurt.taxi.dal.model.user.UserWLModel;

import java.util.List;

public interface UserInfoMapper extends SuperMapper<UserInfo> {
    Page<UserWLModel> getUserWebListPage(UserWLCondition condition);
    int insertUsers(List<UserInfo> list);
}