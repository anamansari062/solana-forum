use anchor_lang::prelude::*;
use crate::state::{Question, ProgramInfo};
use crate::errors::QuestionError;

#[derive(Accounts)]
#[instruction(content: String)]
pub struct CreateQuestion<'info> {
    #[account(
        init, 
        payer = author, 
        space = Question::LEN + content.len(), 
        seeds = [b"question", program_info.question_count.to_be_bytes().as_ref()],
        bump
    )]
    pub question: Account<'info, Question>,

	#[account(mut)]
    program_info: Account<'info, ProgramInfo>,

	#[account(mut)]
    pub author: Signer<'info>,

    pub system_program: Program<'info, System>,
}

pub fn create_question(ctx: Context<CreateQuestion>, content: String) -> Result<()> {

    require!(content.chars().count() < 30, QuestionError::QuestionTooLong);
    require!(content.chars().count() > 1, QuestionError::QuestionEmpty);

    let clock: Clock = Clock::get().unwrap();

    ctx.accounts.question.set_inner(
        Question::new(
            ctx.accounts.author.key(),
            clock.unix_timestamp,
            content,
            ctx.accounts.program_info.question_count,
            0,
            *ctx.bumps.get("question").unwrap()
        )
    );

    ctx.accounts.program_info.increment_question_count();

    Ok(())
}