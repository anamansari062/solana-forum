package com.example.solanamobiledappscaffold.common

import com.example.solanamobiledappscaffold.presentation.ui.question.Question
import com.example.solanamobiledappscaffold.presentation.ui.reply.Reply
import com.solana.core.PublicKey
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object Constants {
    const val LAMPORTS_PER_SOL = 1000000000L
    const val dAPP_NAME = "Solana Mobile Dapp Scaffold"
    const val SOLANA_URL = "https://solana.com"
    val question_list = listOf(Question(PublicKey("123"), 123, "What is Solana?", 1, 0, 0))
    val reply_list = listOf(Reply(PublicKey("123"), 123, "Solana is a fast, secure, and censorship resistant blockchain providing the open infrastructure required for global adoption.", 1, 0, 0), Reply(PublicKey("234"), 234, "Blockchain technology", 1, 1, 0))

    fun getSolanaExplorerUrl(transactionID: String) =
        "https://explorer.solana.com/tx/$transactionID?cluster=devnet"

    fun formatBalance(balance: Long): BigDecimal {
        val balanceInLamports = BigDecimal.valueOf(balance)
        val sol = BigDecimal.valueOf(LAMPORTS_PER_SOL)
        val balanceInSol = balanceInLamports.divide(sol, MathContext.DECIMAL128)
        return if (balance % LAMPORTS_PER_SOL == 0L) {
            balanceInSol.setScale(0, RoundingMode.DOWN)
        } else {
            balanceInSol.setScale(4, RoundingMode.DOWN)
        }
    }

    fun formatAddress(publicKey: String): String {
        val firstChars = publicKey.substring(0, 4)
        val lastChars = publicKey.substring(publicKey.length - 4)
        return "$firstChars...$lastChars"
    }
}
