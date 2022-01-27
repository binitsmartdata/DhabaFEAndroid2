package com.transport.mall.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "reportReasons",
    indices = [Index(
        value = ["_id"],
        unique = true
    )]
)
class ReportReasonModel : Serializable, BaseObservable() {
    @ColumnInfo(name = "uid")
    @PrimaryKey(autoGenerate = true)
    var uid: Int? = null
        get() = field
        set(uid) {
            field = uid
        }

    @SerializedName(value = "_id")
    @ColumnInfo(name = "_id")
    var _id: String = ""
        @Bindable get() = field
        set(_id) {
            field = _id
            notifyPropertyChanged(BR._id)
        }

    @SerializedName(value = "name")
    @ColumnInfo(name = "name")
    var name: String = ""
        @Bindable get() = field
        set(name) {
            field = name
            notifyPropertyChanged(BR.name)
        }

    override fun toString(): String {
        return name
    }


}