package net.darkhax.bingo.client;

import org.lwjgl.opengl.GL11;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.goal.Goal;
import net.darkhax.bingo.api.team.Team;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = "bingo")
public class BingoRenderer {

    private static final int[][] teamUVs = new int[][] { new int[] { 0, 0, 11, 11 }, new int[] { 11, 0, 11, 11 }, new int[] { 0, 11, 11, 11 }, new int[] { 11, 11, 11, 11 } };

    @SubscribeEvent
    public static void render (TickEvent.RenderTickEvent event) {

        final Minecraft mc = Minecraft.getMinecraft();

        if (event.phase == Phase.END && BingoMod.GAME_STATE.isActive() && Minecraft.isGuiEnabled() && mc.currentScreen == null && !mc.gameSettings.showDebugInfo && !(mc.gameSettings.keyBindPlayerList.isKeyDown() && !mc.isIntegratedServerRunning())) {

            final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
            final EntityPlayerSP player = Minecraft.getMinecraft().player;

            GlStateManager.pushMatrix();

            GlStateManager.color(1f, 1f, 1f, 1f);

            // Render the background image for the bingo board
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("bingo", "hud/bingo_board.png"));
            final float texSize = 256f;
            Gui.drawModalRectWithCustomSizedTexture(10, 10, 0, 0, 132, 132, texSize, texSize);

            // Render all the claim markers to the GUI. This is done as a single buffer to
            // maximize performance. (Can be up to 100X more efficient)
            final BufferBuilder buffBuilder = Tessellator.getInstance().getBuffer();
            buffBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

            for (int x = 0; x < 5; x++) {

                for (int y = 0; y < 5; y++) {

                    final Team[] completedTeams = BingoMod.GAME_STATE.getCompletionStats(x, y);

                    for (final Team team : completedTeams) {

                        if (team != null) {

                            final int[] uvs = teamUVs[team.getTeamCorner()];
                            final float[] color = team.getDyeColor().getColorComponentValues();

                            final int xOffset = 13 + x * 24 + uvs[0];
                            final int yOffset = 13 + y * 24 + uvs[1];
                            final float minU = (132 + uvs[0]) / texSize;
                            final float maxU = (132 + uvs[0] + uvs[2]) / texSize;
                            final float minV = uvs[1] / texSize;
                            final float maxV = (uvs[1] + uvs[3]) / texSize;

                            buffBuilder.pos(xOffset, yOffset + uvs[3], 0).tex(minU, maxV).color(color[0], color[1], color[2], 1f).endVertex();
                            buffBuilder.pos(xOffset + uvs[2], yOffset + uvs[3], 0).tex(maxU, maxV).color(color[0], color[1], color[2], 1f).endVertex();
                            buffBuilder.pos(xOffset + uvs[2], yOffset, 0).tex(maxU, minV).color(color[0], color[1], color[2], 1f).endVertex();
                            buffBuilder.pos(xOffset, yOffset, 0).tex(minU, minV).color(color[0], color[1], color[2], 1f).endVertex();
                        }
                    }
                }
            }

            Tessellator.getInstance().draw();
            GlStateManager.enableLighting();

            // Render items on the bingo board.
            RenderHelper.enableGUIStandardItemLighting();

            for (int x = 0; x < 5; x++) {

                for (int y = 0; y < 5; y++) {

                    final ItemStack goal = BingoMod.GAME_STATE.getGoal(x, y);

                    if (goal != null) {

                        itemRender.renderItemAndEffectIntoGUI(player, goal, 16 + x * 24, 16 + y * 24);
                    }
                }
            }

            RenderHelper.disableStandardItemLighting();

            GlStateManager.popMatrix();
        }
    }

}