package com.example.solanamobiledappscaffold.domain.use_case.pda

import android.util.Log
import com.example.solanamobiledappscaffold.BuildConfig
import com.metaplex.kborsh.Borsh
import com.solana.Solana
import com.solana.api.AccountInfoSerializer
import com.solana.api.getAccountInfo
import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import com.solana.models.buffer.AccountInfoData
import com.solana.networking.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.solana.networking.serialization.serializers.solana.AnchorInstructionSerializer
import com.solana.programs.SystemProgram
import kotlinx.serialization.Serializable

class PdaUseCase {
    private val TAG = "HomeViewModel"

    private fun getProgramInfoPda(): PublicKey.ProgramDerivedAddress {
        return PublicKey.findProgramAddress(listOf("program_info".toByteArray()), PublicKey(BuildConfig.PROGRAM_ID))
    }

    /// TODO: Add question count from program info. Fetch Question count from program info account
    public fun getQuestionPda(): PublicKey.ProgramDerivedAddress {
        val buffer = ByteArray(4)
        val questionCount = write4BytesToBuffer(buffer, 0, 5)
        return PublicKey.findProgramAddress(listOf("question".toByteArray(), questionCount), PublicKey(BuildConfig.PROGRAM_ID))
    }

    /// TODO: Add reply count and question id from question. Fetch reply count and question id from question account
    private fun getReplyPda(): PublicKey.ProgramDerivedAddress {
        val buffer = ByteArray(4)
        val replyCount = write4BytesToBuffer(buffer, 0, 5)
        val questionId = write4BytesToBuffer(buffer, 0, 1)
        return PublicKey.findProgramAddress(listOf("reply".toByteArray(), questionId, replyCount), PublicKey(BuildConfig.PROGRAM_ID))
    }

    public fun initializeProgramInfo(author: PublicKey): TransactionInstruction {
        val keys = mutableListOf<AccountMeta>()
        keys.add(AccountMeta(getProgramInfoPda().address, false, true))
        keys.add(AccountMeta(author, true, true))
        keys.add(AccountMeta(SystemProgram.PROGRAM_ID, false, false))

        return TransactionInstruction(
            PublicKey(BuildConfig.PROGRAM_ID),
            keys,
            Borsh.encodeToByteArray(AnchorInstructionSerializer("initialize_program_info"), Args_createProgramInfo()))

    }
    suspend fun createQuestion(author: PublicKey, question: String, solana: Solana): TransactionInstruction {
        fetchQuestionCount(solana)
        val keys = mutableListOf<AccountMeta>()
        keys.add(AccountMeta(getQuestionPda().address, false, true))
        keys.add(AccountMeta(getProgramInfoPda().address, false, true))
        keys.add(AccountMeta(author, true, true))
        keys.add(AccountMeta(SystemProgram.PROGRAM_ID, false, false))

        return TransactionInstruction(
            PublicKey(BuildConfig.PROGRAM_ID),
            keys,
            Borsh.encodeToByteArray(AnchorInstructionSerializer("create_question"), Args_createQuestion(question)))
    }

    public fun createReply(author: PublicKey, reply: String): TransactionInstruction {
        val keys = mutableListOf<AccountMeta>()
        keys.add(AccountMeta(getReplyPda().address, false, true))
        keys.add(AccountMeta(getQuestionPda().address, false, true))
        keys.add(AccountMeta(author, true, true))
        keys.add(AccountMeta(SystemProgram.PROGRAM_ID, false, false))

        return TransactionInstruction(
            PublicKey(BuildConfig.PROGRAM_ID),
            keys,
            Borsh.encodeToByteArray(AnchorInstructionSerializer("create_reply"), Args_replyQuestion(reply)))
    }

     suspend fun fetchQuestionCount(solana: Solana): Long {
         val serializer = AccountInfoSerializer(BorshAsBase64JsonArraySerializer((AccountInfoData.serializer())))
         val account = solana.api.getAccountInfo(serializer, getProgramInfoPda().address).getOrThrow()
         if (account != null) {
             Log.d(TAG, "fetchQuestionCount: ${account.data}")
         }
        return 0
    }

    private fun write4BytesToBuffer(buffer: ByteArray, offset: Int, data: Int): ByteArray {
        buffer[offset + 0] = (data shr 24).toByte()
        buffer[offset + 1] = (data shr 16).toByte()
        buffer[offset + 2] = (data shr 8).toByte()
        buffer[offset + 3] = (data shr 0).toByte()
        return buffer
    }

    @Serializable
    class Args_createProgramInfo()

    @Serializable
    class Args_createQuestion(val question: String)

    @Serializable
    class Args_replyQuestion(val reply: String)
}


