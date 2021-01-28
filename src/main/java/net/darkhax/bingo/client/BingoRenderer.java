package net.darkhax.bingo.client;

import org.lwjgl.opengl.GL11;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.goal.Goal;
import net.darkhax.bingo.api.team.Team;
import net.darkhax.bingo.data.GameState;
import net.darkhax.bookshelf.util.MathsUtils;
import net.darkhax.bookshelf.util.StackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = "bingo")
public class BingoRenderer {

    /**
     * An array of UV coordinate offsets related to the team corners. This is used to render
     * the highlight when a team has completed a goal.
     */
    private static final int[][] teamUVs = new int[][] { new int[] { 0, 0, 11, 11 }, new int[] { 11, 0, 11, 11 }, new int[] { 0, 11, 11, 11 }, new int[] { 11, 11, 11, 11 } };

    public static String getTimeDisplay(long seconds) {

        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        if (h == 0L) {
          return String.format("%02d:%02d",m,s);
        } else {
          return String.format("%d:%02d:%02d",h,m,s);
        }
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {

    	if (BingoAPI.GAME_STATE != null) {

            for (int x = 0; x < 5; x++) {

                for (int y = 0; y < 5; y++) {

                	ItemStack goal = BingoAPI.GAME_STATE.getGoal(x, y);

                	if (goal != null && !goal.isEmpty() && StackUtils.areStacksSimilarWithPartialNBT(event.getItemStack(), goal)) {

                		event.getToolTip().add(TextFormatting.YELLOW + I18n.format("tooltip.bingo.goalitem"));
                	}
                }
            }
    	}
    }

    @SubscribeEvent
    public static void render (TickEvent.RenderTickEvent event) {

    	GameState bingo = BingoAPI.GAME_STATE;

        final Minecraft mc = Minecraft.getMinecraft();

        // Prevent shwoing the game board while crafting, in a gui, on the debug screen, or in
        // the player tab list.
        if (event.phase == Phase.END && BingoAPI.GAME_STATE.isActive() && Minecraft.isGuiEnabled() && mc.currentScreen == null && !mc.gameSettings.showDebugInfo && !(mc.gameSettings.keyBindPlayerList.isKeyDown() && !mc.isIntegratedServerRunning())) {

            if (bingo.isActive() && bingo.getStartTime() > 0 && mc.world != null) {
              String winCountStr = String.valueOf(bingo.getWinCount());
            	long endTime = bingo.getEndTime() >= bingo.getStartTime() ? bingo.getEndTime() : mc.world.getTotalWorldTime();
                mc.fontRenderer.drawString( winCountStr + " Bingo - " + getTimeDisplay((endTime - bingo.getStartTime())/20L), 14, 2, 0xffffff, false);
            }

            final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

            GlStateManager.pushMatrix();

            // Reset the color, to prevent prior render code from changing the board's color.
            GlStateManager.color(1f, 1f, 1f, 1f);

            // Render the background image for the bingo board
            mc.renderEngine.bindTexture(new ResourceLocation("bingo", "hud/bingo_board.png"));
            final float texSize = 256f;
            Gui.drawModalRectWithCustomSizedTexture(10, 10, 0, 0, 132, 132, texSize, texSize);

            // Render all the claim markers to the GUI. This is done as a single buffer to
            // maximize performance. (Can be up to 100X more efficient)
            final BufferBuilder buffBuilder = Tessellator.getInstance().getBuffer();
            buffBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

            for (int x = 0; x < 5; x++) {

                for (int y = 0; y < 5; y++) {

                    final Team[] completedTeams = BingoAPI.GAME_STATE.getCompletionStats(x, y);

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

                    final ItemStack goal = BingoAPI.GAME_STATE.getGoal(x, y);

                    if (goal != null) {

                        itemRender.renderItemAndEffectIntoGUI(null, goal, 16 + x * 24, 16 + y * 24);
                    }
                }
            }

            RenderHelper.disableStandardItemLighting();

            GlStateManager.popMatrix();
        }
    }
}
