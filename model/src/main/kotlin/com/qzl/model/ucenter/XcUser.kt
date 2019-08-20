package com.qzl.model.ucenter

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

/**
 * Created by admin on 2018/3/19.
 */
@Entity
@Table(name = "xc_user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
open class XcUser {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    var id: String? = null
    var username: String? = null
    var password: String? = null
    var salt: String? = null
    var name: String? = null
    var utype: String? = null
    var birthday: String? = null
    var userpic: String? = null
    var sex: String? = null
    var email: String? = null
    var phone: String? = null
    var status: String? = null
    @Column(name = "create_time")
    var createTime: Date? = null
    @Column(name = "update_time")
    var updateTime: Date? = null

}
