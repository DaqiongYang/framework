package com.qzl.model.ucenter.response;


import com.qzl.common.model.response.ResponseResult;
import com.qzl.common.model.response.ResultCode;

/**
 * Created by mrt on 2018/5/21.
 */
public class LoginResult extends ResponseResult {
    public LoginResult(ResultCode resultCode, String token) {
        super(resultCode);
        this.token = token;
    }
    private String token;
}
