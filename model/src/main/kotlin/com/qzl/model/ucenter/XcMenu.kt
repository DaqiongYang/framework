package com.qzl.model.ucenter

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

/**
 * Created by admin on 2018/3/19.
 */
@Entity
@Table(name = "xc_menu")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
class XcMenu {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    var id: String? = null
    var code: String? = null
    @Column(name = "p_code")
    var pCode: String? = null
    @Column(name = "p_id")
    var pId: String? = null
    @Column(name = "menu_name")
    var menuName: String? = null
    var url: String? = null
    @Column(name = "is_menu")
    var isMenu: String? = null
    var level: Int? = null
    var sort: Int? = null
    var status: String? = null
    var icon: String? = null
    @Column(name = "create_time")
    var createTime: Date? = null
    @Column(name = "update_time")
    var updateTime: Date? = null
}
