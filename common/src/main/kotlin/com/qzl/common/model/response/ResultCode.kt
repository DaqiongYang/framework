package com.qzl.common.model.response

/**
 * 10000-- 通用错误代码
 * 22000-- 媒资错误代码
 * 23000-- 用户中心错误代码
 * 24000-- cms错误代码
 * 25000-- 文件系统
 */
interface ResultCode {
    //操作是否成功,true为成功，false操作失败
    fun success(): Boolean

    //操作代码
    fun code(): Int

    //提示信息
    fun message(): String

}
