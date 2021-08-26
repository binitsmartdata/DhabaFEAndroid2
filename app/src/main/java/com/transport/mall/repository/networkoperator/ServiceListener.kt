package com.transport.mall.repository.networkoperator

/**
 * Created by parambir.singh on 14/02/18.
 */
interface ServiceListener<T> {
    fun getServerResponse(response: T, requestcode: Int)
    fun getError(error: ErrorModel, requestcode: Int)
}