use anchor_lang::prelude::*;
use crate::state::Reply;
use crate::errors::ReplyError;

#[derive(Accounts)]
#[instruction(content: String)]
pub struct UpdateReply<'info> {
    #[account(
        mut,
        seeds = [b"reply", reply.question_id.to_be_bytes().as_ref() ,reply.reply_id.to_be_bytes().as_ref()],
        bump = reply.bump, 
        realloc = Reply::LEN + content.len(),
        realloc::payer = author, 
        realloc::zero = true,
    )]
    pub reply: Account<'info, Reply>,

	#[account(mut)]
    pub author: Signer<'info>,
    pub system_program: Program<'info, System>,
}

pub fn update_reply(ctx: Context<UpdateReply>, content: String) -> Result<()> {

    require!(content.chars().count() < 100, ReplyError::ReplyTooLong);
    require!(content.chars().count() > 1, ReplyError::ReplyEmpty);

    let reply = &mut ctx.accounts.reply;
    reply.content = content;

    Ok(())
}