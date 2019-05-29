package net.darkhax.bingo.api.effects.collection;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public abstract class CollectionEffect {

    public abstract void onItemCollected (EntityPlayerMP player, ItemStack item, Team team);
}