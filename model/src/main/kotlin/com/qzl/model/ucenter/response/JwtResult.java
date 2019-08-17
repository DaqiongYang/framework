package com.qzl.model.ucenter.response;


import com.qzl.common.model.response.ResponseResult;
import com.qzl.common.model.response.ResultCode;

/**
 * Created by mrt on 2018/5/21.
 */
public class JwtResult extends ResponseResult {
    public JwtResult(ResultCode resultCode, String jwt) {
        super(resultCode);
        this.jwt = jwt;
    }
    private String jwt;
}
