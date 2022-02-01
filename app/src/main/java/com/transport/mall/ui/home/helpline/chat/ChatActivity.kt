package com.transport.mall.ui.home.helpline.chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.location.LocationManager
import android.net.Uri
import android.os.*
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.work.*
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor.start
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rygelouv.audiosensei.player.AudioSenseiListObserver
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import com.smartdata.transportmall.BottomNavigation.chat.WorkConstants
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.content.Intent
import com.essam.simpleplacepicker.MapActivity
import com.essam.simpleplacepicker.utils.SimplePlacePicker
import com.transport.mall.R
import com.transport.mall.database.AppDatabase
import com.transport.mall.database.ChatDao
import com.transport.mall.databinding.ChatBinding
import com.transport.mall.model.ChatMessagesListModel
import com.transport.mall.utils.common.AudioRecorder
import com.transport.mall.databinding.*
import com.transport.mall.utils.common.FileUtils.getFileSize
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import java.net.Socket

class ChatActivity : AppCompatActivity() {

    private var chatDao: ChatDao? = null
    private lateinit var mContext: Context
    private var gson: Gson? = null
    private lateinit var mSocket: Socket
    private var page = 1
    private val perPage = 50
    private var orderTotalCount = 0
    private var unreadCount = 0
    private var scrolledOutItems = 0
    private var totalItems = 0
    private var isScrolling = false
    private var currentVisible = 0
    var picturePath: String? = null
    private var userChoosenTask = ""
    private val REQUEST_CAMERA = 0
    private val REQUEST_AUDIO = 42
    private val SELECT_FILE = 1
    private val SELECT_PDF = 2
    private val SELECT_CONTACT = 3
    private lateinit var model: ArrayList<ChatMessagesListModel>
    private val modelTemp = ArrayList<ChatMessagesListModel>()
    private var executor: ExecutorService? = null

    //    private var adapter: ChatMessagesAdapter? = null
    private var adapter: ChatAdapter? = null
    var isRecording = false

    //    String SOCKET_URL = " http://127.0.0.1:6105/";
    //    private LinearLayout btn_connect;

    private var chatside: String? = ""
    private var roomId = ""
    private var user_token: String = ""
    var tenpDate = ""
    var from: String? = "helpline"
    private var audioRecorder: AudioRecorder? = null
    private var recordFile: File? = null
    var request = false
    var isFirst = false
    private lateinit var binding: ChatBinding
    private var mCameraPhotoUri: Uri? = null
    private var mVideoUri: Uri? = null
    private var mAudioUri: Uri? = null

    private val LOCATION_PERMISSION_CODE = 100
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    private var locationShare: Boolean = false

    private var videoActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        userChoosenTask = ""
        if (result.resultCode == RESULT_OK) {
            try {
                mVideoUri = result.data?.data
//            val videoPath = getPath(mVideoUri)
                val videoPath = getRealPathVideoURI(mVideoUri)
                val finalFile = File(videoPath)
                val downloadsPath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val desFile = File(
                    downloadsPath,
                    System.currentTimeMillis().toString() + "_" + finalFile.name
                )
                if (desFile.exists()) {
                    desFile.delete()
                    try {
                        desFile.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                val time = arrayOfNulls<Long>(1)
                time[0] = 0L
                val path = arrayOf("")
                val progressDialog = ProgressDialog(this@ChatActivity)
                progressDialog.setMessage(resources.getString(R.string.please_wait))
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                progressDialog.isIndeterminate = false
                progressDialog.setCancelable(false)
                progressDialog.setProgressDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.progress_drawable_horizontal
                    )
                )
                progressDialog.max = 100
                //                    progressDialog.setIndeterminateDrawable(ContextCompat.getDrawable(ChatActivity.this,
//                            R.drawable.progress_drawable));
//                Log.e("chat Activity",("compression Original size: %s", getFileSize(desFile.length()))
                progressDialog.setCanceledOnTouchOutside(false)
                if (finalFile.length() < 150000000) {
                    try {
                        if (finalFile.length() > 0) {
                            start(videoPath, desFile.path, object : CompressionListener {
                                override fun onStart() {
                                    time[0] = System.currentTimeMillis()
                                    progressDialog.show()
                                    progressDialog.progress = 0
                                    progressDialog.setCancelable(false)
                                    progressDialog.secondaryProgress = 5
                                   /* Log.e("chat Activity",(
                                        "compression Original size: %s",
                                        getFileSize(finalFile.length())
                                    )*/
                                }

                                override fun onSuccess() {
                                    val newSizeValue = desFile.length()
/*
                                    Log.e("chat Activity",(
                                        "compression Size after : %s",
                                        getFileSize(newSizeValue)
                                    )
*/
                                    time[0] = System.currentTimeMillis() - time[0]!!
/*
                                    Log.e("chat Activity",(
                                        "compression timeTaken Duration: %s",
                                        DateUtils.formatElapsedTime(time[0]!! / 1000)
                                    )
*/
                                    path[0] = desFile.path
                                    //                                if (desFile.length() <= 150000000) {
                                    picturePath = desFile.toString()
/*
                                    Log.e("chat Activity",(
                                        "compression finalFile %s", """${desFile.length()} 
             result ${Gson().toJson(result.data?.data)}"""
                                    )
*/
                                    SendImageToChat("video", picturePath, mVideoUri)
/*
                                    Log.e("chat Activity",("compression mVideoUri  mVideoUri $mVideoUri")
                                    //                                } else {
                                    //                                    showVideoSizeDialog();
                                    //                                }
                                    Handler(mainLooper).postDelayed({ progressDialog.hide() }, 50)
*/
                                }

                                override fun onFailure() {
                                    Log.e("chat Activity",("compression has been onFailure")
                                    Handler(mainLooper).postDelayed({ progressDialog.hide() }, 50)
                                }

                                override fun onProgress(v: Float) {
                                    //                           Log.e("chat Activity",("Video compression  onProgress "+v);
                                    progressDialog.progress = v.toInt()
                                }

                                override fun onCancelled() {
                                    Log.e("chat Activity",("compression has been cancelled")
                                }
                            }, VideoQuality.HIGH, false)
                        } else {
                            Toast.makeText(this, "empty video", Toast.LENGTH_SHORT).show();
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {
                    showVideoSizeDialog()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    var audioActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            if (result.data != null) {
                mAudioUri = result.data?.data
                var finalFile: File? = null
                try {
                    finalFile = getFile(this@ChatActivity, mAudioUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (finalFile != null) {
                    Log.e("chat Activity",(
                        "compression audio size: %s",
                        getFileSize(finalFile.length())
                    )
                    if (finalFile.length() <= 150000000) {
                        SendImageToChat("audio", finalFile.path, mAudioUri)
                    } else {
                        showAudioSizeDialog()
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide() // hide the title bar
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        AppConfig.setLocale(AppConfig.loadLanguage(this@ChatActivity), this)
        binding = ChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this
        executor = Executors.newSingleThreadExecutor()
        val db = AppDatabase.getInstance(application)
        chatDao = db?.chatDao()
        gson = Gson()
        user_id = SharedPrefsHelper.getInstance(this).getUserData()._id
        //        binding.llMainLayout.setEnabled(false);
        if (intent != null) {
            if (intent.hasExtra("from")) from = intent.getStringExtra("from")
            chatside = intent.getStringExtra("chatside")
        }
        if (from.equals("insurance", ignoreCase = true)) {
            binding.helplinelogo.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_logo_splash
                )
            )
            domain = "INSURANCE"
        } else {
            domain = "TVHELPLINE"
            binding.helplinelogo.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_helpline
                )
            )
        }
        user_token = AppConfig.getStringPreferences(mContext, "loginToken") ?: ""
        /*        Log.e("chat Activity",("user_id: %s", user_id);
        Log.e("chat Activity",("roomId: %s", roomId);
        Log.e("chat Activity",("user_token: %s", user_token);*/
        audioRecordingSetUp()
        setUpClickListeners()
        setUpChatRecycler()
        socketConnect()
        binding.textMessage.setOnTouchListener { v: View?, event: MotionEvent? ->
            binding.textMessage.requestLayout()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            false
        }
        try {
            NetWatch.builder(this) //                .setIcon(R.drawable.ic_signal_wifi_off)
                .setCallBack(object : NetworkChangeReceiver_navigator {
                    override fun onConnected(source: Int) {
                        // do some thing
                        try {
                            if (mSocket != null && mSocket.connected()) {
                                updateUnReadMesages()
                                mSocket.disconnect()
                                mSocket.off("JOIN_ROOM", joinRoom)
                                mSocket.off(Socket.EVENT_CONNECT, onConnect)
                                mSocket.off(Socket.EVENT_DISCONNECT, onReConnect)
                                mSocket.off("NEW_MESSAGE", onNewMessageReceived)
                                mSocket.off("ROOM_DETAILS", onRoomDetail)
                                mSocket.off("DISPLAY_TYPING", onDisplayTyping)
                                mSocket.off("MESSAGE_DELIVERED", onMESSAGE_DELIVERED)
                                mSocket.off("MESSAGE_READ_ALL", onMESSAGE_READ_ALL)
                                mSocket == null
                                Log.e("chat Activity",(" chat onDestroy")
                            }
                            socketConnect()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                        //                        Toast.makeText(ChatActivity.this, getResources().getString(R.string.you_re_online), Toast.LENGTH_SHORT).show();
                    }

                    override fun onDisconnected(): View? {
                        // do some other stuff
//                        Toast.makeText(ChatActivity.this, getResources().getString(R.string.you_re_offline), Toast.LENGTH_SHORT).show();
                        return null //To display a dialog simply return a custom view or just null to ignore it
                    }
                })
                .setNotificationCancelable(true)
                .setNotificationEnabled(false)
                .build()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun socketConnect() {
        try {
            val options = IO.Options()
            options.forceNew = true
            options.reconnection = true
            options.query = "?userId=$user_id"
            Log.e("chat Activity",("options.query: %s", options.query)
            mSocket = IO.socket(Const.SOCKET_URL)
            mSocket.connect()
            if (mSocket != null) {
//                mSocket.connect();
                mSocket.on("JOIN_ROOM", joinRoom)
                mSocket.on(Socket.EVENT_CONNECT, onConnect)
                mSocket.on(Socket.EVENT_DISCONNECT, onReConnect)
                mSocket.on("NEW_MESSAGE", onNewMessageReceived)
                mSocket.on("ROOM_DETAILS", onRoomDetail)
                mSocket.on("DISPLAY_TYPING", onDisplayTyping)
                mSocket.on("MESSAGE_DELIVERED", onMESSAGE_DELIVERED)
                mSocket.on("MESSAGE_READ_ALL", onMESSAGE_READ_ALL)
            }
            binding.btnSend.setOnClickListener { view: View? ->
                val msg = binding.textMessage.text.toString().trim { it <= ' ' }
                sendChatMessageType(msg, "text")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("chat Activity",("to server")
        }
    }


    private fun sendChatMessageType(msg: String, type: String) {
        try {
            if (!msg.equals("", ignoreCase = true)) {
                val `object` = JSONObject()
                `object`.put("from", user_id)
                `object`.put("domain", domain)
                `object`.put("sendFrom", "app")
                `object`.put("message", msg)
                `object`.put("roomId", roomId)
                `object`.put("type", type)
                val mobileTimeStamp = System.currentTimeMillis()
                val dayStr = createdAtNowString()
                `object`.put("mobileTimeStamp", mobileTimeStamp)
                val cd = ConnectionDetector(applicationContext)
                if (!cd.isConnectingToInternet) {
                    Toast.makeText(
                        this@ChatActivity,
                        resources.getString(R.string.intenet_connectivity),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (mSocket.connected()) {
                        if (isFirst) {
                            mSocket.emit("GROUP_MESSAGE", `object`)
                            Log.e("chat Activity",("GROUP_MESSAGE " + Gson().toJson(`object`))
                            setMessageModel(msg, type, null, mobileTimeStamp)
                            binding.textMessage.setText("")
                        } else {
                            Toast.makeText(
                                this@ChatActivity, "Please wait server is connecting",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        mSocket.connect()
                        Toast.makeText(
                            this@ChatActivity, "Please wait server is connecting",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        updateUnReadMesages()
    }

    private fun createdAtNowString(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val calendar = Calendar.getInstance()
        val date = calendar.time
        val dayStr = formatter.format(date.time)
        return dayStr
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.clear()
    }

    private fun showVideoSizeDialog() {
        val builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_Material_Dialog_Alert
        )
        builder.setMessage("Video Size should be less than 150mb.")
        builder.setTitle(getString(R.string.app_name))
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
        //        builder.setNeutralButton("Cancel", null);
        val dialog = builder.create()
        dialog.show()
    }

    private fun showAudioSizeDialog() {
        val builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_Material_Dialog_Alert
        )
        builder.setMessage("Audio Size should be less than 150mb.")
        builder.setTitle(getString(R.string.app_name))
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
        //        builder.setNeutralButton("Cancel", null);
        val dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("BinaryOperationInTimber")
    private fun setUpChatRecycler() {
        model = ArrayList()
        adapter = ChatAdapter(mContext, model, this) { success, data, retry ->
            var chat: ChatMessagesListModel? = null
            Log.e("chat Activity",("ChatAdapter chatDao " + data?.message + " mobileTimeStamp" + data.mobileTimeStamp)
            executor?.execute {
                chat = chatDao?.getChat(user_id, data.mobileTimeStamp ?: 0L, domain)
                Log.e("chat Activity",("ChatAdapter chatDao " + chat?.message + "  " + chat?.mobileTimeStamp)
                if (success) {
                    chat?.let {
                        val `object` = JSONObject()
                        `object`.put("from", user_id)
                        `object`.put("message", chat?.typeUri)
                        `object`.put("domain", domain)
                        `object`.put("sendFrom", "app")
                        `object`.put("roomId", roomId)
                        `object`.put("type", chat?.type)
                        `object`.put("mobileTimeStamp", chat?.mobileTimeStamp)
                        for (i in model.indices) {
                            val listModel = model[i]
                            if (listModel.type.equals(
                                    chat?.type,
                                    ignoreCase = true
                                ) && listModel.message?.isEmpty() == true
                                && chat?.mobileTimeStamp?.equals(
                                    listModel.mobileTimeStamp ?: 0L
                                ) == true
                            ) {
                                listModel.message = chat?.message
                                model[i].message = chat?.message
                                runOnUiThread {
                                    adapter?.notifyItemChanged(i, listModel)
                                }
                                break
                            }
                        }
                        mSocket.emit("GROUP_MESSAGE", `object`)
                        Log.e("chat Activity",("GROUP_MESSAGE chatDao " + gson?.toJson(`object`))
                    }
                }
                if (!success && retry) {
//                    chat?.let {
//                        val `object` = JSONObject()
//                        `object`.put("from", user_id)
//                        `object`.put("message", chat?.typeUri)
//                        `object`.put("domain", domain)
//                        `object`.put("sendFrom", "app")
//                        `object`.put("roomId", roomId)
//                        `object`.put("type", chat?.type)
//                        `object`.put("mobileTimeStamp", chat?.mobileTimeStamp)
//                        for (i in model.indices) {
//                            val listModel = model[i]
//                            if (listModel.type.equals(
//                                    chat?.type,
//                                    ignoreCase = true
//                                ) && listModel.message?.isEmpty() == true
//                                && chat?.mobileTimeStamp?.equals(
//                                    listModel.mobileTimeStamp ?: 0L
//                                ) == true
//                            ) {
//                                listModel.message = chat?.message
//                                model[i].message = chat?.message
//                                runOnUiThread {
//                                    adapter?.notifyItemChanged(i, listModel)
//                                }
//                                break
//                            }
//                        }
//                        mSocket.emit("GROUP_MESSAGE", `object`)
//                        Log.e("chat Activity",("GROUP_MESSAGE chatDao " + gson?.toJson(`object`))
//                    }
                }
            }

        }
        val layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL,
            true
        )
        layoutManager.orientation = RecyclerView.VERTICAL
        layoutManager.stackFromEnd = false
        layoutManager.reverseLayout = true
        layoutManager.isAutoMeasureEnabled = false
        binding.recyclerview.layoutManager = layoutManager
        setUpScrollListener()
//        adapter?.c(true)
        (Objects.requireNonNull(binding.recyclerview.itemAnimator) as SimpleItemAnimator).changeDuration =
            0
        (Objects.requireNonNull(binding.recyclerview.itemAnimator) as SimpleItemAnimator).addDuration =
            0
        (Objects.requireNonNull(binding.recyclerview.itemAnimator) as SimpleItemAnimator).moveDuration =
            0
        (Objects.requireNonNull(binding.recyclerview.itemAnimator) as SimpleItemAnimator).removeDuration =
            0
        binding.recyclerview.adapter = adapter
    }

    private fun safeEndAnimations(rv: RecyclerView?) {
        val itemAnimator = rv?.itemAnimator
        itemAnimator?.endAnimations()
    }

    private fun setUpScrollListener() {
        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = (binding.recyclerview.layoutManager as LinearLayoutManager?)!!
                currentVisible = layoutManager.childCount
                totalItems = layoutManager.itemCount
                val rem = orderTotalCount % perPage
                var totalPages = 0
                totalPages = if (rem <= perPage && rem >= 1) {
                    orderTotalCount / perPage + 1
                } else {
                    orderTotalCount / perPage
                }
                scrolledOutItems = layoutManager.findFirstVisibleItemPosition()
                if (totalItems == currentVisible + scrolledOutItems) {
                    if (page < totalPages) {
                        page += 1
//                        Log.e("chat Activity",("%s  onScrollStateChanged: onScrolled", TAG)
                        chatData
                        isScrolling = false
                    }
                }
            }
        })
    }

    private fun updateUnReadMesages() {
        if (unreadCount > 0) {
            try {
                val `object` = JSONObject()
                `object`.put("userId", user_id)
                `object`.put("roomId", roomId)
                `object`.put("domain", domain)
                `object`.put("sendFrom", "app")
                if (mSocket.connected()) {
                    mSocket.emit("RESET_UNREAD_COUNT_CLIENT", `object`)
                    unreadCount = 0
                    val broadcastIntent = Intent()
                    broadcastIntent.action = Const.HIT_NOTIFICATION_COUNT_API
                    LocalBroadcastManager.getInstance(applicationContext)
                        .sendBroadcast(broadcastIntent)
                    Log.e("chat Activity",("updateUnReadMesages();: RESET_UNREAD_COUNT_CLIENT %s", `object`)
                    for (i in model.indices) {
                        val listModel = model[i]
                        if (listModel.unreadCount > 0) {
                            model.remove(listModel)
                            adapter?.notifyItemRemoved(i)
                            return
                        }
                    }
                    showNoChatDataMessage()
                } else {
                    mSocket.connect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun sendunReadMesagesEventEarly() {
        if (unreadCount > 0) {
            try {
                val `object` = JSONObject()
                `object`.put("userId", user_id)
                `object`.put("roomId", roomId)
                `object`.put("domain", domain)
                `object`.put("sendFrom", "app")
                if (mSocket.connected()) {
                    mSocket.emit("RESET_UNREAD_COUNT_CLIENT", `object`)
//                    unreadCount = 0
                    val broadcastIntent = Intent()
                    broadcastIntent.action = Const.HIT_NOTIFICATION_COUNT_API
                    LocalBroadcastManager.getInstance(applicationContext)
                        .sendBroadcast(broadcastIntent)
                    Log.e("chat Activity",("updateUnReadMesages();: RESET_UNREAD_COUNT_CLIENT %s", `object`)
                } else {
                    mSocket.connect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setUpClickListeners() {
        binding.imgDelete.setOnClickListener { view: View? ->
            val popupMenu =
                PopupMenu(ContextThemeWrapper(mContext, R.style.popup), binding.imgDelete)
            popupMenu.menuInflater.inflate(R.menu.popup_menu_delete, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                // Toast message on menu item clicked
                if (menuItem.itemId == R.id.clear_history) {
                    deleteChatData()
                    return@setOnMenuItemClickListener true
                }
                true
            }
            // Showing the popup menu
            popupMenu.show()
        }
        binding.btnPdf.setOnClickListener { v: View? ->
            userChoosenTask = ""
            val result = checkMultiplePermission(mContext)
            if (result) selectImage()
        }
        binding.btnImage.setOnClickListener { view: View? ->
            val result = checkMultiplePermission(mContext)
            userChoosenTask = "Take Photo"
            if (result) cameraIntent()
        }
        binding.backButton.setOnClickListener { view: View? -> onBackPressed() }
        binding.textMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                try {
                    val `object` = JSONObject()
                    `object`.put("userId", user_id)
                    `object`.put("roomId", roomId)
                    `object`.put("domain", domain)
                    `object`.put("sendFrom", "app")
                    if (mSocket != null) {
//                        mSocket.connect();
                        mSocket.emit("TYPING", `object`)
                    }
                    if (s.isNotEmpty()) {
                        binding.btnSend.visibility = View.VISIBLE
                        binding.recordButton.visibility = View.GONE
                    } else {
                        binding.btnSend.visibility = View.GONE
                        binding.recordButton.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun audioRecordingSetUp() {
        AudioSenseiListObserver.getInstance().registerLifecycle(lifecycle)
        audioRecorder = AudioRecorder()
        binding.recordButton.setRecordView(binding.recordView)
        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8
        binding.recordView.cancelBounds = 8f
        binding.recordView.setSmallMicColor(Color.parseColor("#c2185b"))
        binding.recordButton.isListenForRecord = true
        //prevent recording under one Second
        binding.recordView.setLessThanSecondAllowed(false)
        binding.recordView.setSlideToCancelText("Slide To Cancel")
        binding.recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0)
        binding.recordButton.setOnRecordClickListener { v: View? ->
            if (isRecording) return@setOnRecordClickListener
//            Toast.makeText(this@ChatActivity, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show()
            Timber.d("RECORD BUTTON CLICKED")
        }
        var mLastTimeClick = 0L
        binding.recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                binding.recordView.visibility = View.VISIBLE
                binding.editlayout.visibility = View.GONE
                recordFile = File(filesDir, UUID.randomUUID().toString() + ".mp3")
                try {
                    if (!isRecording) {
                        isRecording = true
                        audioRecorder?.start(recordFile?.path)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    isRecording = false
                }
                Timber.d("onStart")
//                Toast.makeText(this@ChatActivity, "OnStartRecord", Toast.LENGTH_SHORT).show()
                val timeElasped = SystemClock.elapsedRealtime() - mLastTimeClick
                if (mLastTimeClick == 0L || timeElasped > 2000L) {
                    mLastTimeClick = SystemClock.elapsedRealtime()
                    Toast.makeText(
                        this@ChatActivity,
                        getString(R.string.hold_to_record_release_to_send),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


            override fun onCancel() {
                binding.recordView.visibility = View.GONE
                binding.editlayout.visibility = View.VISIBLE
                stopRecording(true)
//                Toast.makeText(this@ChatActivity, "onCancel", Toast.LENGTH_SHORT).show()
                Timber.d("onCancel")
                isRecording = false
            }

            override fun onFinish(recordTime: Long, limitReached: Boolean) {
                try {
                    stopRecording(false)
                    binding.recordView.visibility = View.GONE
                    binding.editlayout.visibility = View.VISIBLE
                    val time = getHumanTimeText(recordTime)
                    picturePath = recordFile?.path
                    SendImageToChat("audio", picturePath, null)
                    Timber.d("onFinish Limit Reached? $limitReached")
                    Timber.d(time)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                isRecording = false
            }

            override fun onLessThanSecond() {
                try {
                    stopRecording(true)
                    binding.recordView.visibility = View.GONE
                    binding.editlayout.visibility = View.VISIBLE
//                    Toast.makeText(this@ChatActivity, "OnLessThanSecond", Toast.LENGTH_SHORT).show()
                    Timber.d("onLessThanSecond")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                isRecording = false
            }
        })
        binding.recordView.setOnBasketAnimationEndListener { Timber.d("Basket Animation Finished") }
        binding.recordView.setRecordPermissionHandler {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return@setRecordPermissionHandler true
            }

//            boolean recordPermissionAvailable = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO) == PERMISSION_GRANTED;
//            if (recordPermissionAvailable) {
//                return true;
//            }
//
//            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
//            return false;
//        });
            val result = checkMultiplePermissionRecording(mContext)
            result
        }
    }

    fun checkMultiplePermissionRecording(context: Context?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Do something, when permissions not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    // If we should give explanation of requested permissions

                    // Show an alert dialog here with request explanation
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("RECORD AUDIO permissions are required to do the task.")
                    builder.setTitle("Please grant those permissions")
                    builder.setPositiveButton("OK") { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.RECORD_AUDIO),
                            MY_PERMISSIONS_REQUEST_AUDIO_RECORD
                        )
                    }
                    builder.setNegativeButton("Cancel", null)
                    val dialog = builder.create()
                    dialog.show()
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getColor(R.color.black))
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getColor(R.color.black))
                } else {
                    // Directly request for required permissions, without explanation
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        MY_PERMISSIONS_REQUEST_AUDIO_RECORD
                    )
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    private fun deleteChatData() {
        try {
            Log.e("chat Activity",("roomId%s", roomId)
            ProjectUtils.showProgressDialog(mContext, true, getString(R.string.please_wait))
            ApiClient.getApiService(mContext).room_delet_chat(roomId, user_token)
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        // Utility.hideProgressHud();
                        var respo: String? = null
                        try {
                            if (response != null) {
//                                assert(response.body() != null)
                                respo = response.body()?.string()
                                val jsonObject = JSONObject(respo)
                                Log.e("chat Activity",("respo$respo")
                                if (jsonObject.getInt("status") == 200) {
                                    model.clear()
                                    adapter?.notifyDataSetChanged()
                                } else {
                                    Toast.makeText(
                                        mContext,
                                        "Oops something went wrong",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                showNoChatDataMessage()
                            }
                            ProjectUtils.pauseProgressDialog()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            ProjectUtils.pauseProgressDialog()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        t.printStackTrace()
                        ProjectUtils.pauseProgressDialog()
                    }
                })
        } catch (e: Exception) {
            ProjectUtils.pauseProgressDialog()
            e.printStackTrace()
        }
    }

    private fun setMessageModel(
        msg: String,
        text: String,
        videouri: String?,
        mobileTimeStamp: Long
    ): ChatMessagesListModel {
        Log.e("chat Activity",("setMessageModel: %s", text)
        Log.e("chat Activity",("onDateSet: %s", mobileTimeStamp)
        val listModel = ChatMessagesListModel()
        listModel.type = text
        val from = From()
        listModel.message = msg
//        if (text.equals("video", ignoreCase = true) || text.equals("image", ignoreCase = true)) {
        listModel.typeUri = videouri
//        }
        listModel.id = user_id
//        listModel.user_id = user_id
        listModel.roomId = roomId
        from.id = user_id
        listModel.from = from
        listModel.createdAt = createdAtNowString()
        listModel.mobileTimeStamp = mobileTimeStamp
        listModel.domain = domain
        model.add(0, listModel)
        refreshData(listModel)
        executor?.execute {
            chatDao?.insert(listModel)
        }
        return listModel
    }

    private fun refreshData(listModel: ChatMessagesListModel) {
        adapter?.notifyItemInserted(0)
        binding.recyclerview.smoothScrollToPosition(0)
        showNoChatDataMessage()
    }

    override fun onResume() {
        super.onResume()
        try {
            val application = application as App
            val mTracker = application.defaultTracker
            mTracker?.setScreenName("Screen~" + "Transport Screen")
            mTracker?.send(ScreenViewBuilder().build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!mSocket.connected()) {
            mSocket.connect()
        } else {
            showProgress(false)
        }
    }//                                        binding.recyclerview.scrollToPosition(0);

    //                                if (modelNew.getUnreadCount()>0)
    //                                model.add(modelNew);
    private val chatData: Unit
        get() {
            try {
                showProgress(true)
                ApiClient.getApiService(mContext)
                    .room_chat_list(roomId, user_token, perPage, page, "app")
                    .enqueue(object : Callback<ResponseBody?> {
                        override fun onResponse(
                            call: Call<ResponseBody?>,
                            response: Response<ResponseBody?>
                        ) {
                            var respo: String? = null
                            try {
//                                assert(response.body() != null)
                                respo = response.body()?.string()
                                val jsonObject = JSONObject(respo ?: "")
                                Log.e("chat Activity",("room_chat_list %s", respo?.length)
                                if (jsonObject.getInt("status") == 200) {
                                    val data = jsonObject.getJSONObject("data")
                                    orderTotalCount = data.getInt("total")
                                    unreadCount = data.getInt("unread")
                                    Log.e("chat Activity",(
                                        "orderTotalCount unread %d  %d ",
                                        orderTotalCount,
                                        unreadCount
                                    )
                                    val chatMessagesListModels =
                                        gson?.fromJson<List<ChatMessagesListModel>>(
                                            jsonObject.getJSONObject("data").getJSONArray("data")
                                                .toString(),
                                            object :
                                                TypeToken<ArrayList<ChatMessagesListModel?>?>() {}.type
                                        )
                                    modelTemp.addAll(chatMessagesListModels!!)
                                    model.clear()

                                    for (modelNew in modelTemp) {
                                        modelNew.unreadCount = 0
                                        if (tenpDate.isNotEmpty() && !tenpDate.equals(
                                                "",
                                                ignoreCase = true
                                            )
                                        ) {
                                            val `val` =
                                                Utils.compareTwoDate(tenpDate, modelNew.createdAt)
                                            if (`val`) {
                                                tenpDate = modelNew.createdAt.toString()
                                                modelNew.messageDate = ""
                                                model.add(modelNew)
                                            } else {
                                                tenpDate = modelNew.createdAt.toString()
                                                val time = Utils.dateToLongCovvert(tenpDate)
                                                val ddd = Utils.getFormattedDateTime(
                                                    time,
                                                    this@ChatActivity
                                                )

                                                model.add(modelNew)

                                                val dateModel = modelNew.deepCopy()
                                                dateModel.type = "date"
                                                dateModel.messageDate = ddd

                                                model.add(dateModel)
                                            }
                                        } else {
                                            tenpDate = modelNew.createdAt.toString()
                                            val time = Utils.dateToLongCovvert(tenpDate)
                                            val ddd =
                                                Utils.getFormattedDateTime(time, this@ChatActivity)
                                            modelNew.messageDate = ddd
                                            model.add(modelNew)
                                        }
                                    }

                                    if (chatMessagesListModels.isNotEmpty()) {
                                        if (page == 1) {
                                            if (model.isNotEmpty() && !request) {
                                                executor?.execute {
                                                    val list = chatDao?.getChatList(user_id, domain)
                                                    Log.e("chat Activity",(
                                                        "room_chat_list getChatList1 %s",
                                                        list?.size.toString() + " " + model.get(0).message
                                                    )

                                                    if (list != null) {
                                                        for (i in model.indices) {
                                                            list.map {
                                                                if (it.type == model[i].type && it.mobileTimeStamp == model[i].mobileTimeStamp) {
                                                                    chatDao?.getChat(
                                                                        user_id,
                                                                        it.mobileTimeStamp
                                                                            ?: 0L,
                                                                        domain
                                                                    )
                                                                        ?.let {
                                                                            chatDao?.delete(it)
                                                                        }
                                                                    return@map
                                                                }
                                                            }

                                                        }
                                                    }
                                                    val list2 =
                                                        chatDao?.getChatList(user_id, domain)
                                                    Log.e("chat Activity",(
                                                        "room_chat_list getChatList2 %s",
                                                        list2?.size.toString() + " " + model.get(0).message
                                                    )
                                                    if (list2 != null) {
                                                        model.addAll(0, list2)
                                                    }

                                                    model.sort()
                                                    if (unreadCount > 0) {
                                                        val unreadModel = ChatMessagesListModel()
                                                        unreadModel.unreadCount = unreadCount
                                                        unreadModel.type = "text"
                                                        val from = From()
                                                        unreadModel.message = ""
                                                        unreadModel.id = user_id
//                                        unreadModel.user_id = user_id
                                                        unreadModel.roomId = roomId
                                                        from.id = user_id
                                                        unreadModel.from = from
                                                        if (model.size >= unreadCount) model.add(
                                                            unreadCount,
                                                            unreadModel
                                                        ) else model.add(
                                                            model.size, unreadModel
                                                        )
                                                    }
                                                    runOnUiThread {
                                                        adapter?.notifyDataSetChanged()
                                                        binding.recyclerview.scrollToPosition(0);
                                                    }
                                                    list2?.map {
                                                        if (it.type?.equals("text") == true) {
                                                            val `object` = JSONObject()
                                                            `object`.put("from", user_id)
                                                            `object`.put("domain", domain)
                                                            `object`.put("sendFrom", "app")
                                                            `object`.put("message", it.message)
                                                            `object`.put("roomId", roomId)
                                                            `object`.put("type", it.type)
                                                            `object`.put(
                                                                "mobileTimeStamp",
                                                                it.mobileTimeStamp
                                                            )
                                                            mSocket.emit("GROUP_MESSAGE", `object`)
                                                        }
                                                    }
                                                }
                                            }
                                            sendunReadMesagesEventEarly()
                                        } else {
                                            model.sort()
                                            adapter?.notifyDataSetChanged()
                                        }
                                    }
                                    showNoChatDataMessage()
                                    showProgress(false)
                                    request = true
                                } /*else if (jsonObject.getInt("status") == 400) {
                                    val error = JSONObject(response.errorBody()?.string() ?: "")
                                    val retrofitMesage = error.getString("message")
                                    Toast.makeText(
                                        this@ChatActivity,
                                        retrofitMesage ?: "something went wrong, please try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } */ else {
                                    val retrofitMesage = jsonObject.getString("message")
                                    Toast.makeText(
                                        mContext,
                                        retrofitMesage ?: "something went wrong, please try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                showProgress(false)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                showProgress(false)
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                            t.printStackTrace()
                            showProgress(false)
                        }
                    })
            } catch (e: Exception) {
                showProgress(false)
                e.printStackTrace()
            }
        }

    override fun onBackPressed() {
        super.onBackPressed()
        try {
            val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
            //            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            val taskList = am.getRunningTasks(10)
            Log.e("chat Activity",("activitieNum %s", taskList[0].numActivities)
            if (taskList[0].numActivities == 1 && taskList[0].topActivity!!.className == this.javaClass.name) {
                Timber.i("This is last activity in the stack")
                val intent = Intent(this, BottomActivity::class.java)
                intent.putExtra("home", "1")
                intent.putExtra("videoId", "")
                intent.putExtra("vimeoLink", "")
                startActivity(intent)
                finish()
            } else {
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

    override fun onDestroy() {
        NetWatch.unregister(this)
        if (mSocket != null && mSocket.connected()) {
            updateUnReadMesages()
            mSocket.disconnect()
            mSocket.off("JOIN_ROOM", joinRoom)
            mSocket.off(Socket.EVENT_CONNECT, onConnect)
            mSocket.off(Socket.EVENT_DISCONNECT, onReConnect)
            mSocket.off("NEW_MESSAGE", onNewMessageReceived)
            mSocket.off("ROOM_DETAILS", onRoomDetail)
            mSocket.off("DISPLAY_TYPING", onDisplayTyping)
            mSocket.off("MESSAGE_DELIVERED", onMESSAGE_DELIVERED)
            mSocket.off("MESSAGE_READ_ALL", onMESSAGE_READ_ALL)

            mSocket == null
            Log.e("chat Activity",(" chat onDestroy")
        }
        super.onDestroy()
    }

    private val onNewMessageReceived = Emitter.Listener { objects ->
        try {
            updateUnReadMesages()
            var message = JSONObject()
            message = objects[0] as JSONObject
            Log.e("chat Activity",("onNewMessageReceived: %s", message)
            val listModel =
                gson?.fromJson(message.toString(), ChatMessagesListModel::class.java)
            if (listModel?.from?.id != user_id) {
                var addMssg = true
                for (i in model.indices) {
                    Log.e("chat Activity",("notifyItemChanged: listModel.from?.id == user_id  $i ${listModel?.id}")
                    if (listModel?.id == model[i].id) {
                        addMssg = false
                        Log.e("chat Activity",("notifyItemChanged: addMssg.from?.id != user_id  $i")
                        break
                    }
                    if (i > 100) {
                        Log.e("chat Activity",("notifyItemChanged: addMssg.break  $i")
                        break
                    }
                }
                runOnUiThread {
                    if (addMssg) {
                        listModel?.let {
                            model.add(0, it)
                            adapter?.notifyItemInserted(0)
                            binding.recyclerview.scrollToPosition(0)
                            showNoChatDataMessage()
                            val `object` = JSONObject()
                            `object`.put("userId", user_id)
                            `object`.put("from", listModel.from?.id)
                            `object`.put("roomId", roomId)
                            `object`.put("domain", domain)
                            `object`.put("type", "read")
                            `object`.put("sendFrom", "app")
                            `object`.put("messageId", listModel.id ?: "")
                            val dayStr = createdAtNowString()
                            `object`.put("mobileTimeStamp", System.currentTimeMillis())
                            if (mSocket != null) {
                                mSocket.emit("DELIVERED", `object`)
                                Log.e("chat Activity",("DELIVERED: " + gson?.toJson(`object`))
                            }
                        }
                    }
                }
            } else if (listModel.from?.id == user_id) {
                for (i in model.indices) {
                    Log.e("chat Activity",("notifyItemChanged: listModel.from?.id == user_id  $i ${listModel.mobileTimeStamp}")
                    if (listModel.type == model[i].type && listModel.mobileTimeStamp == model[i].mobileTimeStamp) {
                        executor?.execute {
                            chatDao?.getChat(
                                user_id,
                                listModel.mobileTimeStamp ?: 0L,
                                domain
                            )
                                ?.let {
                                    chatDao?.delete(it)
                                }
                        }
                        runOnUiThread {
                            model[i] = listModel
                            adapter?.notifyItemChanged(i)
                        }
                        Log.e("chat Activity",("notifyItemChanged: listModel.from?.id == user_id  $i")
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showNoChatDataMessage() {
        runOnUiThread {
            if (model != null && model.isNotEmpty()) binding.noChatData.visibility =
                View.GONE else binding.noChatData.visibility = View.VISIBLE
        }
    }

    private val joinRoom = Emitter.Listener { objects ->
        Log.e(TAG, "joinRoom: $objects")
        runOnUiThread {
            try {
                val `object` = JSONObject()
                `object`.put("from", user_id)
                `object`.put("domain", domain)
                `object`.put("sendFrom", "app")
                //                        mSocket.connect();
                mSocket.emit("JOIN_ROOM_CLIENT", `object`)
                //                        Log.e(TAG, "joinRoom: " + mSocket.connected());
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private val onConnect = Emitter.Listener {
        val `object` = JSONObject()
        try {
            `object`.put("from", user_id)
            `object`.put("domain", domain)
            `object`.put("sendFrom", "app")
            if (mSocket != null && mSocket.connected()) {
                Log.e("chat Activity",("JOIN_ROOM_CLIENT: %s", Gson().toJson(`object`))
                mSocket.emit("JOIN_ROOM_CLIENT", `object`)
                showProgress(true)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        //            mSocket.connect();
    }
    private val onReConnect = Emitter.Listener {
        try {
            /* runOnUiThread(() -> {
                    Toast.makeText(mContext, "ReConnecting", Toast.LENGTH_SHORT).show();
                });*/
            val cd = ConnectionDetector(applicationContext)
            if (cd.isConnectingToInternet) {
                if (mSocket != null && !mSocket.connected()) {
                    Log.e("chat Activity",("onReConnect: ")
                    mSocket.connect()
                    showProgress(true)
                } else {
                    socketConnect()
                }
            }
            if (mSocket != null) Log.e("chat Activity",("onReConnect() returned: %s", mSocket.connected())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //            mSocket.connect();
    }
    private val onRoomDetail =
        Emitter.Listener { objects -> // Toast.makeText(mContext,"joinRoom",Toast.LENGTH_SHORT).show();
            runOnUiThread {
                try {
                    Log.e("chat Activity",("onRoomDetail: %s", *objects)
                    var room = JSONObject()
                    room = objects[0] as JSONObject
                    roomId = room.optString("roomId")
                    showProgress(false)
                    AppConfig.setStringPreferences(mContext, "roomId", roomId)
                    if (isFirst) {
                        Log.e("chat Activity",("run: ALREADY CALLED!!!!!!!")
                    } else {
                        isFirst = true
                        chatData
                    }
                    Log.e("chat Activity",("onRoomDetail: %s", room)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    var handler: Handler? = null

    @SuppressLint("SetTextI18n")
    private val onDisplayTyping = Emitter.Listener { objects: Array<Any> ->
        runOnUiThread {
            try {
                Log.e("chat Activity",("onDisplayTyping: %s", Gson().toJson(objects))
                val typeingModel = objects[0] as JSONObject
                runOnUiThread {
                    if (typeingModel.has("_id")) {
                        var id: String? = null
                        try {
                            id = typeingModel.getString("_id")
                            val name = typeingModel.getString("fname_en")
                            if (id != user_id) {
                                binding.typingTxt.visibility = View.VISIBLE
                                binding.typingTxt.text =
                                    String.format("%s is typing...", name)
                            } else {
                                binding.typingTxt.visibility = View.INVISIBLE
                            }
                            if (binding.typingTxt.visibility == View.VISIBLE && handler == null) {
                                handler = Handler(Looper.getMainLooper())
                                handler?.postDelayed({
                                    binding.typingTxt.visibility = View.INVISIBLE
                                    handler = null
                                }, 2000)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private val onMESSAGE_DELIVERED = Emitter.Listener { objects: Array<Any> ->
        runOnUiThread {
            try {
                Log.e("chat Activity",("onMESSAGE_DELIVERED: %s", Gson().toJson(objects))
                val message = objects[0] as JSONObject
                val listModel =
                    gson?.fromJson(message.toString(), ChatMessagesListModel::class.java)
                val user_id = AppConfig.getStringPreferences(mContext, "user_id")
                if (listModel?.from?.id == user_id) {
                    for (i in model.indices) {
                        if (model[i].id != null && model[i].id.equals(
                                listModel?.id ?: ""
                            ) || model[i].mobileTimeStamp == listModel?.mobileTimeStamp
                        ) {
                            if (listModel != null) {
                                model[i] = listModel
                                adapter?.notifyItemChanged(i)
                                break
                            }

                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private val onMESSAGE_READ_ALL = Emitter.Listener { objects: Array<Any> ->
        runOnUiThread {
            try {
                Log.e("chat Activity",("onMESSAGE_READ_ALL: %s", Gson().toJson(objects))
                val message = objects[0] as JSONObject
                val all = message.getBoolean("All")
                val roomId = message.getString("roomId")
                val user_id = AppConfig.getStringPreferences(mContext, "user_id")
                Log.e("chat Activity",("onMESSAGE_READ_ALL: $all $roomId")

                if (roomId == this.roomId && all) {
                    for (i in model.indices) {
//                        if (listModel?.type == model[i].type && listModel?.id == model[i].id) {
//                        if (model[i].id != null && model[i].id.equals(listModel?.id ?: "")) {
                        model[i].members?.map {
                            model[i].unassignedRead = true
                            if (it.userId != user_id) {
                                it.delivered = true
                                it.read = true
                            }
                        }
//                                model[i] = listModel

//                        }
                    }
                    adapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //---------image---------------
    @SuppressLint("LogNotTimber")
    private fun selectImage() {
        //            getString(R.string.camera),
        val items = arrayOf<CharSequence>(
            getString(R.string.gallery),
            getString(R.string.document),
            getString(R.string.video),
            getString(R.string.audio),
            getString(R.string.loaction),
            getString(R.string.contact)
        )
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle(R.string.share_chat)
        builder.setItems(items) { dialog: DialogInterface, item: Int ->
            val result = checkMultiplePermission(mContext)
            /* if (items[item] == getString(R.string.camera)) {
                 userChoosenTask = getString(R.string.camera)
                 if (result) cameraIntent()
             } else*/
            if (items[item] == getString(R.string.gallery)) {
                userChoosenTask = getString(R.string.gallery)
                if (result) galleryIntent()
            } else if (items[item] == getString(R.string.document)) {
                userChoosenTask = getString(R.string.document)
                if (result) pdfIntent()
            } else if (items[item] == getString(R.string.video)) {
                userChoosenTask = getString(R.string.video)
                if (result) videoIntent()
            } else if (items[item] == getString(R.string.audio)) {
                userChoosenTask = getString(R.string.audio)
                if (result) audioIntent()
            } else if (items[item] == getString(R.string.cancel_task)) {
                dialog.dismiss()
            } else if (items[item] == getString(R.string.contact)) {
                userChoosenTask = getString(R.string.contact)
                if (result) smsIntent()
            } else if (items[item] == getString(R.string.loaction)) {
                locationShare = true
                userChoosenTask = getString(R.string.loaction)
                if (result) {
                    selectLocationOnMap()
                }
            }
        }
        builder.show()
    }

    fun selectLocationOnMap() {
        val apiKey = getString(R.string.googleMapApiKey)
        val intent = Intent(this, MapActivity::class.java)

        val bundle = Bundle()
        bundle.putString(SimplePlacePicker.API_KEY, apiKey)

        /*   try {
               if (latitude != null && longitude != null && latitude.toDouble() != 0.0 && longitude.toDouble() != 0.0) {
                   bundle.putDouble(SimplePlacePicker.LATITUDE, latitude.toDouble())
                   bundle.putDouble(SimplePlacePicker.LONGITUDE, longitude.toDouble())
               }
           } catch (e: java.lang.Exception) {
               e.printStackTrace()
           }*/

        intent.putExtras(bundle)
        startLocationActivityForResult.launch(intent)
//        fragment.startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE)
    }

    private fun smsIntent() {
        try {
            val contactPickerIntent =
                Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            startActivityForResult(contactPickerIntent, SELECT_CONTACT)
        } catch (e: ActivityNotFoundException) {
            try {
                val contactPickerIntent =
                    Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                startActivityForResult(contactPickerIntent, SELECT_CONTACT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
    }

    private fun pdfIntent() {
        val path =
            Environment.getExternalStorageDirectory().path + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator
        Log.e("chat Activity",("path  $path")
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val mimeTypes = arrayOf(
            "application/pdf",
            "text/plain",
            "application/msword",
            "application/vnd.ms-excel",
            "application/vnd.google-apps.document",
            "text/csv",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        )
        intent.setDataAndType(
            FileProvider.getUriForFile(
                this, "$packageName.provider",
                File(path)
            ), "application/pdf"
        )
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, SELECT_PDF)
    }

    private fun videoIntent() {
        mVideoUri = contentResolver.insert(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        )
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 150)
        intent.setDataAndType(mVideoUri, "video/*");
        videoActivityResultLauncher.launch(Intent.createChooser(intent, "Select Video"))
    }

    private fun audioIntent() {
        try {
            mAudioUri = contentResolver.insert(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            )
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            //        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 150);
            intent.type = "audio/*"
            //        intent.setAction(Intent.ACTION_GET_CONTENT);
            audioActivityResultLauncher.launch(Intent.createChooser(intent, "Select Audio"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cameraIntent() {
        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCameraPhotoUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraPhotoUri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    @SuppressLint("Recycle")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_FILE) {
//                    assert(data != null)
                    val uri = data?.data
                    picturePath = getRealPathFromURI(uri)
                    SendImageToChat("image", picturePath, null)
                    Log.e("chat Activity",(picturePath)
                } else if (requestCode == REQUEST_CAMERA) {
                    val finalFile = File(getRealPathFromURI(mCameraPhotoUri))
                    picturePath = finalFile.toString()
                    Log.e("chat Activity",("REQUEST_CAMERA finalFile %s", finalFile.length())
                    SendImageToChat("image", picturePath, null)
                    Log.e("chat Activity",(picturePath)
                } else if (requestCode == SELECT_PDF) {
                    val uri = data?.data
                    val cR = contentResolver
                    val mime = cR.getType(uri!!)
                    val f = getFile(this@ChatActivity, uri)
                    Log.e("chat Activity",("%s%s", f.path, mime)
                    if (mime?.contains("pdf") == true) SendImageToChat(
                        "pdf",
                        f.path,
                        null
                    ) else if (mime?.contains("audio") == true) SendImageToChat(
                        "audio",
                        f.path,
                        null
                    ) else if (mime?.contains("video") == true) SendImageToChat(
                        "video",
                        f.path,
                        null
                    ) else SendImageToChat("image", f.path, null)
                } else if (requestCode == REQUEST_AUDIO) {
                    val finalFile = File(getRealPathFromURI(mAudioUri))
                    picturePath = finalFile.toString()
                    Log.e("chat Activity",("REQUEST_AUDIO finalFile %s", finalFile.length())
                    SendImageToChat("audio", picturePath, null)
                    Log.e("chat Activity",(picturePath)
                } else if (requestCode == SELECT_CONTACT) {
                    val cursor: Cursor?
                    try {
                        val phoneNo: String
                        val name: String
                        val uri = data?.data
                        cursor = contentResolver.query(uri!!, null, null, null, null)
                        cursor?.moveToFirst()
                        val phoneIndex =
                            cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val nameIndex =
                            cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        phoneNo = phoneIndex?.let { cursor.getString(it) }!!
                        name = nameIndex?.let { cursor.getString(it) }!!
//                        val msg = """
//                            Contact
//                            Name: $name
//                            Phone: ${phoneNo.replace(" ", "")}
//                            """.trimIndent()
                        val msg = "$name,$phoneNo"
                        sendChatMessageType(msg, "contact")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        val mypath = generateRandomPassword(10)
        val path =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                mypath,
                null
            )
        return Uri.parse(path)
    }

    fun getRealPathFromURI(contentUri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri!!, projection, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(projection[0])
        val picture_Path = cursor.getString(columnIndex) // returns null
        cursor.close()
        return picture_Path
    }

    private fun getRealPathVideoURI(contentUri: Uri?): String {
        try {
            val projection = arrayOf(MediaStore.Video.Media.DATA)
            val cursor =
                contentUri?.let { contentResolver.query(it, projection, null, null, null) }
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(projection[0])
            val picture_Path = columnIndex?.let { cursor.getString(it) } // returns null
            cursor?.close()
            return picture_Path ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun getPath(uri: Uri?): String {
        try {
            var cursor = uri?.let { contentResolver.query(it, null, null, null, null) }
            cursor!!.moveToFirst()
            var document_id = cursor.getString(0)
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
            cursor.close()
            cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null
            )
            cursor?.moveToFirst()
            val path =
                cursor?.getString(cursor?.getColumnIndex(MediaStore.Video.Media.DATA) ?: 0)
            cursor?.close()
            return path ?: "{"
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun getRealPathAudioURI(contentUri: Uri?): String {
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = contentResolver.query(contentUri!!, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(projection[0])
        val picture_Path = cursor?.getString(columnIndex!!) // returns null
        cursor?.close()
        return picture_Path ?: ""
    }

    fun checkMultiplePermission(context: Context?): Boolean {
        Log.d(TAG, "checkMultiplePermission: ")
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.CAMERA
                ) + ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) + ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) + ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS
                ) +
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) + ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )) != PackageManager.PERMISSION_GRANTED
            ) {

                // Do something, when permissions not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_CONTACTS
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    // If we should give explanation of requested permissions

                    // Show an alert dialog here with request explanation
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(
                        "Camera, Read Contacts and Write External" +
                                " Storage permissions are required to do the task."
                    )
                    builder.setTitle("Please grant those permissions")
                    builder.setPositiveButton("OK") { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                    }
                    builder.setNegativeButton("Cancel", null)
                    val dialog = builder.create()
                    dialog.show()
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getColor(R.color.black))
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getColor(R.color.black))
                } else {
                    // Directly request for required permissions, without explanation
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                    //                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    private fun SendImageToChat(type: String, picturePath: String?, mVideoUri: Uri?) {
        try {
            var chatModel: ChatMessagesListModel? = null
            val mobileTimeStamp = System.currentTimeMillis()
            if (picturePath != null) {
                chatModel = setMessageModel("", type, picturePath, mobileTimeStamp)
            }
            if (chatModel != null) {
                uploadPictureJob(chatModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ProjectUtils.pauseProgressDialog()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (userChoosenTask.equals(
                        getString(R.string.camera),
                        ignoreCase = true
                    )
                ) cameraIntent() else if (userChoosenTask.equals(
                        getString(R.string.gallery),
                        ignoreCase = true
                    )
                ) galleryIntent() else if (userChoosenTask.equals(
                        getString(R.string.document),
                        ignoreCase = true
                    )
                ) {
                    pdfIntent()
                } else if (userChoosenTask.equals(
                        getString(R.string.audio),
                        ignoreCase = true
                    )
                ) {
                    audioIntent()
                } else if (userChoosenTask.equals(
                        getString(R.string.video),
                        ignoreCase = true
                    )
                ) {
                    videoIntent()
                } else if (userChoosenTask.equals(
                        getString(R.string.contact),
                        ignoreCase = true
                    )
                ) {
                    smsIntent()
                } else if (userChoosenTask.equals(
                        getString(R.string.loaction),
                        ignoreCase = true
                    )
                ) {
                    selectLocationOnMap()
                } else {
                    selectImage()
                }
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_CONTACTS
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                } else {
                    showNeverAskAgainAlert()
                }
            }
        } else if (requestCode == MY_PERMISSIONS_REQUEST_AUDIO_RECORD) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                } else {
                    showNeverAskAgainAlert()
                }
            }
        }
    }

    private fun showNeverAskAgainAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("You have previously declined this permission. You must approve this permission in Permissions in the app settings on your device.")
        builder.setTitle("Please grant those permissions")
        builder.setPositiveButton("Settings") { dialogInterface: DialogInterface?, i: Int ->
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                )
            )
        }
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.black))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.black))
    }

    //------------- record=======
    private fun stopRecording(deleteFile: Boolean) {
        if (isRecording) {
            audioRecorder?.stop()
            if (recordFile != null && deleteFile) {
                recordFile?.delete()
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun getHumanTimeText(milliseconds: Long): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(milliseconds),
            TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        )
    }

    private fun showProgress(show: Boolean) {
        runOnUiThread {
            if (show) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showProgressUpload(show: Boolean) {
        runOnUiThread {
            if (show) {
                binding.progressBarUpload.visibility = View.VISIBLE
            } else {
                binding.progressBarUpload.visibility = View.GONE
            }
        }
    }

    private val startLocationActivityForResult =
        registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
//            val result: String? = result.data?.getStringExtra("result")
//    Log.e("chat Activity",("startLocationActivityForResult "+ gson?.toJson(result?.data))
                currentLatitude =
                    result.data?.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA, 0.0)
                        ?: 0.0
                currentLongitude =
                    result.data?.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA, 0.0)
                        ?: 0.0
                                ?: 0.0
//            currentLatitude     = result.data?.getStringExtra("Latitude")?.toDouble() ?: 0.0
//            currentLongitude    = result.data?.getStringExtra("Longitude")?.toDouble()?: 0.0

                sendChatMessageType(
                    "$currentLatitude,$currentLongitude",
                    "location"
                )

            }
        }


    private fun statusCheck() {
        val manager =
            this@ChatActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this@ChatActivity)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
//                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.dismiss()
                enableLocationSettings()

            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
                statusCheck()
            }
        val alert: AlertDialog? = builder.create()
        if (alert != null) {
            if (!alert.isShowing) {
                alert.show()
                alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getColor(R.color.black))
                alert.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getColor(R.color.black))
            }
        }
    }

    private fun enableLocationSettings() {
        val locationRequest = LocationRequest.create()
            .setInterval(200)
            .setFastestInterval(200)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        LocationServices
            .getSettingsClient(this@ChatActivity)
            .checkLocationSettings(builder.build())
            .addOnSuccessListener(this@ChatActivity) { response: LocationSettingsResponse? -> }
            .addOnFailureListener(this@ChatActivity) { ex: java.lang.Exception? ->
                if (ex is ResolvableApiException) {
                    try {
                        // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                        ex.startResolutionForResult(this@ChatActivity, 100)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
    }


    override fun onStop() {
        super.onStop()
//        stopLocationUpdates()
    }

    companion object {
        private const val TAG = "ChatMessageActivity"
        const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
        const val MY_PERMISSIONS_REQUEST_AUDIO_RECORD = 124
        var user_id: String = ""

        @JvmField
        var domain = "TVHELPLINE"
        fun generateRandomPassword(len: Int): String {
            val chars = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                    + "lmnopqrstuvwxyz!@#$%&")
            val rnd = Random()
            val sb = StringBuilder(len)
            for (i in 0 until len) sb.append(chars[rnd.nextInt(chars.length)])
            return sb.toString()
        }

        @Throws(IOException::class)
        fun getFile(context: Context, uri: Uri?): File {
            val destinationFilename =
                File(context.filesDir.path + File.separatorChar + queryName(context, uri))
            try {
                context.contentResolver.openInputStream(uri!!).use { ins ->
                    createFileFromStream(
                        ins!!, destinationFilename
                    )
                }
            } catch (ex: Exception) {
                Log.e("chat Activity",(ex.message)
                ex.printStackTrace()
            }
            return destinationFilename
        }

        fun createFileFromStream(ins: InputStream, destination: File?) {
            try {
                FileOutputStream(destination).use { os ->
                    val buffer = ByteArray(4096)
                    var length: Int
                    while (ins.read(buffer).also { length = it } > 0) {
                        os.write(buffer, 0, length)
                    }
                    os.flush()
                }
            } catch (ex: Exception) {
                Log.e("chat Activity",(ex.message)
                ex.printStackTrace()
            }
        }

        private fun queryName(context: Context, uri: Uri?): String {
            val returnCursor =
                uri?.let { context.contentResolver.query(it, null, null, null, null) }
            val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor?.moveToFirst()
            val name = nameIndex?.let { returnCursor.getString(it) }
            returnCursor?.close()
            return name ?: ""
        }

        fun ResizeBitmap(
            bitmap: Bitmap,
            newWidth: Int
        ): Bitmap { //The picture taken is too large,
            // set the format size
            val width = bitmap.width
            val height = bitmap.height
            val temp = height.toFloat() / width.toFloat()
            val newHeight = (newWidth * temp).toInt()
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            val matrix = Matrix()
            // resize the bit map
            matrix.postScale(scaleWidth, scaleHeight)
            // matrix.postRotate(45);
            val resizedBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
            bitmap.recycle()
            return resizedBitmap
        }
    }

    private fun uploadPictureJob(model: ChatMessagesListModel) {
        val mediaMessageData = Data.Builder()
            .putString(WorkConstants.ARG_KEY_ACTION, WorkConstants.ACTION_UPDATE_PICTURE)
            .putString(WorkConstants.ARG_KEY_CHAT_MODEL, gson?.toJson(model))
            .build()
        val uniqueWorkName = model.mobileTimeStamp?.toString()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val mediaMessageWork = OneTimeWorkRequest.Builder(MediaMessageWork::class.java)
            .setConstraints(constraints)
            .setInputData(mediaMessageData)
            .build()
        if (uniqueWorkName != null) {
            WorkManager.getInstance(this)
                .enqueueUniqueWork(
                    uniqueWorkName,
                    ExistingWorkPolicy.KEEP,
                    mediaMessageWork
                )
        }
    }
}
