package com.transport.mall.model

import android.net.Uri
import android.widget.ImageView
import androidx.room.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
import com.transport.mall.utils.common.GlobalUtils
import java.io.Serializable

@Entity(
    tableName = "ChatMessagesListModel", indices = [Index(
        value = ["createdAt", "_id"],
        unique = true
    )]
)
class ChatMessagesListModel : Comparable<ChatMessagesListModel>, Cloneable, Serializable {
    @PrimaryKey(autoGenerate = true)
    var p_id: Long? = null

    @SerializedName("type")
    @Expose
    @ColumnInfo(name = "type")
    var type: String? = null

    @ColumnInfo(name = "_id")
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("roomId")
    @Expose
    @ColumnInfo(name = "roomId")
    var roomId: String? = null

    @SerializedName("from")
    @Expose
    @Embedded
    var from: From? = null

    @ColumnInfo(name = "message")
    @SerializedName("message")
    @Expose
    var message: String? = null
        get() = /*if (App.currentLanguage != null) {
            if (App.currentLanguage == "hi" && message_hi != null && !message_hi.isEmpty()) {
                message_hi
            } else if (App.currentLanguage.contains("p") && message_pu != null && !message_pu.isEmpty()) {
                message_pu
            } else {
                field
            }
        } else {*/
            field
//        }

    @SerializedName("message_pu")
    @Expose
    @Ignore
    private val message_pu: String? = null

    @SerializedName("message_hi")
    @Expose
    @Ignore
    private val message_hi: String? = null

    @SerializedName("members")
    @Expose
    @Ignore
    var members: List<Member>? = null

    @ColumnInfo(name = "createdAt")
    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    @Ignore
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    @Ignore
    var v: Int? = null

    @SerializedName("unassignedRead")
    @Expose
    @Ignore
    var unassignedRead: Boolean? = null

    //        Timber.e("setMessageDate  "+messageDate + " created at " +getCreatedAt());
    @Ignore
    var messageDate: String? = null

    @ColumnInfo(name = "typeUri")
    var typeUri: String? = null

    @ColumnInfo(name = "domain")
    var domain: String? = null

    @SerializedName("mobileTimeStamp")
    @Expose
    @ColumnInfo(name = "mobileTimeStamp")
    var mobileTimeStamp: Long? = null

    var user_id: String? = null

    @Ignore
    var unreadCount = 0

    @Ignore
    var progress = -1

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any {
        return super.clone()
    }

    // 2021-06-28 20:31:57
    val time: String
        get() {
            var time = ""
            // 2021-06-28 20:31:57
            if (createdAt != null) {
                time =
                    GlobalUtils.getTimeAgo(createdAt)
            }
            return time
        }

    val isChatType: Boolean
        get() = type.equals(
            "agent_join_notification",
            ignoreCase = true
        ) || type.equals("agent_chatEnd_notification", ignoreCase = true) || type.equals(
            "welcome",
            ignoreCase = true
        ) || type.equals("agent_offline", ignoreCase = true)

    override fun compareTo(o: ChatMessagesListModel): Int {
        return o.createdAt?.compareTo(createdAt ?: "") ?: 0
    }

    val isShowDate: Boolean
        get() = messageDate != null && !messageDate.equals("", ignoreCase = true)
    val isShowUnreadCount: Boolean
        get() = unreadCount > 0
    val isShowCustomer: Boolean
        get() = !isShowUnreadCount && !isChatType && from?.id == user_id
    val isShowAgent: Boolean
        get() = !isShowUnreadCount && !isChatType && from?.id != user_id

    companion object {
        @JvmStatic
//        @BindingAdapter("bind:imageUrl", "bind:load")
        fun loadImage(view: ImageView, imageUrl: String?, load: Boolean) {
            if (load) Glide.with(view.context).load(imageUrl).placeholder(R.drawable.placeholder)
                .centerCrop().into(view)
        }

        //        @BindingAdapter("bind:imageUri", "bind:load")
        fun loadVideoImage(view: ImageView, imageUrl: Uri?, load: Boolean) {
            if (load) Glide.with(view.context).load(imageUrl).placeholder(R.drawable.video_thumb)
                .into(view) else Glide.with(view.context).load(R.drawable.video_thumb)
                .placeholder(R.drawable.video_thumb).into(view)
        }
    }

    fun deepCopy(): ChatMessagesListModel {
        val JSON = Gson().toJson(this)
        return Gson().fromJson(JSON, ChatMessagesListModel::class.java)
    }
}