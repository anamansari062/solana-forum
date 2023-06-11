package com.example.solanamobiledappscaffold.presentation.ui.reply

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.GuardedBy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solanamobiledappscaffold.common.Constants
import com.example.solanamobiledappscaffold.common.Constants.getSolanaExplorerUrl
import com.example.solanamobiledappscaffold.databinding.FragmentReplyBinding
import com.example.solanamobiledappscaffold.presentation.ui.extensions.copyToClipboard
import com.example.solanamobiledappscaffold.presentation.ui.extensions.openInBrowser
import com.example.solanamobiledappscaffold.presentation.ui.extensions.showSnackbar
import com.example.solanamobiledappscaffold.presentation.ui.extensions.showSnackbarWithAction
import com.example.solanamobiledappscaffold.presentation.ui.question.Question
import com.example.solanamobiledappscaffold.presentation.utils.StartActivityForResultSender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReplyFragment : Fragment() {

    private var _binding: FragmentReplyBinding? = null
    private lateinit var adapter: ReplyAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: ReplyViewModel by viewModels()

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            intentSender.onActivityComplete()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReplyBinding.inflate(inflater, container, false)

        val question = arguments?.getSerializable("question") as Question?

        if(question!=null){
            binding.textViewQuestion.text = question.content
            binding.textViewDateReply.text = question.timestamp.toString()
            binding.textViewUserReply.text = question.author.toString()

            binding.recyclerViewReplies.layoutManager = LinearLayoutManager(context)
            adapter = ReplyAdapter(Constants.reply_list)
            binding.recyclerViewReplies.adapter = adapter
        }
        else{
            binding.textViewQuestion.text = "No question found"
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkWalletConnected({
            enableWallet()
        }, {
            disableWallet()
        })

        observeViewModel()
    }

    private fun checkWalletConnected(view: View, action: () -> Unit) {
        viewModel.uiState.value.wallet?.publicKey58?.let {
            action.invoke()
        } ?: view.showSnackbar("Connect a wallet first!")
    }

    private fun checkWalletConnected(
        positiveAction: () -> Unit,
        negativeAction: () -> Unit,
    ) {
        viewModel.uiState.value.wallet?.publicKey58?.let {
            positiveAction.invoke()
        } ?: negativeAction.invoke()
    }

    private fun enableWallet() {

    }

    private fun disableWallet() {

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            with(viewModel) {
                uiState.collect { uiState ->
                    uiState.signedMessage?.let {
                        requireView().showSnackbarWithAction("Signed message: $it") {
                            requireContext().copyToClipboard(text = it)
                            requireView().showSnackbar("Copied to clipboard")
                        }
                    }

                    uiState.transactionID?.let {
                        requireView().showSnackbarWithAction("Transaction Signature: $it", "View") {
                            requireContext().openInBrowser(getSolanaExplorerUrl(it))
                        }
                    }
                }
            }
        }
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
