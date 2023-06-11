use anchor_lang::prelude::*;
use crate::state::{Reply, Question};
use crate::errors::ReplyError;

#[derive(Accounts)]
#[instruction(content: String)]
pub struct CreateReply<'info> {
    #[account(
        init, 
        payer = author, 
        space = Reply::LEN + content.len(), 
        seeds = [b"reply", question.question_id.to_be_bytes().as_ref(), question.reply_count.to_be_bytes().as_ref()],
        bump
    )]
    pub reply: Account<'info, Reply>,

	#[account(mut)]
    question: Account<'info, Question>,

	#[account(mut)]
    pub author: Signer<'info>,

    pub system_program: Program<'info, System>,
}

pub fn create_reply(ctx: Context<CreateReply>, content: String) -> Result<()> {

    require!(content.chars().count() < 100, ReplyError::ReplyTooLong);
    require!(content.chars().count() > 1, ReplyError::ReplyEmpty);

    let clock: Clock = Clock::get().unwrap();

    ctx.accounts.reply.set_inner(
        Reply::new(
            ctx.accounts.author.key(),
            clock.unix_timestamp,
            content,
            ctx.accounts.question.question_id,
            ctx.accounts.question.reply_count,
            *ctx.bumps.get("reply").unwrap()
        )
    );

    ctx.accounts.question.increment_reply_count();

    Ok(())
}