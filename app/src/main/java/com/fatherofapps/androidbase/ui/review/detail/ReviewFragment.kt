package com.fatherofapps.androidbase.ui.review.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.request.createReview
import com.fatherofapps.androidbase.data.response.DoctorAppointment
import com.fatherofapps.androidbase.databinding.FragmentReviewBinding
import com.fatherofapps.androidbase.ui.review.detail.ReviewFragmentArgs
import com.fatherofapps.androidbase.ui.review.detail.ReviewFragmentDirections
import com.fatherofapps.androidbase.utils.convertImagePath
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ReviewFragment @Inject constructor() : BaseFragment() {
    private lateinit var dataBinding: FragmentReviewBinding
    private val viewModel by viewModels<ReviewViewModel>()
    private val args: ReviewFragmentArgs by navArgs()
    private var review: createReview? = null
    private var doctor: DoctorAppointment? = null
    private val TAG = "ReviewFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDoctorAppointment(args.appointmentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentReviewBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        (activity as MainActivity).setTitle("Đánh giá bác sĩ")
        (activity as MainActivity).setNavigationBackIcon()
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenTopAppBar(true)
        hideOpenNavigation(false)
        registerAllExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverLoadingEvent(viewModel, viewLifecycleOwner)
        setupObservers()
        setupReviewButton()
        bindObservers()
    }

    private fun setupObservers() {
        viewModel.doctorAppointmentResponse.observe(viewLifecycleOwner) { response ->
            response?.let { doctorAppointmentResponse ->
                if (doctorAppointmentResponse.isSuccess()) {
                    doctor = doctorAppointmentResponse.data
                    Glide.with(requireContext())
                        .load(convertImagePath(doctor?.doctorImage!!))
                        .into(dataBinding.imgDoctor)
                    dataBinding.txtIntro.text = "Bạn có trải nghiệm như thế nào\nvới Bác sĩ ${doctor?.doctorName}"
                } else {
                    showErrorMessage(doctorAppointmentResponse.message ?: "Lỗi không xác định")
                }
            }
        }
    }

    private fun setupReviewButton() {
        dataBinding.submitReviewButton.setOnClickListener {
            if (validateReview()) {
                viewModel.createReview(review!!)
            }
        }
    }

    private fun validateReview(): Boolean {
        val rating = dataBinding.ratingBar.rating.toInt()
        val comment = dataBinding.commentEditText.text.toString()
        if (rating == 0) {
            showErrorMessage("Vui lòng chọn số sao")
            return false
        }
        if (comment.isEmpty()) {
            showErrorMessage("Vui lòng nhập bình luận")
            return false
        }
        review = createReview(
            doctorId = doctor?.doctorId ?: throw IllegalStateException("Bác sĩ không được null"),
            rating = rating,
            comment = comment,
            appointmentId = args.appointmentId
        )
        return true
    }

    private fun bindObservers() {
        viewModel.createReviewResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccess()) {
                showDialogMaterial(
                    "Đánh giá thành công",
                    "Cảm ơn bạn đã đánh giá bác sĩ ${doctor?.doctorName}"
                )
                val action = ReviewFragmentDirections.actionReviewFragmentToHomeFragment()
                navigateToPage(action)
            } else {
                showErrorMessage(response.message ?: "Lỗi không xác định")
            }
        }
    }

}