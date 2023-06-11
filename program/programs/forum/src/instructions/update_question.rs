use anchor_lang::prelude::*;
use crate::state::Question;
use crate::errors::QuestionError;

#[derive(Accounts)]
#[instruction(content: String)]
pub struct UpdateQuestion<'info> {
    #[account(
        mut,
        seeds = [b"question", question.question_id.to_be_bytes().as_ref()],
        bump = question.bump, 
        realloc = Question::LEN + content.len(),
        realloc::payer = author, 
        realloc::zero = true,
    )]
    pub question: Account<'info, Question>,

	#[account(mut)]
    pub author: Signer<'info>,
    pub system_program: Program<'info, System>,
}

pub fn update_question(ctx: Context<UpdateQuestion>, content: String) -> Result<()> {

    require!(content.chars().count() < 30, QuestionError::QuestionTooLong);
    require!(content.chars().count() > 1, QuestionError::QuestionEmpty);

    let question = &mut ctx.accounts.question;
    question.content = content;

    Ok(())
}