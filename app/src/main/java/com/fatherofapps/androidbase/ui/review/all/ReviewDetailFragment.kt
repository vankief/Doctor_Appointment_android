package com.fatherofapps.androidbase.ui.review.all

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.data.response.ReViewResponse
import com.fatherofapps.androidbase.databinding.FragmentReviewDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReviewDetailFragment @Inject constructor(): BaseFragment() {
    private lateinit var dataBinding: FragmentReviewDetailBinding
    private val viewModel by viewModels<ReviewDetailViewModel>()
    private var starAdapter: StarAdapter? = null
    private var reviewAdapter: ReviewAdapter? = null
    private var reviewList: List<ReViewResponse> = emptyList()
    private val TAG = "ReviewDetailFragment"
    private val args: ReviewDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getReviewDoctor(args.doctorId, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentReviewDetailBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        (activity as MainActivity).setTitle("Đánh giá bác sĩ")
        (activity as MainActivity).setNavigationBackIcon()
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideOpenTopAppBar(true)
        hideOpenNavigation(false)
        setupObservers()
        setupStarRecyclerView()
    }

    private fun setupObservers() {
        viewModel.reviewDoctor.observe(viewLifecycleOwner) { response ->
            if (response!= null && response.isSuccess()) {
                Log.d(TAG, "TopDoctor: ${response.data}}")
                response.data?.let { data ->
                    reviewList = data
                    setupReviewRecyclerView()
                }
            }
            else {
                if (response == null) showErrorMessage("Lỗi mạng")
                else showErrorMessage(response.checkTypeErr())
            }
        }
    }

    private fun setupStarRecyclerView() {
        starAdapter = StarAdapter()
        dataBinding.rvReviewRating.apply {
            adapter = starAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        starAdapter?.onItemClickListener = { star ->
            viewModel.getReviewDoctor(args.doctorId, star)
        }
    }
    private fun setupReviewRecyclerView() {
        reviewAdapter = ReviewAdapter(reviewList)
        dataBinding.rvReviewDetail.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        dataBinding.rvReviewDetail.adapter = reviewAdapter
        reviewAdapter?.notifyDataSetChanged()
    }
}