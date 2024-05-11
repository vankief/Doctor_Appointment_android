package com.fatherofapps.androidbase.ui.videocall


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentCallvideoBinding
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.BroadcastIntentHelper
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL


class VideocallFragment : BaseFragment() {
    private lateinit var dataBinding: FragmentCallvideoBinding

    private lateinit var codeBox: EditText
    private lateinit var button: Button
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_callvideo, container, false)
        codeBox = view.findViewById(R.id.edtCode)
        button = view.findViewById(R.id.btnSendCode)

        val intentFilter = IntentFilter()
        intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_JOINED.action)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, intentFilter)
        val roomId = "vpaas-magic-cookie-6e5379b6cb9d497689528c0df4c7bc3a/c2e4f860-4d9c-4e48-979a-6e157b2a14c9"
        try {
            val serverUrl = "https://8x8.vc" // Sử dụng URL tương tự như trên FE

            val url = URL(serverUrl)
            val defaultOption = JitsiMeetConferenceOptions.Builder()
                .setServerURL(url)
                .build()
            JitsiMeet.setDefaultConferenceOptions(defaultOption)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }



        button.setOnClickListener {
            val text = codeBox.text.toString()
            if (text.isNotEmpty()) {
                val options = JitsiMeetConferenceOptions.Builder()
                    .setRoom(roomId)
                    .setFeatureFlag("invite.enabled", false)
                    .build()
                JitsiMeetActivity.launch(requireActivity(), options)
            }
        }
        Log.d("VideocallFragment", "onCreateView: ")

        return view
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.action);
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action);
                ... other events
         */
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, intentFilter)
    }

    // Example for handling different JitsiMeetSDK events
    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.type) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> Timber.i("Conference Joined with url%s", event.getData().get("url"))
                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i("Participant joined%s", event.getData().get("name"))
                else -> Timber.i("Received event: %s", event.type)
            }
        }
    }

    // Example for sending actions to JitsiMeetSDK
    private fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(hangupBroadcastIntent)
    }
}