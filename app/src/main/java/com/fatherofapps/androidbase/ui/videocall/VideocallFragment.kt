package com.fatherofapps.androidbase.ui.videocall

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentCallvideoBinding
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.MalformedURLException
import java.net.URL

class VideocallFragment : BaseFragment() {
    private lateinit var dataBinding: FragmentCallvideoBinding

    private lateinit var codeBox: EditText
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_callvideo, container, false)

        codeBox = view.findViewById(R.id.edtCode)
        button = view.findViewById(R.id.btnSendCode)

        try {
            val serverUrl = URL("https://meet.jit.si")
            val defaultOption = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverUrl)
                .build()
            JitsiMeet.setDefaultConferenceOptions(defaultOption)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        button.setOnClickListener {
            val text = codeBox.text.toString()
            if (text.isNotEmpty()) {
                val options = JitsiMeetConferenceOptions.Builder()
                    .setRoom(text)
                    .setFeatureFlag("invite.enabled", false)
                    .build()
                JitsiMeetActivity.launch(requireActivity(), options)
            }
        }

        return view
    }
}