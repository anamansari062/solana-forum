use anchor_lang::prelude::*;

#[error_code]
pub enum QuestionError {
    #[msg("The provided question should be 30 characters long maximum.")]
    QuestionTooLong,
    #[msg("Question cannot not be empty.")]
    QuestionEmpty,
}