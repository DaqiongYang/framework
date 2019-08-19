package com.qzl.model.ucenter.response

import com.google.common.collect.ImmutableMap
import com.qzl.common.model.response.ResultCode
import io.swagger.annotations.ApiModelProperty


/**
 * Created by admin on 2018/3/5.
 */
enum class AuthCode private constructor(//操作代码
        @field:ApiModelProperty(value = "操作是否成功", example = "true", required = true)
        internal var success: Boolean, //操作代码
        @field:ApiModelProperty(value = "操作代码", example = "22001", required = true)
        internal var code: Int, //提示信息
        @field:ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
        internal var message: String) : ResultCode {
    AUTH_USERNAME_NONE(false, 23001, "请输入账号！"),
    AUTH_PASSWORD_NONE(false, 23002, "请输入密码！"),
    AUTH_VERIFYCODE_NONE(false, 23003, "请输入验证码！"),
    AUTH_ACCOUNT_NOTEXISTS(false, 23004, "账号不存在！"),
    AUTH_CREDENTIAL_ERROR(false, 23005, "账号或密码错误！"),
    AUTH_LOGIN_ERROR(false, 23006, "登陆过程出现异常请尝试重新操作！"),
    AUTH_LOGIN_APPLYTOKEN_FAIL(false, 23007, "申请令牌失败！"),
    AUTH_LOGIN_TOKEN_SAVEFAIL(false, 23008, "存储令牌失败！");

    override fun success(): Boolean {
        return success
    }

    override fun code(): Int {
        return code
    }

    override fun message(): String {
        return message
    }

    companion object {
        private val CACHE: ImmutableMap<Int, AuthCode>

        init {
            val builder = ImmutableMap.builder<Int, AuthCode>()
            for (commonCode in values()) {
                builder.put(commonCode.code(), commonCode)
            }
            CACHE = builder.build()
        }
    }
}
