package net.darkhax.bingo.commands;

import java.util.Random;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.darkhax.bingo.ModdedBingo;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bookshelf.util.CommandUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandBingoReroll {

	public static LiteralArgumentBuilder<CommandSource> register() {
		return CommandUtils.createCommand("reroll", 2, ctx -> {
			if(!BingoAPI.GAME_STATE.isActive()) {
				throw new SimpleCommandExceptionType(new TranslationTextComponent("command.bingo.info.notactive")).create();
			}
			Random rand = BingoAPI.GAME_STATE.getRandom(); //is null when loaded from disc
			BingoAPI.GAME_STATE.rollGoals(rand == null ? new Random() : rand);
			ModdedBingo.NETWORK.sendToAllPlayers(new PacketSyncGameState());
			return 1;
		});
	}
}