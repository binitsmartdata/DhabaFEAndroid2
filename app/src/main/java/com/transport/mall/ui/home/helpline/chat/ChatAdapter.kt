package com.transport.mall.ui.home.helpline.chat

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.transport.mall.R
import com.transport.mall.databinding.*
import com.transport.mall.model.ChatMessagesListModel
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import java.util.concurrent.TimeUnit

class ChatAdapter(
    internal var context: Context,
    private var chatModelList: List<ChatMessagesListModel>,
    private val lifecycleOwner: LifecycleOwner,
    var onUploadSuccessed: (success: Boolean, data: ChatMessagesListModel, retry: Boolean) -> Unit
) : RecyclerView.Adapter<ChatAdapter.BaseViewHolder>() {
    var dialogShown = false
    private var handler: Handler? = null


    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var lifecycleOwner: LifecycleOwner? = null
        abstract fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner)

        @JvmName("setLifecycleOwner1")
        fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
            this.lifecycleOwner = lifecycleOwner
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding: ViewBinding

        when (viewType) {
            CardType.AGENT_TEXT_MESSAGE.ordinal -> {
                binding = RowChatAgentMessageBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return AgentChatViewHolder(binding)
            }
            CardType.AGENT_IMAGE.ordinal -> {
                binding = RowChatAgentImageBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return AgentImageViewHolder(binding)
            }
            CardType.AGENT_VIDEO.ordinal -> {
                binding = RowChatAgentVideoBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return AgentVideoViewHolder(binding)
            }
            CardType.AGENT_AUDIO.ordinal -> {
                binding = RowChatAgentAudioBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return AgentAudioViewHolder(binding)
            }
            CardType.AGENT_PDF.ordinal -> {
                binding = RowChatAgentPdfBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return AgentPdfViewHolder(binding)
            }
            CardType.AGENT_CONTACT.ordinal -> {
                binding = RowChatAgentContactBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return AgentChatContactViewHolder(binding)
            }

            CardType.USER_TEXT_MESSAGE.ordinal -> {
                binding = RowChatUserMessageBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return UserChatViewHolder(binding)
            }
            CardType.USER_IMAGE.ordinal -> {
                binding = RowChatUserImageBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return UserImageViewHolder(binding)
            }
            CardType.USER_LOCATION.ordinal -> {
                binding = RowChatUserLocationBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return UserLocationViewHolder(binding)
            }
            CardType.USER_CONTACT.ordinal -> {
                binding = RowChatUserContactBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return UserChatContactViewHolder(binding)
            }
            CardType.USER_VIDEO.ordinal -> {
                binding = RowChatUserVideoBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return UserVideoViewHolder(binding)
            }
            CardType.USER_AUDIO.ordinal -> {
                binding = RowChatUserAudioBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return UserAudioViewHolder(binding)
            }
            CardType.USER_PDF.ordinal -> {
                binding = RowChatUserPdfBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return UserPdfViewHolder(binding)
            }
            CardType.CHAT_DATE.ordinal -> {
                binding = RowChatDateHeaderBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return ChatHeaderDateViewHolder(binding)
            }
            CardType.HEADER.ordinal -> {
                binding = RowChatHeaderBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                return ChatHeaderViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val chat = chatModelList[position]
        holder.setLifecycleOwner(lifecycleOwner)
        holder.bind(position, chat, lifecycleOwner)
    }

    override fun getItemCount(): Int {
        return chatModelList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (chatModelList[position].isShowAgent) {
            return if (chatModelList[position].type.equals("text")) {
                CardType.AGENT_TEXT_MESSAGE.ordinal
            } else if (chatModelList[position].type.equals("image")) {
                CardType.AGENT_IMAGE.ordinal
            } else if (chatModelList[position].type.equals("video")) {
                CardType.AGENT_VIDEO.ordinal
            } else if (chatModelList[position].type.equals("audio")) {
                CardType.AGENT_AUDIO.ordinal
            } else if (chatModelList[position].type.equals("pdf")) {
                CardType.AGENT_PDF.ordinal
            } else if (chatModelList[position].type.equals("contact")) {
                CardType.AGENT_CONTACT.ordinal
            } else if (chatModelList[position].type.equals("date")) {
                CardType.CHAT_DATE.ordinal
            } else {
                CardType.HEADER.ordinal
            }

        } else if (chatModelList[position].isShowCustomer) {
            return if (chatModelList[position].type.equals("text")) {
                CardType.USER_TEXT_MESSAGE.ordinal
            } else if (chatModelList[position].type.equals("image")) {
                CardType.USER_IMAGE.ordinal
            } else if (chatModelList[position].type.equals("video")) {
                CardType.USER_VIDEO.ordinal
            } else if (chatModelList[position].type.equals("audio")) {
                CardType.USER_AUDIO.ordinal
            } else if (chatModelList[position].type.equals("pdf")) {
                CardType.USER_PDF.ordinal
            } else if (chatModelList[position].type.equals("location")) {
                CardType.USER_LOCATION.ordinal
            } else if (chatModelList[position].type.equals("contact")) {
                CardType.USER_CONTACT.ordinal
            } else if (chatModelList[position].type.equals("date")) {
                CardType.CHAT_DATE.ordinal
            } else {
                CardType.HEADER.ordinal
            }
        } else {
            return CardType.HEADER.ordinal
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ChatHeaderDateViewHolder(val binding: RowChatDateHeaderBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {
            binding.tvDate.text = chat.messageDate
        }
    }

    inner class ChatHeaderViewHolder(val binding: RowChatHeaderBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {

            if (chat.isShowUnreadCount) {
                binding.unread.visibility = View.VISIBLE
                binding.unread.text = chat.unreadCount.toString() + "unread message"
            } else {
                binding.unread.visibility = View.GONE
            }
            if (chat.isChatType) {
                binding.agentBanner.visibility = View.VISIBLE
                binding.receiverMsg.text = chat.message
                binding.time.text = chat.time
            } else {
                binding.agentBanner.visibility = View.GONE
            }
        }
    }

    inner class AgentChatViewHolder(val binding: RowChatAgentMessageBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {

            binding.receiverMsg.text = chat.message
            binding.time.text = chat.time
        }
    }

    inner class AgentImageViewHolder(val binding: RowChatAgentImageBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {

            binding.progressBar.visibility = View.VISIBLE
            Glide.with(context).load(chat.message).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }
                }).into(binding.img)
            binding.time.text = chat.time
            binding.llImagelayout.setOnClickListener {
                chat.message?.let { it1 -> showImageAlert(it1) }
            }
        }
    }

    inner class AgentVideoViewHolder(val binding: RowChatAgentVideoBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {
            binding.time.text = chat.time

            binding.llVideolayout.setOnClickListener {
//                val intent = Intent(context, VideoVIewActivity::class.java)
//                intent.putExtra("video", chat.message)
//                context.startActivity(intent)
            }
        }
    }

    inner class AgentAudioViewHolder(val binding: RowChatAgentAudioBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {


            binding.time.text = chat.time
            binding.btnPlayLeft.setOnClickListener {
                try {
                    chat.message?.let { it1 -> showSoundDialog(context, it1) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    inner class AgentPdfViewHolder(val binding: RowChatAgentPdfBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {


            binding.time.text = chat.time

            binding.llPdflayout.setOnClickListener {
                try {
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(chat.message), "application/pdf")
                    intent = Intent.createChooser(intent, "PDF File")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(chat.message))
                    context.startActivity(browserIntent)
                }

            }
        }
    }

    inner class AgentChatContactViewHolder(val binding: RowChatAgentContactBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {
            val contactArray: List<String>? =
                if (!chat.message.isNullOrEmpty()) chat.message?.split(",") else listOf<String>(
                    chat.message ?: ""
                )
            binding.senderMsg.text = contactArray?.get(0) ?: ""
            binding.phoneNumber.text = contactArray?.get(1) ?: ""
            binding.callId.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${contactArray?.get(1) ?: ""}")
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            binding.time.text = chat.time
        }
    }

    inner class UserChatViewHolder(val binding: RowChatUserMessageBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {

            binding.senderMsg.text = chat.message
            binding.time.text = chat.time
            setSentMessageIcon(binding.mediaStatus, chat, true)
        }
    }

    inner class UserChatContactViewHolder(val binding: RowChatUserContactBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {
            val contactArray: List<String>? =
                if (!chat.message.isNullOrEmpty()) chat.message?.split(",") else listOf<String>(
                    chat.message ?: ""
                )
            binding.senderMsg.text = contactArray?.get(0) ?: ""
            binding.phoneNumber.text = contactArray?.get(1) ?: ""
            binding.callId.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${contactArray?.get(1) ?: ""}")
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            binding.time.text = chat.time
            setSentMessageIcon(binding.mediaStatus, chat, true)
        }
    }


    inner class UserImageViewHolder(val binding: RowChatUserImageBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {

            binding.progressBar.visibility = View.VISIBLE
            Glide.with(context).load(chat.message).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }
                }).into(binding.imgRight)

            binding.time.text = chat.time
            WorkManager.getInstance(binding.root.context.applicationContext)
                .getWorkInfosForUniqueWorkLiveData(chat.mobileTimeStamp.toString())
                .observe(lifecycleOwner,
                    { workInfos: List<WorkInfo> ->
                        if (workInfos.isNotEmpty()) {
                            Log.e(
                                "Chat Activity--",
                                "chat.workInfos " + workInfos[0].state + " pos " + pos
                            )
                            binding.mediaMiddleContainer.visibility = View.VISIBLE
                            if (workInfos[0].state == WorkInfo.State.ENQUEUED) {
                                binding.mediaMiddleContainer.visibility = View.VISIBLE
                                binding.mediaProgress.visibility = View.VISIBLE
                                binding.mediaProgress.progress = 0
                            } else if (workInfos[0].state == WorkInfo.State.RUNNING) {
                                val progress =
                                    workInfos[0].progress.getDouble(MediaMessageWork.PROGRESS, 0.0)
                                binding.mediaProgress.progress = progress.toInt()
                                binding.mediaRetry.visibility = View.GONE
                                binding.mediaProgress.isIndeterminate = progress <= 4
                                if (progress in 4.0..99.9) {
                                    chat.progress = progress.toInt()
                                    binding.mediaMiddleContainer.visibility = View.VISIBLE
                                    binding.mediaProgress.visibility = View.VISIBLE
                                    binding.mediaProgress.progress = progress.toInt()
                                    binding.mediaProgress.isIndeterminate = false
                                    binding.mediaProgress.progressDrawable.mutate();
                                } else {
//                                    binding.mediaMiddleContainer.visibility = View.GONE
                                    binding.mediaProgress.isIndeterminate = true
                                    binding.mediaProgress.incrementProgressBy(30)
                                }
                            } else if (workInfos[0].state == WorkInfo.State.CANCELLED ||
                                workInfos[0].state == WorkInfo.State.FAILED
                            ) {

                            } else if (workInfos[0].state == WorkInfo.State.SUCCEEDED
                            ) {
                                binding.mediaMiddleContainer.visibility = View.GONE
                                Handler(Looper.getMainLooper()).postDelayed({
                                    onUploadSuccessed.invoke(true, chat, false)
                                }, 600)
                            }
                        } else {
//                            binding.mediaMiddleContainer.visibility = View.GONE
                            binding.mediaProgress.isIndeterminate = true
                            binding.mediaProgress.incrementProgressBy(30)
                        }
                    })

            binding.llImagelayoutRight.setOnClickListener {
                chat.message?.let { it1 -> showImageAlert(it1) }
            }
            setSentMessageIcon(binding.mediaStatus, chat, true)
        }
    }

    inner class UserLocationViewHolder(val binding: RowChatUserLocationBinding) :
        BaseViewHolder(binding.root) {
        lateinit var map: GoogleMap
        var latLng: LatLng = LatLng(0.0, 0.0)

        init {
            with(binding.mapGchatMe) {
                // Initialise the MapView
                onCreate(null)
                // Set the map ready callback to receive the GoogleMap object
                getMapAsync {
                    MapsInitializer.initialize(context.applicationContext)
                    map = it ?: return@getMapAsync
                    setLocation()
                }
            }
        }

        private fun setLocation() {
            if (!::map.isInitialized) return
            with(map) {
                moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
                addMarker(MarkerOptions().position(latLng))
                mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            binding.mapGchatMe.onResume()
        }

        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {

            binding.time.text = chat.time
            binding.llImagelayoutRight.setOnClickListener {
                chat.message?.let { it1 -> showImageAlert(it1) }
            }
            chat.message?.let {
                try {
                    val latlngArray = it.split(",")
                    latLng = LatLng(latlngArray[0].toDouble(), latlngArray[1].toDouble())
                    setLocation()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            setSentMessageIcon(binding.mediaStatus, chat, true)
        }
    }

    inner class UserVideoViewHolder(val binding: RowChatUserVideoBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {
            binding.time.text = chat.time
            Log.e("chat Activity", "chat.progress " + chat.progress + " pos " + pos)
            WorkManager.getInstance(binding.root.context.applicationContext)
                .getWorkInfosForUniqueWorkLiveData(chat.mobileTimeStamp.toString())
                .observe(lifecycleOwner,
                    { workInfos: List<WorkInfo> ->
                        if (workInfos.isNotEmpty()) {
                            Log.e(
                                "chat Activity",
                                "chat.workInfos " + workInfos[0].state + " pos " + pos
                            )
                            binding.mediaMiddleContainer.visibility = View.VISIBLE
                            if (workInfos[0].state == WorkInfo.State.ENQUEUED) {
                                binding.mediaMiddleContainer.visibility = View.VISIBLE
                                binding.mediaProgress.visibility = View.VISIBLE
                                binding.mediaProgress.progress = 0
                            } else if (workInfos[0].state == WorkInfo.State.RUNNING) {
                                val progress =
                                    workInfos[0].progress.getDouble(MediaMessageWork.PROGRESS, 0.0)
                                binding.mediaProgress.progress = progress.toInt()
                                binding.mediaRetry.visibility = View.GONE
//                                binding.mediaProgress.isIndeterminate = progress <= 4
                                Log.e("chat Activity", "chat.progress " + progress + " pos " + pos)
                                if (progress in 4.0..99.9) {
                                    binding.mediaMiddleContainer.visibility = View.VISIBLE
                                    binding.mediaProgress.isIndeterminate = false
                                    binding.mediaProgress.visibility = View.VISIBLE
                                    binding.mediaProgress.progress = progress.toInt()
                                    binding.mediaProgress.progressDrawable.mutate();
                                } else {
//                                    binding.mediaMiddleContainer.visibility = View.GONE
                                    binding.mediaProgress.isIndeterminate = true
                                    binding.mediaProgress.incrementProgressBy(30)
                                }
                            } else if (workInfos[0].state == WorkInfo.State.CANCELLED ||
                                workInfos[0].state == WorkInfo.State.FAILED
                            ) {
                                binding.mediaRetry.visibility = View.GONE
                                binding.mediaProgress.visibility = View.GONE
                                /*binding.mediaRetry.setOnClickListener {
                                    onUploadSuccessed?.invoke(false, chat, true)
                                }*/

                            } else if (workInfos[0].state == WorkInfo.State.SUCCEEDED
                            ) {
                                binding.mediaMiddleContainer.visibility = View.GONE
                                Handler(Looper.getMainLooper()).postDelayed({
                                    onUploadSuccessed.invoke(true, chat, false)
                                }, 600)
                            }
                        } else {
                            binding.mediaProgress.isIndeterminate = true
                            binding.mediaProgress.incrementProgressBy(30)
                        }
                    })

            binding.llVideolayoutRight.setOnClickListener {
                /*if (chat.message.equals(
                        "uploading",
                        ignoreCase = true
                    )
                ) AppConfig.showToast("Please wait video is uploading!") else {
                    val intent = Intent(context, VideoVIewActivity::class.java)
                    intent.putExtra("video", chat.message)
                    context.startActivity(intent)
                }*/
            }
            setSentMessageIcon(binding.mediaStatus, chat, true)

        }
    }

    inner class UserAudioViewHolder(val binding: RowChatUserAudioBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {

            binding.time.text = chat.time
            binding.btnPlay.setOnClickListener {
                try {
                    chat.message?.let { it1 -> showSoundDialog(context, it1) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            WorkManager.getInstance(binding.root.context.applicationContext)
                .getWorkInfosForUniqueWorkLiveData(chat.mobileTimeStamp.toString())
                .observe(lifecycleOwner,
                    { workInfos: List<WorkInfo> ->
                        if (workInfos.isNotEmpty()) {
                            Log.e(
                                "chat Activity",
                                "chat.workInfos " + workInfos[0].state + " pos " + pos
                            )
                            binding.mediaMiddleContainer.visibility = View.VISIBLE
                            if (workInfos[0].state == WorkInfo.State.ENQUEUED) {
                                binding.mediaMiddleContainer.visibility = View.VISIBLE
                                binding.mediaProgress.visibility = View.VISIBLE
                                binding.mediaProgress.progress = 0
                            } else if (workInfos[0].state == WorkInfo.State.RUNNING) {
                                val progress =
                                    workInfos[0].progress.getDouble(MediaMessageWork.PROGRESS, 0.0)
                                binding.mediaProgress.progress = progress.toInt()
                                binding.mediaRetry.visibility = View.GONE
                                binding.btnPlay.visibility = View.GONE
                                binding.mediaProgress.isIndeterminate = progress <= 1
                                if (progress in 4.0..99.9) {
                                    chat.progress = progress.toInt()
                                    binding.mediaMiddleContainer.visibility = View.VISIBLE
                                    binding.mediaProgress.visibility = View.VISIBLE
                                    binding.mediaProgress.progress = progress.toInt()
                                    binding.mediaProgress.isIndeterminate = false
                                    binding.mediaProgress.progressDrawable.mutate();
                                } else {
//                                    binding.mediaMiddleContainer.visibility = View.GONE
//                                    binding.btnPlay.visibility = View.VISIBLE
                                    binding.mediaProgress.isIndeterminate = true
                                    binding.mediaProgress.incrementProgressBy(30)
                                }
                            } else if (workInfos[0].state == WorkInfo.State.CANCELLED ||
                                workInfos[0].state == WorkInfo.State.FAILED
                            ) {
                                binding.mediaRetry.visibility = View.GONE
                                binding.btnPlay.visibility = View.GONE
//                                Handler(Looper.getMainLooper()).postDelayed({
//                                    onUploadSuccessed.invoke(false, chat, true)
//                                }, 600)
                            } else if (workInfos[0].state == WorkInfo.State.SUCCEEDED
                            ) {
                                binding.btnPlay.visibility = View.VISIBLE
                                binding.mediaMiddleContainer.visibility = View.GONE
                                Handler(Looper.getMainLooper()).postDelayed({
                                    onUploadSuccessed.invoke(true, chat, false)
                                }, 600)
                            }

                        } else {
                            binding.btnPlay.visibility = View.VISIBLE
                            binding.mediaMiddleContainer.visibility = View.GONE
                        }
                    })
            setSentMessageIcon(binding.mediaStatus, chat, true)

        }
    }

    inner class UserPdfViewHolder(val binding: RowChatUserPdfBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(pos: Int, chat: ChatMessagesListModel, lifecycleOwner: LifecycleOwner) {

            binding.time.text = chat.time

            binding.llPdflayoutRight.setOnClickListener {
                try {
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(chat.message), "application/pdf")
                    intent = Intent.createChooser(intent, "PDF File")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(chat.message))
                    context.startActivity(browserIntent)
                }

            }
            WorkManager.getInstance(binding.root.context.applicationContext)
                .getWorkInfosForUniqueWorkLiveData(chat.mobileTimeStamp.toString())
                .observe(lifecycleOwner,
                    { workInfos: List<WorkInfo> ->
                        if (workInfos.isNotEmpty()) {
                            Log.e(
                                "chat Activity",
                                "chat.workInfos " + workInfos[0].state + " pos " + pos
                            )
                            binding.mediaMiddleContainer.visibility = View.VISIBLE
                            if (workInfos[0].state == WorkInfo.State.ENQUEUED) {
                                binding.mediaMiddleContainer.visibility = View.VISIBLE
                                binding.mediaProgress.visibility = View.VISIBLE
                                binding.mediaProgress.progress = 0
                            } else if (workInfos[0].state == WorkInfo.State.RUNNING) {
                                val progress =
                                    workInfos[0].progress.getDouble(MediaMessageWork.PROGRESS, 0.0)
                                binding.mediaProgress.progress = progress.toInt()
                                binding.mediaRetry.visibility = View.GONE
                                binding.mediaProgress.isIndeterminate = progress <= 4
                                if (progress in 4.0..99.9) {
                                    chat.progress = progress.toInt()
                                    binding.mediaMiddleContainer.visibility = View.VISIBLE
                                    binding.mediaProgress.visibility = View.VISIBLE
                                    binding.mediaProgress.isIndeterminate = false
                                    binding.mediaProgress.progress = progress.toInt()
                                    binding.mediaProgress.progressDrawable.mutate();
                                } else {
//                                    binding.mediaMiddleContainer.visibility = View.GONE
                                    binding.mediaProgress.isIndeterminate = true
                                    binding.mediaProgress.incrementProgressBy(30)
                                }
                            } else if (workInfos[0].state == WorkInfo.State.CANCELLED ||
                                workInfos[0].state == WorkInfo.State.FAILED
                            ) {

                            } else if (workInfos[0].state == WorkInfo.State.SUCCEEDED
                            ) {
                                binding.mediaMiddleContainer.visibility = View.GONE
                                Handler(Looper.getMainLooper()).postDelayed({
                                    onUploadSuccessed.invoke(true, chat, false)
                                }, 600)
                            }
                        } else {
                            binding.mediaMiddleContainer.visibility = View.GONE
                        }
                    })
            setSentMessageIcon(binding.mediaStatus, chat, true)

        }
    }

    private fun showImageAlert(service_Img: String) {
        val alertadd = Dialog(context, R.style.full_screen_dialog)
        alertadd.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        alertadd.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertadd.setContentView(R.layout.view_image)
        alertadd.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        alertadd.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        val imageView = alertadd.findViewById<ImageView>(R.id.dialog_imageview)
        val btnBack = alertadd.findViewById<ImageView>(R.id.close)
        val llProgressBar = alertadd.findViewById<LinearLayoutCompat>(R.id.llProgressBar)
        btnBack.setOnClickListener { v: View? -> alertadd.dismiss() }
        llProgressBar.visibility = View.VISIBLE
        Glide.with(context).load(service_Img)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    llProgressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    llProgressBar.visibility = View.GONE
                    return false
                }
            }).into(imageView)
        alertadd.show()
    }

    private fun showSoundDialog(mContext: Context, uri: String) {
        try {

            /*   final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_sound, null);
            builder.setView(view);
            final AlertDialog alertDialog = builder.create();*/
            if (dialogShown) {
                return
            } else {
                dialogShown = true
                val dialog = Dialog(mContext)
                dialog.setContentView(R.layout.dialog_sound)
                if (dialog.window != null) {
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
                dialog.setCanceledOnTouchOutside(false)
                dialog.setCancelable(false)
                val btn_close = dialog.findViewById<ImageView>(R.id.btn_close)
                val btnPlay = dialog.findViewById<ImageView>(R.id.btn_play)
                val btnStop = dialog.findViewById<ImageView>(R.id.btn_stop)
                val seekBar = dialog.findViewById<SeekBar>(R.id.seekbar)
                val initialTime = dialog.findViewById<TextView>(R.id.time)
                val totalTime = dialog.findViewById<TextView>(R.id.total_time)
                var mMediaPlayer: MediaPlayer? = null
                try {
//                    mMediaPlayer = MediaPlayer.create(mContext, Uri.parse(uri))
                    mMediaPlayer = MediaPlayer()
                    mMediaPlayer.setDataSource(mContext, Uri.parse(uri))
                    val attr = AudioAttributes.Builder()
                    attr.setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    mMediaPlayer.setAudioAttributes(attr.build())
                    mMediaPlayer.prepareAsync()
                    mMediaPlayer.setOnPreparedListener {
//                        it.start()
                        mMediaPlayer.start()
                        updateSeekBar(seekBar, mMediaPlayer, totalTime)
                    }
                    mMediaPlayer.setOnErrorListener { mediaPlayer, i, i2 ->
                        try {
                            Log.e("chat Activity", "i, i2" + i + i2)
                            handler?.removeCallbacksAndMessages(null)
                            mMediaPlayer?.stop()
                            resetSeekBar(seekBar)
                            btnPlay.visibility = View.VISIBLE
                            btnStop.visibility = View.GONE
                            mMediaPlayer?.release()
                            dialog.dismiss()
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                            dialog.dismiss()
                        }
                        return@setOnErrorListener true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                /*   int total = mMediaPlayer.getDuration();

            String time = String.format("%02d , %02d ",
                    TimeUnit.MILLISECONDS.toMinutes(total),
                    TimeUnit.MILLISECONDS.toSeconds(total) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total))
            );
            totalTime.setText("/ " + time);*/
                btnPlay.visibility = View.GONE
                btnStop.visibility = View.VISIBLE
                seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        mMediaPlayer?.seekTo(seekBar.progress)
                    }
                })

                // initialTime.setText(s);
                btn_close.setOnClickListener { view: View? ->
                    try {
                        handler?.removeCallbacksAndMessages(null)
                        mMediaPlayer?.stop()
                        resetSeekBar(seekBar)
                        btnPlay.visibility = View.VISIBLE
                        btnStop.visibility = View.GONE
                        mMediaPlayer?.release()
                        dialog.dismiss()
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                        dialog.dismiss()
                    }
                }
                dialog.setOnCancelListener { dialog1: DialogInterface? ->
                    dialogShown = false
                    Log.e("chat Activity", "setOnCancelListener $dialogShown")
                }
                dialog.setOnDismissListener { dialog1: DialogInterface? ->
                    Log.e("chat Activity", "setOnDismissListener $dialogShown")
                    dialogShown = false
                }
                btnPlay.setOnClickListener { view: View? ->
                    try {
//                        MediaPlayer mMediaPlayer = MediaPlayer.create(mContext, Uri.parse(uri));
//                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer?.start()
                        btnPlay.visibility = View.GONE
                        btnStop.visibility = View.VISIBLE
                        if (mMediaPlayer != null) {
                            updateSeekBar(seekBar, mMediaPlayer, totalTime)
                        }
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
                btnStop.setOnClickListener { view: View? ->
                    try {
                        mMediaPlayer?.pause()
                        btnPlay.visibility = View.VISIBLE
                        btnStop.visibility = View.GONE
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
                Log.d("TAG", "aa gya>>>>>>")
                mMediaPlayer?.setOnCompletionListener { mediaPlayer: MediaPlayer ->
                    mediaPlayer.pause()
                    resetSeekBar(seekBar)
                    mediaPlayer.seekTo(0)
                    btnPlay.visibility = View.VISIBLE
                    btnStop.visibility = View.GONE
                }
                dialog.show()
                val width = GlobalUtils.getWidthScreenResolution(mContext)
                val h = GlobalUtils.getHeightScreenResolution(mContext)
                val width1 = width * 4 / 5
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window!!.attributes)
                lp.width = width1
                // lp.height = h / 3;
                // lp.height = h;
                lp.x = 0
                lp.y = 0
                dialog.window!!.attributes = lp
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun updateSeekBar(seek: SeekBar, mMediaPlayer: MediaPlayer, tvTime: TextView) {
        var time = ""
        try {
            val pos = mMediaPlayer.currentPosition
            val totalTime = mMediaPlayer.duration
            time = String.format(
                "%02d , %02d ",
                TimeUnit.MILLISECONDS.toMinutes(pos.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(pos.toLong()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(pos.toLong()))
            )

            // tvTime.setText(time);
            /* if(pos==totalTime){

            }*/
//            Log.e("chat Activity","totalTime:  " + time + "  pos " + pos);
            seek.progress = pos
            seek.max = mMediaPlayer.duration
        } catch (ignored: Exception) {
        }
        val runnable = Runnable { updateSeekBar(seek, mMediaPlayer, tvTime) }
        handler = Handler()
        handler?.postDelayed(runnable, 1800)
    }

    private fun resetSeekBar(seek: SeekBar) {
        seek.progress = 0
    }

    fun setSentMessageIcon(
        imageView: ImageView,
        chat: ChatMessagesListModel,
        pending: Boolean
    ) {
        if (pending) {
            imageView.background = imageView.context.getDrawable(R.drawable.ic_schedule_grey_24dp)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imageView.foreground =
                    imageView.context.getDrawable(R.drawable.ic_schedule_grey_24dp)
            }
        }
        if (chat.members?.isNullOrEmpty() == false) {
            val user_id = SharedPrefsHelper.getInstance(context).getUserData()._id
            imageView.background = imageView.context.getDrawable(R.drawable.ic_sent_grey_24dp)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imageView.foreground = imageView.context.getDrawable(R.drawable.ic_sent_grey_24dp)
            }
            if (chat.members?.size == 1) {
                chat.members?.map {
                    if (it.userId == user_id && chat.unassignedRead == true) {
                        imageView.background =
                            imageView.context.getDrawable(R.drawable.ic_read_blue_24dp)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            imageView.foreground =
                                imageView.context.getDrawable(R.drawable.ic_read_blue_24dp)
                        }
                    }
                }
            } else {
                chat.members?.map {
                    if (it.userId != user_id) {
                        if (it.delivered || it.read) {
                            if (it.read) {
                                imageView.background =
                                    imageView.context.getDrawable(R.drawable.ic_read_blue_24dp)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    imageView.foreground =
                                        imageView.context.getDrawable(R.drawable.ic_read_blue_24dp)
                                }
                            } else {
                                imageView.background =
                                    imageView.context.getDrawable(R.drawable.ic_delivered_grey_24dp)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    imageView.foreground =
                                        imageView.context.getDrawable(R.drawable.ic_delivered_grey_24dp)
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    enum class CardType(i: Int) {
        AGENT_TEXT_MESSAGE(1),
        AGENT_IMAGE(2),
        AGENT_VIDEO(3),
        AGENT_AUDIO(4),
        AGENT_PDF(5),
        AGENT_CONTACT(6),
        USER_TEXT_MESSAGE(7),
        USER_IMAGE(8),
        USER_VIDEO(9),
        USER_AUDIO(10),
        USER_PDF(11),
        USER_LOCATION(12),
        USER_CONTACT(13),
        CHAT_DATE(14),
        HEADER(15),
    }

}