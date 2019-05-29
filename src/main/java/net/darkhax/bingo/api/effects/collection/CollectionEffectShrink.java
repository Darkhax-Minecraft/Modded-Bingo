package net.darkhax.bingo.api.effects.collection;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class CollectionEffectShrink extends CollectionEffect {

    @Override
    public void onItemCollected (EntityPlayerMP player, ItemStack item, Team team) {

        item.shrink(1);
    }
}