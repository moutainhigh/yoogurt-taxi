package com.yoogurt.taxi.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoogurt.taxi.common.enums.StatusCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Map;

/**
 * Description:
 * 客户端响应返回值封装。
 * @author Eric Lau
 * @Date 2017/8/28.
 */
@Slf4j
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult<T> implements Serializable {

    /**
     * 返回的状态码
     */
    private int status = StatusCode.INFO_SUCCESS.getStatus();

    /**
     * 提示信息
     */
    private String message = StatusCode.INFO_SUCCESS.getDetail();

    /**
     * 返回的数据主体内容
     */
    private T body;

    /**
     * 返回的额外信息，无特殊情况，不使用
     */
    private Map<String, Object> extras;

    public RestResult() {
    }

    public RestResult(int status, String message, T body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public static <T> RestResult<T> success() {
        return new RestResult<>();
    }

    public static <T> RestResult<T> success(T body) {

        RestResult<T> result = success();
        result.setBody(body);
        return result;
    }

    public static RestResult fail() {
        RestResult result = new RestResult();
        result.setStatus(StatusCode.BIZ_FAILED.getStatus());
        result.setMessage(StatusCode.BIZ_FAILED.getDetail());
        return result;
    }

    public static RestResult fail(StatusCode statusCode, String message) {
        int status = StatusCode.BIZ_FAILED.getStatus();
        if (statusCode != null) {
            status = statusCode.getStatus();
        }
        return fail(status, message);
    }

    public static RestResult fail(int status, String message) {
        RestResult result = new RestResult();
        result.setStatus(status);
        if (StringUtils.isBlank(message)) {
            message = StatusCode.BIZ_FAILED.getDetail();
        }
        result.setMessage(message);
        return result;
    }

    /**
     * 将ResponseObj对象转换成JSON字符串。
     * 基于Jackson对象序列化技术。
     * @return
     */
    public String toJSON() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(DateFormat.getDateTimeInstance());
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化异常,{}", e);
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String toString() {
        return toJSON();
    }

}