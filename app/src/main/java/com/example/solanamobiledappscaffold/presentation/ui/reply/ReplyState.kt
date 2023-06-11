package com.example.solanamobiledappscaffold.presentation.ui.reply

import com.example.solanamobiledappscaffold.domain.model.Wallet
import java.math.BigDecimal

data class ReplyState(
    val isLoading: Boolean = false,
    val balance: BigDecimal = BigDecimal(0),
    var wallet: Wallet? = null,
    val signedMessage: String? = null,
    val transactionID: String? = null,
    val error: String = "",
)
