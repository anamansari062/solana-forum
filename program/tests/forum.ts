import * as anchor from "@project-serum/anchor";
import { Program } from "@project-serum/anchor";
import { Forum } from "../target/types/forum";
import { PublicKey } from '@solana/web3.js';
import {encode} from "@project-serum/anchor/dist/cjs/utils/bytes/utf8";

const toBytesInt32 = (num: number): Buffer => {
    const arr = new ArrayBuffer(4);
    const view = new DataView(arr);
    view.setUint32(0, num);
    return Buffer.from(arr);
};

describe("forum", () => {
  // Configure the client to use the local cluster.
  const provider = anchor.AnchorProvider.env()
  anchor.setProvider(provider)

  const program = anchor.workspace.Forum as Program<Forum>;

  let programInfoPda: PublicKey;
  let programInfo = null;

  let question1Pda: PublicKey;
  let question1Info = null;

  let question2Pda: PublicKey;
  let question2Info = null;

  let reply11Pda: PublicKey;
  let reply11Info = null;

  let reply12Pda: PublicKey;
  let reply12Info = null;

  let reply21Pda: PublicKey;
  let reply21Info = null;

  it("Is program info initialized!", async () => {
    const [newProgramInfoPda, _] = anchor.web3.PublicKey.findProgramAddressSync(
          [
              encode("program_info"),
          ],
          program.programId
      );
    
    programInfoPda = newProgramInfoPda

    // initializes the program info
    const transactionSignature = await program.methods
        .initializeProgramInfo()
        .accounts({
            author: provider.wallet.publicKey,
            programInfo: programInfoPda,
        })
        .rpc();

    programInfo = await program.account.programInfo.fetch(programInfoPda);

    console.log(programInfo)

  });

  it("it creates a question 1", async () => {

    const [newQuestionPda, _] = anchor.web3.PublicKey.findProgramAddressSync(
          [
              encode("question"),
              toBytesInt32(programInfo.questionCount)
          ],
          program.programId
      );

    question1Pda = newQuestionPda;

    const transactionSignature = await program.methods
        .createQuestion("First Question")
        .accounts({
            question: question1Pda,
            author: provider.wallet.publicKey,
            programInfo: programInfoPda,
        })
        .rpc();

    question1Info = await program.account.question.fetch(question1Pda);

    console.log(question1Info)

  });

  it("it creates question 2", async () => {
    programInfo = await program.account.programInfo.fetch(programInfoPda);

    const [newQuestionPda, _] = anchor.web3.PublicKey.findProgramAddressSync(
          [
              encode("question"),
              toBytesInt32(programInfo.questionCount)
          ],
          program.programId
      );

    question2Pda = newQuestionPda;

    const transactionSignature = await program.methods
        .createQuestion("First Question")
        .accounts({
            question: question2Pda,
            author: provider.wallet.publicKey,
            programInfo: programInfoPda,
        })
        .rpc();

    question2Info = await program.account.question.fetch(question2Pda);

    console.log(question2Info)
  
  });

  it("it creates a reply for question 1", async () => {

    const [newReplyPda, _] = anchor.web3.PublicKey.findProgramAddressSync(
          [
              encode("reply"),
              toBytesInt32(question1Info.questionId),
              toBytesInt32(question1Info.replyCount)
          ],
          program.programId
      );
    
    reply11Pda = newReplyPda

    const transactionSignature = await program.methods
        .createReply("First Reply for first question")
        .accounts({
            reply: reply11Pda,
            question: question1Pda,
            author: provider.wallet.publicKey,
        })
        .rpc();

    reply11Info = await program.account.reply.fetch(reply11Pda);

    console.log(reply11Info)

  });

  it("it creates another reply for question 1", async () => {
    question1Info = await program.account.question.fetch(question1Pda);

    const [newReplyPda, _] = anchor.web3.PublicKey.findProgramAddressSync(
      [
          encode("reply"),
          toBytesInt32(question1Info.questionId),
          toBytesInt32(question1Info.replyCount)
      ],
      program.programId
    );

    reply12Pda = newReplyPda 

    const transactionSignature = await program.methods
        .createReply("Second Reply for first question")
        .accounts({
            reply: reply12Pda,
            question: question1Pda,
            author: provider.wallet.publicKey,
        })
        .rpc();

    reply12Info = await program.account.reply.fetch(reply12Pda);

    console.log(reply12Info)

  
  });

  it("it creates reply for question 2", async () => {
    const [newReplyPda, _] = anchor.web3.PublicKey.findProgramAddressSync(
      [
          encode("reply"),
          toBytesInt32(question2Info.questionId),
          toBytesInt32(question2Info.replyCount)
      ],
      program.programId
    );

    reply21Pda = newReplyPda

    const transactionSignature = await program.methods
        .createReply("First Reply for second question")
        .accounts({
            reply: reply21Pda,
            question: question2Pda,
            author: provider.wallet.publicKey,
        })
        .rpc();

    reply21Info = await program.account.reply.fetch(reply21Pda);

    console.log(reply21Info)
  
  });

  it("it updates question 2", async () => {

    const transactionSignature = await program.methods
        .updateQuestion("Updated Second Question")
        .accounts({
            question: question2Pda,
            author: provider.wallet.publicKey,
        })
        .rpc();

    let questionInfo = await program.account.question.fetch(question2Pda);

    console.log(questionInfo)
  
  });

  it("it updates the first reply of question 1", async () => {

    const transactionSignature = await program.methods
        .updateReply("Updated first reply of Question 1")
        .accounts({
            reply: reply11Pda,
            author: provider.wallet.publicKey,
        })
        .rpc();

    reply11Info = await program.account.reply.fetch(reply11Pda);

    console.log(reply11Info)
  
  });


});
