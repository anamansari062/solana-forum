use anchor_lang::prelude::*;
use instructions::*;

mod state;
mod instructions;
mod errors;

declare_id!("Gs4PpmRLnwjra33Y2kaxHwyM6KXBMmPAsw8FEFhyi8rF");

#[program]
pub mod forum {
    use super::*;

    pub fn initialize_program_info(ctx: Context<InitializeProgramInfo>) -> Result<()> {
        instructions::initialize_program_info(ctx)
    }

    pub fn create_question(ctx: Context<CreateQuestion>, content: String) -> Result<()> {
        instructions::create_question(ctx, content)
    }

    pub fn create_reply(ctx: Context<CreateReply>, content: String) -> Result<()> {
        instructions::create_reply(ctx, content)
    }

    pub fn update_question(ctx: Context<UpdateQuestion>, content: String) -> Result<()> {
        instructions::update_question(ctx, content)
    }

    pub fn update_reply(ctx: Context<UpdateReply>, content: String) -> Result<()> {
        instructions::update_reply(ctx, content)
    }

}
