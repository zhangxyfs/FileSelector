package com.z7dream.lib.selector.box.model

class ArgsBean constructor(var searchKey: String?, var parentPath: String?, var userId: Long, var companyId: Long){

    constructor(searchKey: String?, userId: Long, companyId: Long):this(searchKey, null, userId, companyId)
}