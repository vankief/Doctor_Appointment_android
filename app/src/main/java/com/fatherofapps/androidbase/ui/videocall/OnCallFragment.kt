package com.fatherofapps.androidbase.ui.videocall

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.facebook.react.modules.core.PermissionListener
import com.fatherofapps.androidbase.R
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetView

class OnCallFragment: FragmentActivity(), JitsiMeetActivityInterface {

    private var jitsiMeetView: JitsiMeetView? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        JitsiMeetActivityDelegate.onActivityResult(this, requestCode, resultCode, data)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        JitsiMeetActivityDelegate.onBackPressed()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_oncall)
        val videocall = findViewById<FrameLayout>(R.id.fragment_oncall)
        val callId = intent.getStringExtra("callId")
        jitsiMeetView = JitsiMeetView(this)
        val options = JitsiMeetConferenceOptions.Builder()
            .setRoom("https://meet.jit.si/$callId")
            .build()

        jitsiMeetView!!.join(options)
        videocall.addView(jitsiMeetView)
    }

    override fun onDestroy() {
        super.onDestroy()
        jitsiMeetView!!.dispose()
        jitsiMeetView = null
        JitsiMeetActivityDelegate.onHostDestroy(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        JitsiMeetActivityDelegate.onNewIntent(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {}

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(this)
    }

    override fun onStop() {
        super.onStop()
        JitsiMeetActivityDelegate.onHostPause(this)
    }
}