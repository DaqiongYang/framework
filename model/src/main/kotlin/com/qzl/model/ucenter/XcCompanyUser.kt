package com.qzl.model.ucenter

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.*

/**
 * Created by admin on 2018/2/10.
 */
@Entity
@Table(name = "xc_company_user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
class XcCompanyUser : Serializable {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    var id: String? = null
    @Column(name = "company_id")
    var companyId: String? = null
    @Column(name = "user_id")
    var userId: String? = null

    companion object {
        private const val serialVersionUID = -916357110051689786L
    }
}
