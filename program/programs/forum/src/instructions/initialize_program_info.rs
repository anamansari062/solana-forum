use anchor_lang::prelude::*;
use crate::state::ProgramInfo;

#[derive(Accounts)]
pub struct InitializeProgramInfo<'info> {
    #[account(
        init, 
        payer = author, 
        seeds = [b"program_info"], 
        bump, 
        space = ProgramInfo::LEN
    )]
    program_info: Account<'info, ProgramInfo>,

    #[account(mut)]
    author: Signer<'info>,
    
    system_program: Program<'info, System>
}

pub fn initialize_program_info(ctx: Context<InitializeProgramInfo>) -> Result<()> {
    ctx.accounts.program_info.set_inner(
        ProgramInfo::new(
            ctx.accounts.author.key(),
            Default::default(),
            *ctx.bumps.get("program_info").unwrap()
        )
    );
    Ok(())
}