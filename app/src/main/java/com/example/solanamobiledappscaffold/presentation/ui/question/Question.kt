package com.example.solanamobiledappscaffold.presentation.ui.question
import com.solana.core.PublicKey
import java.io.Serializable


data class Question(var author: PublicKey, var timestamp: Number, var content: String = "", var question_id: Number, var reply_count: Number, var bump: Number) :
    Serializable