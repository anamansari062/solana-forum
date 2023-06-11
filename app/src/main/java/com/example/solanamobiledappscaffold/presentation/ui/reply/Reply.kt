package com.example.solanamobiledappscaffold.presentation.ui.reply
import com.solana.core.PublicKey

data class Reply(var author : PublicKey, var timestamp : Number, var content : String = "", var question_id: Number, var reply_id : Number, var bump : Number)
