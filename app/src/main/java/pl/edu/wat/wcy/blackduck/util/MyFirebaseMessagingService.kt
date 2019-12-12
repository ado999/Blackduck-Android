package pl.edu.wat.wcy.blackduck.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.GsonBuilder
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.responses.MessageResponse
import pl.edu.wat.wcy.blackduck.ui.conversation.ConversationActivity
import java.util.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import javax.inject.Inject


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val ADMIN_CHANNEL_ID = "admin_channel"

    private var broadcaster: LocalBroadcastManager? = null

    @Inject
    lateinit var prefsManager: PrefsManager


    override fun onCreate() {
        super.onCreate()
        BlackduckApplication.appComponent.inject(this)
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.e("TO", p0.to?:"jjjj")
        Log.e("TO", prefsManager.loadUser().uuid)
        val gb = GsonBuilder()
        gb.setDateFormat("dd.MM.yyyy, HH:mm:ss")
        val messageResponse = gb.create().fromJson(p0.data["chatMessageResponse"], MessageResponse::class.java)
        if(messageResponse.toUser.username != prefsManager.loadUser().displayName)
            return

        val messageIntent = Intent("MessageResponse")
        messageIntent.putExtra("message", p0.data["chatMessageResponse"])
        broadcaster?.sendBroadcast(messageIntent)

        val title = "${messageResponse.fromUser.username} napisał(a) wiadomość"
        val message = if(messageResponse.message.length < 50){
            messageResponse.message
        } else {
            "${messageResponse.message.substring(0, 47)}..."
        }

        val bundle =  Bundle()
        bundle.putString(Key.MESSAGE_USERNAME.name, messageResponse.fromUser.username)
        bundle.putString(Key.MESSAGE_PROFILE_URL.name, messageResponse.fromUser.profilePhotoUrl)
        bundle.putString(Key.MESSAGE_DATE.name, DateUtils.getDateDiff(messageResponse.fromUser.lastActivity))
        val intent = Intent(this, ConversationActivity::class.java)
        intent.putExtras(bundle)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)

        /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them.
      */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val largeIcon = BitmapFactory.decodeResource(
            resources,
            R.drawable.swanicon
        )

        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.swanicon)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(notificationSoundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannelName = "New notification"
        val adminChannelDescription = "Device to devie notification"

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH)
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }

}
