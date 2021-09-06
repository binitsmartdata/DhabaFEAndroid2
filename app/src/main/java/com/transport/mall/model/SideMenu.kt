package com.transport.mall.model

/**
 * Created by Parambir Singh on 2020-01-24.
 */
data class SideMenu(
    val title: String,
    val icon: Int,
    val hilightedIcon: Int,
    var isSelected: Boolean
)