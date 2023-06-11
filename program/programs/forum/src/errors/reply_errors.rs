use anchor_lang::prelude::*;

#[error_code]
pub enum ReplyError {
    #[msg("The provided reply should be 100 characters long maximum.")]
    ReplyTooLong,
    #[msg("Question cannot not be empty.")]
    ReplyEmpty,
}