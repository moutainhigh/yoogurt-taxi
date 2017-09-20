package com.yoogurt.taxi.user.service.impl;

import com.yoogurt.taxi.common.vo.ResponseObj;
import com.yoogurt.taxi.dal.beans.CarInfo;
import com.yoogurt.taxi.user.dao.CarDao;
import com.yoogurt.taxi.user.service.CarInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarInfoServiceImpl implements CarInfoService{
    @Autowired
    private CarDao carDao;

    @Override
    public ResponseObj saveCarInfo(CarInfo carInfo) {
        carDao.insert(carInfo);
        return ResponseObj.success();
    }

    @Override
    public CarInfo getCarInfo(Long carId) {
        CarInfo carInfo = carDao.selectById(carId);
        return carInfo;
    }

    @Override
    public List<CarInfo> getCarByUserId(Long userId) {
        Example example = new Example(CarInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("isDeleted",Boolean.FALSE);
        return carDao.selectByExample(example);
    }

    @Override
    public List<CarInfo> getCarByDriverId(Long driverId) {
        Example example = new Example(CarInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("driverId",driverId);
        criteria.andEqualTo("isDeleted",Boolean.FALSE);
        return carDao.selectByExample(example);
    }

    @Override
    public ResponseObj removeCar(Long carId) {
        CarInfo carInfo = carDao.selectById(carId);
        if(carInfo == null){
            return ResponseObj.success();
        }
        carInfo.setIsDeleted(Boolean.TRUE);
        carDao.updateById(carInfo);
        return ResponseObj.success();
    }
}
