use anchor_lang::prelude::*;

#[account]
pub struct Reply {
    pub author: Pubkey,
    pub timestamp: i64,
    pub content: String,
    pub question_id: u32,
    pub reply_id: u32,
    pub bump: u8
}

const DISCRIMINATOR_LENGTH: usize = 8;
const PUBLIC_KEY_LENGTH: usize = 32;
const TIMESTAMP_LENGTH: usize = 8;
const STRING_LENGTH_PREFIX: usize = 4;
const QUESTION_ID: usize = 4;
const REPLY_ID: usize = 4;
const BUMP_LENTH: usize = 1; 


impl Reply {

    pub const LEN: usize = DISCRIMINATOR_LENGTH
        + BUMP_LENTH
        + PUBLIC_KEY_LENGTH
        + TIMESTAMP_LENGTH
        + STRING_LENGTH_PREFIX
        + QUESTION_ID
        + REPLY_ID;

    pub fn new(author: Pubkey, timestamp: i64, content: String, question_id: u32, reply_id: u32,bump: u8) -> Self {
        Reply {
            author,
            timestamp,
            content,
            question_id,
            reply_id,
            bump,
        }
    }
}