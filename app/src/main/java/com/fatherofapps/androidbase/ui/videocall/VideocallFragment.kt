package com.fatherofapps.androidbase.ui.videocall

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import com.facebook.react.modules.core.PermissionListener
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentCallvideoBinding
import dagger.hilt.android.AndroidEntryPoint
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.JitsiMeetView
import javax.inject.Inject
class VideocallFragment : BaseFragment() {
    private lateinit var dataBinding: FragmentCallvideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentCallvideoBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.btnSendCode.setOnClickListener {
            if (dataBinding.edtCode.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter call id", Toast.LENGTH_SHORT).show()
            } else {
                val callId = dataBinding.edtCode.text.toString()
                val intent = Intent(requireContext(), OnCallFragment::class.java)
                intent.putExtra("callId", callId)
                startActivity(intent)
            }
        }
    }
}