package com.transport.mall.repository.transporttv;


import com.smartdata.transportmall.api.DTO.DislikeWishlist;
import com.smartdata.transportmall.api.DTO.Comment;
import com.smartdata.transportmall.api.DTO.CommentReplies;
import com.smartdata.transportmall.api.DTO.Favorite;
import com.smartdata.transportmall.api.DTO.Language;
import com.smartdata.transportmall.api.DTO.LikeWishlist;
import com.smartdata.transportmall.interfaces.Const;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {


    @Multipart
    @POST(Const.IMAGE_CHAT)
    Call<ResponseBody> imgChat(
            @Part("type") RequestBody type,
            @Part MultipartBody.Part chat_image);

    @GET(Const.CATEGORY)
    Call<ResponseBody> getCategory();

    @GET(Const.GET_VIDEO_BY_CATEGORY)
    Call<ResponseBody> getVideoByCategory(@Query("categorySlug") String categorySlug, @Query("userId") String userId, @Query("page") String page, @Query("limit") String limit);

    @GET(Const.GET_VIDEO_BY_CATEGORY)
    Call<ResponseBody> getVideoBySubCategory(@Query("subCategorySlug") String categorySlug, @Query(
            "userId") String userId, @Query("page") String page, @Query("limit") String limit);

    @GET(Const.GET_VIDEOS_BY_ID + "{id}")
    Call<ResponseBody> getVideoByID(@Path("id") String id, @Header("Authorization") String authHeader);

    @GET(Const.GET_ALL_VIDEOS_BY_CATEGORY)
    Call<ResponseBody> getAllVideoByCategory(@Query("userId") String userId);

    @GET(Const.GET_ALL_MESSAGE_API)
    Call<ResponseBody> room_chat_list(@Path("id") String id,
                                      @Header("Authorization") String authHeader,
                                      @Query("count") int count ,@Query("page") int page ,@Query("sendFrom") String sendFrom);


    @DELETE(Const.DELETE_CHAT)
    Call<ResponseBody> room_delet_chat(@Path("roomId") String id, @Header("Authorization") String authHeader);

    @POST(Const.SAVE_LIKES)
    Call<ResponseBody> saveDisLikes(@Body DislikeWishlist addWishlist,
                                  @Header("Authorization") String authHeader);
    @POST(Const.SAVE_LIKES)
    Call<ResponseBody> saveLikes(@Body LikeWishlist addWishlist,
                                 @Header("Authorization") String authHeader);

    @POST(Const.SAVE_FAVORITE)
    Call<ResponseBody> saveFav(@Body Favorite favorite, @Header("Authorization") String authHeader);

    @POST(Const.SAVE_LANGUAGE)
    Call<ResponseBody> saveLanguage(@Body Language language,
                                @Header("Authorization") String authHeader);

    @DELETE(Const.USER_LOGOUT)
    Call<ResponseBody> userLogout(@Query("loginToken") String loginToken,
                                  @Header("Authorization") String authHeader);
    @GET(Const.GET_ALL_COMMENT)
    Call<ResponseBody> getComment(@Query("videoId") String videoId,@Query("count") String count,@Query("page") String page, @Header("Authorization") String authHeader);

    @GET(Const.GET_ALL_COMMENT_REPLIES)
    Call<ResponseBody> getRepliesComment(@Query("commentId") String videoId,@Query("count") String count,@Query("page") String page, @Header("Authorization") String authHeader);

    @POST(Const.SAVE_USER_COMMENT)
    Call<ResponseBody> sendComment(@Body Comment comment, @Header("Authorization") String authHeader);

    @POST(Const.SAVE_COMMENT_REPLY)
    Call<ResponseBody> sendReplyComment(@Body CommentReplies commentReplies, @Header("Authorization") String authHeader);

    @GET(Const.SEARCH)
    Call<ResponseBody> getVideoSearch(@Query("searchKey") String searchKey, @Query("userId") String userId);

    @GET(Const.GET_NOTIFICATION_COUNT)
    Call<ResponseBody> getNotification(@Query("userId") String userId, @Header("Authorization") String authHeader);

    @DELETE(Const.CLEAR_SINGLE_NOTIFICATION)
    Call<ResponseBody> clearSingleNotification(@Path("id") String id, @Header("Authorization") String authHeader);

    @DELETE(Const.CLEAR_ALL_NOTIFICATION)
    Call<ResponseBody> clearAllNotification(@Path("type") String id, @Header("Authorization") String authHeader);


    /*5 Liter Diesel Winners */
    @GET(Const.DAILY_WINNERS)
    Call<ResponseBody> getDailyWinners(@Path("campaignTypeSlug") String campaignTypeSlug,@Query("count") String count,@Query("page") String page, @Header("Authorization") String authHeader);


    /*LED TV Winners */
    @GET(Const.ALL_MONTHLY_WINNERS)
    Call<ResponseBody> getAllMonthlyWinners(@Query("count") String count,@Query("page") String page, @Header("Authorization") String authHeader);


    /* 5 Liter Daily Diesel and LED TV Monthly response*/
    @GET(Const.TOP_FIVE_RANKED_USER)
    Call<ResponseBody> getTopFiveRankedUser(@Path("id") String id,@Path("type") String type,@Path("campaignTypeSlug") String campaignTypeSlug, @Header("Authorization") String authHeader);

    /* My Points and Rewards response*/
    @GET(Const.MY_REWARDS)
    Call<ResponseBody> getMyRewardsData(@Path("id") String id, @Header("Authorization") String authHeader);

    /* GET_ALL_MENU*/
    @GET(Const.GET_ALL_MENU)
    Call<ResponseBody> getAllMenuStatus(@Header("Authorization") String authHeader);


}
