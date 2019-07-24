package com.qzl.common.model.response

import lombok.Data
import lombok.ToString

@Data
@ToString
class QueryResponseResult(resultCode: ResultCode, internal var queryResult: QueryResult<*>) : ResponseResult(resultCode)
