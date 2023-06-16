package com.example.solanamobiledappscaffold.domain.use_case.pda

import com.example.solanamobiledappscaffold.BuildConfig
import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import com.solana.mobilewalletadapter.clientlib.RpcCluster
import com.solana.programs.SystemProgram

class PdaUseCase {

    private fun getProgramInfoPda(): PublicKey.ProgramDerivedAddress {
        return PublicKey.findProgramAddress(listOf("program_info".toByteArray()), PublicKey(BuildConfig.PROGRAM_ID))
    }

    /// TODO: Add question count from program info. Fetch Question count from program info account
    private fun getQuestionPda(): PublicKey.ProgramDerivedAddress {
        return PublicKey.findProgramAddress(listOf("question".toByteArray()), PublicKey(BuildConfig.PROGRAM_ID))
    }

    private fun getReplyPda(): PublicKey.ProgramDerivedAddress {
        return PublicKey.findProgramAddress(listOf("reply".toByteArray()), PublicKey(BuildConfig.PROGRAM_ID))
    }

    public fun initializeProgramInfo(author: PublicKey): TransactionInstruction {
        val keys = mutableListOf<AccountMeta>()
        keys.add(AccountMeta(getProgramInfoPda().address, false, true))
        keys.add(AccountMeta(author, true, true))
        keys.add(AccountMeta(SystemProgram.PROGRAM_ID, false, false))
        val instruction = TransactionInstruction(
            PublicKey(BuildConfig.PROGRAM_ID),
            keys,
            byteArrayOf()
        )
        return instruction
    }

    /// Incomplete function
    public fun createQuestion(author: PublicKey, question: String, rpcClient: RpcCluster): TransactionInstruction {
        val keys = mutableListOf<AccountMeta>()
        keys.add(AccountMeta(getQuestionPda().address, false, true))
        keys.add(AccountMeta(author, true, true))
        keys.add(AccountMeta(SystemProgram.PROGRAM_ID, false, false))
        val instruction = TransactionInstruction(
            PublicKey(BuildConfig.PROGRAM_ID),
            keys,
            question.toByteArray()
        )
        return instruction
    }

    /// Incomplete function
    public fun createReply(author: PublicKey, reply: String): TransactionInstruction {
        val keys = mutableListOf<AccountMeta>()
        keys.add(AccountMeta(getReplyPda().address, false, true))
        keys.add(AccountMeta(author, true, true))
        keys.add(AccountMeta(SystemProgram.PROGRAM_ID, false, false))
        val instruction = TransactionInstruction(
            PublicKey(BuildConfig.PROGRAM_ID),
            keys,
            reply.toByteArray()
        )
        return instruction
    }
}