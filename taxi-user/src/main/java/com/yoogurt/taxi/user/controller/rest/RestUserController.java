package com.yoogurt.taxi.user.controller.rest;

import com.google.common.collect.Lists;
import com.yoogurt.taxi.common.controller.BaseController;
import com.yoogurt.taxi.common.enums.StatusCode;
import com.yoogurt.taxi.common.vo.RestResult;
import com.yoogurt.taxi.dal.beans.CarInfo;
import com.yoogurt.taxi.dal.beans.DriverInfo;
import com.yoogurt.taxi.dal.beans.UserInfo;
import com.yoogurt.taxi.dal.beans.UserRoleInfo;
import com.yoogurt.taxi.dal.model.user.AuthorityModel;
import com.yoogurt.taxi.user.dao.UserDao;
import com.yoogurt.taxi.user.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/user")
public class RestUserController extends BaseController {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleAuthorityService roleAuthorityService;

    @Autowired
    private UserService userService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private CarService carService;

    @RequestMapping(value = "/driverInfo/id/{id}",method = RequestMethod.GET)
    public RestResult<DriverInfo> driverInfo(@PathVariable(name = "id") Long id) {

        DriverInfo driverInfo = driverService.getDriverInfo(id);
        if (driverInfo == null) {
            return RestResult.fail(StatusCode.BIZ_FAILED,"对象不存在");
        }
        return RestResult.success(driverInfo);
    }

    @RequestMapping(value = "/driverInfoByUserId/userId/{userId}",method = RequestMethod.GET)
    public RestResult<DriverInfo> driverInfoByUserId(@PathVariable(name = "userId") Long userId) {
        DriverInfo driverInfo = driverService.getDriverByUserId(userId);
        if (driverInfo == null) {
            return RestResult.fail(StatusCode.BIZ_FAILED,"对象不存在");
        }
        return RestResult.success(driverInfo);
    }

    @RequestMapping(value = "/authorities/userId/{userId}", method = RequestMethod.GET)
    public RestResult<List<AuthorityModel>> authorities(@PathVariable(name = "userId") Long userId) {
        List<AuthorityModel> authorities = Lists.newArrayList();
        UserRoleInfo userRoleInfo = userRoleService.getUserRoleInfo(userId, null);
        if (userRoleInfo != null) {
            authorities = roleAuthorityService.getAuthoritiesByRoleId(userRoleInfo.getRoleId());
        }
        return RestResult.success(authorities);
    }

    @RequestMapping(value = "/userInfo/userId/{userId}", method = RequestMethod.GET)
    public RestResult<UserInfo> userInfo(@PathVariable(name = "userId") Long userId) {
        UserInfo userInfo = userService.getUserByUserId(userId);
        if (userInfo == null) {
            return RestResult.fail(StatusCode.BIZ_FAILED,"对象不存在");
        }
        return RestResult.success(userInfo);
    }

    @RequestMapping(value = "/carInfo/id/{id}", method = RequestMethod.GET)
    public RestResult<CarInfo> carInfo(@PathVariable(name = "id") Long id) {
        CarInfo carInfo = carService.getCarInfo(id);
        if (carInfo == null) {
            return RestResult.fail(StatusCode.BIZ_FAILED,"对象不存在");
        }
        return RestResult.success(carInfo);
    }

    @RequestMapping(value = "/carInfoByUserId/userId/{userId}", method = RequestMethod.GET)
    public RestResult<List<CarInfo>> carInfoByUserId(@PathVariable(name = "userId") Long userId) {
        List<CarInfo> carList = carService.getCarByUserId(userId);
        return RestResult.success(carList);
    }

    @RequestMapping(value = "/carInfoByDriverId/driverId/{driverId}", method = RequestMethod.GET)
    public RestResult<List<CarInfo>> carInfoByDriverId(@PathVariable(name = "driverId") Long driverId) {
        return RestResult.success(carService.getCarByDriverId(driverId));
    }
}
