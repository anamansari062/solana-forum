use anchor_lang::prelude::*;

#[account]
pub struct ProgramInfo {
    pub author: Pubkey,
    pub question_count: u32,
    pub bump: u8
}

const DISCRIMINATOR_LENGTH: usize = 8;
const PUBLIC_KEY_LENGTH: usize = 32;
const QUESTION_COUNT_LENGTH: usize = 8;
const BUMP_LENTH: usize = 1;


impl ProgramInfo {
    pub const LEN: usize = DISCRIMINATOR_LENGTH 
        + PUBLIC_KEY_LENGTH 
        + QUESTION_COUNT_LENGTH
        + BUMP_LENTH;

    pub fn new(author: Pubkey, question_count: u32, bump: u8) -> Self {
        ProgramInfo {
            author,
            bump,
            question_count,
        }
    }
    
    pub fn increment_question_count(&mut self) {
        self.question_count += 1;
    }

}
