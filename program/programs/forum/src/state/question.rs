use anchor_lang::prelude::*;

#[account]
pub struct Question {
    pub author: Pubkey,
    pub timestamp: i64,
    pub content: String,
    pub question_id: u32,
    pub reply_count: u32,
    pub bump: u8
}

const DISCRIMINATOR_LENGTH: usize = 8;
const PUBLIC_KEY_LENGTH: usize = 32;
const TIMESTAMP_LENGTH: usize = 8;
const STRING_LENGTH_PREFIX: usize = 4;
const QUESTION_ID: usize = 4;
const REPLY_COUNT: usize = 4;
const BUMP_LENTH: usize = 1; 


impl Question {

    pub const LEN: usize = DISCRIMINATOR_LENGTH
        + BUMP_LENTH
        + PUBLIC_KEY_LENGTH
        + TIMESTAMP_LENGTH
        + STRING_LENGTH_PREFIX
        + QUESTION_ID
        + REPLY_COUNT;

    pub fn new(author: Pubkey, timestamp: i64, content: String, question_id: u32, reply_count: u32, bump: u8) -> Self {
        Question {
            author,
            timestamp,
            content,
            question_id,
            reply_count,
            bump,
        }
    }

    pub fn increment_reply_count(&mut self) {
        self.reply_count += 1;
    }
}