package com.qzl.app

import com.qzl.api.TestApi
import com.qzl.common.model.response.CommonCode
import com.qzl.common.model.response.QueryResponseResult
import com.qzl.common.model.response.QueryResult
import com.qzl.model.CmsPage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class TestController :TestApi{

    @GetMapping("/testK")
    override fun test(name: String): QueryResponseResult {
        //暂时用静态数据
        //定义queryResult
        val queryResult = QueryResult<CmsPage>()
        val list = ArrayList<CmsPage>()
        val cmsPage = CmsPage(name)
        list.add(cmsPage)
        queryResult.list = list
        queryResult.total = 1

        return QueryResponseResult(CommonCode.SUCCESS, queryResult)
    }
}