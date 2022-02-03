package com.transport.mall.ui.home.helpline.chat

import com.transport.mall.BuildConfig

interface Const {
    companion object {
        const val APP_NAME = "TransportTV"
        const val BASE_URL_DHABA = BuildConfig.BASE_URL
        const val BASE_URL = BuildConfig.BASE_URL
        const val SOCKET_URL = BuildConfig.BASE_URL

        //    String BASE_URL = "http://54.190.192.105:6105/";
        //    String SOCKET_URL = "http://54.190.192.105:6105/";
        //API Methods
        const val CATEGORY = "api/v1/category/getAllCategory_Web"
        const val GET_VIDEO_BY_CATEGORY = "api/v1/common/getVideoByCategory"
        const val GET_ALL_VIDEOS_BY_CATEGORY = "api/v1/common/getAllVideosByCategory"
        const val GET_ALL_MESSAGE_API = "api/v1/room-chat-list/{id}"
        const val IMAGE_CHAT = "api/v1/common/s3imgUploadchat"
        const val DELETE_CHAT = "/api/v1/clear-chat/{roomId}"
        const val GET_VIDEOS_BY_ID = "api/v1/common/getVideoData/"
        const val SAVE_LIKES = "api/v1/video/saveLikes"
        const val SAVE_FAVORITE = "api/v1/video/saveFavorite"
        const val SAVE_LANGUAGE = "api/v1/common/saveLanguage"
        const val USER_LOGOUT = "api/v1/user/logout"
        const val MESSAGE = "message"
        const val GET_ALL_COMMENT = "api/v1/comment/get-all-comments"
        const val GET_ALL_COMMENT_REPLIES = "/api/v1/comment/get-all-comment-replies"
        const val SAVE_USER_COMMENT = "/api/v1/comment/saveUserComment"
        const val SAVE_COMMENT_REPLY = "/api/v1/comment/saveCommentReply"
        const val SEARCH = "api/v1/video/search"
        const val GET_NOTIFICATION_COUNT = "api/v1/common/getNotificationCount"

        //    String IMAGE_URL = "http://54.190.192.105:6105/images/video/";
        const val OFFLINE = "Offline"
        const val NOTIFICATION_COUNT = "NOTIFICATION_COUNT"
        const val HIT_NOTIFICATION_COUNT_API = "HIT_NOTIFICATION_COUNT_API"
        const val HIT_WATCH_TIME_API = "HIT_WATCH_TIME_API"
        const val OFFLINEID = "OfflineID"
        const val CLEAR_SINGLE_NOTIFICATION = "api/v1/common/clearSingleNotification/{id}"
        const val CLEAR_ALL_NOTIFICATION = "api/v1/common/clearAllNotification/{type}"
        const val DAILY_WINNERS = "api/v1/rewards/get-all-daily-winners/{campaignTypeSlug}"
        const val ALL_MONTHLY_WINNERS = "api/v1/rewards/get-all-monthly-winners"
        const val TOP_FIVE_RANKED_USER = "api/v1/rewards/get-top-five/{id}/{type}/{campaignTypeSlug}"
        const val MY_REWARDS = "api/v1/rewards/my-rewards/{id}"
    }
}