package net.darkhax.bingo.api.effects.collection;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CollectionEffectAnnouncement extends CollectionEffect {

    @Override
    public void onItemCollected (EntityPlayerMP player, ItemStack item, Team team) {

        final ITextComponent playerName = player.getDisplayName();
        playerName.getStyle().setColor(team.getTeamColorText());

        final ITextComponent itemName = item.getTextComponent();
        itemName.getStyle().setColor(TextFormatting.GRAY);

        player.server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.player.obtained", playerName, itemName));
    }
}