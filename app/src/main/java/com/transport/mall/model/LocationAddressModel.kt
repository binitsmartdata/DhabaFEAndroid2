package com.transport.mall.model

/**
 * Created by Parambir Singh on 2020-01-24.
 */
data class LocationAddressModel(
    val fullAddress: String,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String,
    val knownName: String
)