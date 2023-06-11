package com.example.solanamobiledappscaffold.presentation.ui.question

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.GuardedBy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solanamobiledappscaffold.R
import com.example.solanamobiledappscaffold.common.Constants.formatAddress
import com.example.solanamobiledappscaffold.common.Constants.question_list
import com.example.solanamobiledappscaffold.databinding.FragmentQuestionBinding
import com.example.solanamobiledappscaffold.presentation.utils.StartActivityForResultSender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private lateinit var adapter: QuestionAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: QuestionViewModel by viewModels()

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            intentSender.onActivityComplete()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)

        val animDrawable = binding.root.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(10)
        animDrawable.setExitFadeDuration(1000)
        animDrawable.start()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.walletBtn.text = viewModel.getWalletButtonText(requireContext())

        binding.walletBtn.setOnClickListener {
            // TODO: open modal showing two things, copy and disconnect
            viewModel.interactWallet(intentSender)
        }

        observeViewModel()

        binding.recyclerViewQuestions.layoutManager = LinearLayoutManager(context)
        adapter = QuestionAdapter(question_list)
        binding.recyclerViewQuestions.adapter = adapter

        adapter.setOnItemClickListener(object : QuestionAdapter.OnItemClickListener {
            override fun onItemClick(question: Question) {
                val args = Bundle()
                args.putSerializable("question", question)
                binding.root.findNavController().navigate(R.id.action_navigation_question_to_navigation_reply, args)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.getBalance()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            with(viewModel) {
                uiState.collect { uiState ->
                    uiState.wallet?.let {
                        connectWallet(it.publicKey58)
                    } ?: run {
                        clearWallet()
                        disconnectWallet()
                    }

                    uiState.balance.let {
                        binding.balanceTv.text = String.format(
                            resources.getString(R.string.wallet_balance),
                            it,
                        )
                    }

                    // TODO: show snackbar, extension
//                    uiState.error.let {
//                        requireView().showSnackbar(
//                            it,
//                        )
//                    }
                }
            }
        }
    }

    private fun connectWallet(publicKey: String) {

        binding.walletBtn.text = formatAddress(publicKey)
        binding.walletBtn.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.black),
        )

        binding.walletBtn.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.solana_green),
        )

        binding.walletBtn.iconTint =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.teal))

    }

    private fun disconnectWallet() {

        binding.walletBtn.text = getString(R.string.select_wallet)
        binding.walletBtn.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.white),
        )
        binding.walletBtn.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.black),
        )

        binding.walletBtn.iconTint =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red))
    }

    private val intentSender = object : StartActivityForResultSender {
        @GuardedBy("this")
        private var callback: (() -> Unit)? = null

        override fun startActivityForResult(
            intent: Intent,
            onActivityCompleteCallback: () -> Unit,
        ) {
            synchronized(this) {
                check(callback == null) {
                    "Received an activity start request while another is pending"
                }
                callback = onActivityCompleteCallback
            }
            activityResultLauncher.launch(intent)
        }

        fun onActivityComplete() {
            synchronized(this) {
                callback?.let { it() }
                callback = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
