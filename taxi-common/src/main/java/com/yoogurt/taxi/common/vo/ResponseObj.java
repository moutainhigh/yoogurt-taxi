package com.yoogurt.taxi.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoogurt.taxi.common.enums.StatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Description:
 * 客户端响应返回值封装。
 * @author Eric Lau
 * @Date 2017/8/28.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObj {

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
    private Object body;

    /**
     * 返回的额外信息，无特殊情况，不使用
     */
    private Map<String, Object> extras;

    private ResponseObj() {
    }

    private ResponseObj(int status, String message, Object body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public static ResponseObj success() {
        return new ResponseBuilder().build();
    }

    public static ResponseObj success(Object body) {
        return new ResponseBuilder().body(body).build();
    }

    public static ResponseObj success(Object body, Map<String, Object> extras) {
        return new ResponseBuilder().body(body).extras(extras).build();
    }

    public static ResponseObj fail() {
        return new ResponseBuilder().status(StatusCode.BIZ_FAILED.getStatus()).message(StatusCode.BIZ_FAILED.getDetail()).build();
    }

    public static ResponseObj fail(int status, String message) {
        ResponseBuilder builder = new ResponseBuilder().status(status);
        if (StringUtils.isBlank(message)) {
            message = StatusCode.BIZ_FAILED.getDetail();
        }
        return builder.message(message).build();
    }



    public static class ResponseBuilder {

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
        private Object body;

        /**
         * 返回的额外信息，无特殊情况，不使用
         */
        private Map<String, Object> extras;

        public ResponseBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder body(Object body) {
            this.body = body;
            return this;
        }

        public ResponseBuilder extras(Map<String, Object> extras) {
            this.extras = extras;
            return this;
        }

        public ResponseObj build() {
            ResponseObj obj = new ResponseObj(this.status, this.message, this.body);
            if (this.extras != null && this.extras.size() > 0) {
                obj.setExtras(this.extras);
            }
            return obj;
        }
    }

}
