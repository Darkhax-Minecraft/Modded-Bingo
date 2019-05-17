package net.darkhax.bingo;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.actors.threadpool.Arrays;

@EventBusSubscriber(modid = "bingo")
public class BingoRenderer {

    @SubscribeEvent
    public static void render(RenderGameOverlayEvent.Post event) {
    	
    	//BingoMod.currentGame.reload(new Random());
    	RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    	EntityPlayerSP player = Minecraft.getMinecraft().player;
    	
    	GlStateManager.pushMatrix();
    	
    	// Sets the scale of the entire HUD. If Z is less than 1 blocks render darker than they should.
    	GlStateManager.scale(2, 2, 1);
    	
    	// Render the background image for the bingo board
    	Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("bingo", "hud/bingo_board.png"));
    	//GuiScreen.drawModalRectWithCustomSizedTexture(10, 10, 0, 0, 132, 133, 256, 256);
    	
    	int[][] teamUVs = new int[4][4];
    	teamUVs[0] = new int[]{0, 0, 11, 11};
    	teamUVs[1] = new int[]{11, 0, 22, 11};
    	teamUVs[2] = new int[]{0, 11, 11, 22};
    	teamUVs[3] = new int[]{11, 11, 22, 22};
    	
    	for (int x = 0; x < 5; x++) {
    		
    		for (int y = 0; y < 5; y++) {
    			
    			boolean[] states = BingoMod.currentGame.getCompletionStats(x, y);
    			
    			for (int corner = 0; corner < 4; corner++) {
    				
    				if (states[corner]) {

        				int[] uvs = teamUVs[corner];
        				float[] color = BingoAPI.getTeamColors(corner);
        				GlStateManager.color(color[0], color[1], color[2]);
    					//drawTexturedModalRect(17 + (x * 24) + uvs[0], 17 + (y * 24) + uvs[1], 132 + uvs[0], uvs[1], uvs[2], uvs[3], 1d);
        				GuiScreen.drawModalRectWithCustomSizedTexture(17 + (x * 24) + uvs[0], 17 + (y * 24) + uvs[1], 132 + uvs[0], uvs[1], uvs[2], uvs[3], 256, 256);
    				}
    			}
    		}
    	}
    	
    	GlStateManager.color(1, 1, 1);
    	
    	// Render items on the bingo board.
    	RenderHelper.enableGUIStandardItemLighting();
    	
    	for (int x = 0; x < 5; x++) {
    		
    		for (int y = 0; y < 5; y++) {
    			
    	        itemRender.renderItemAndEffectIntoGUI(player, BingoMod.currentGame.getGoal(x, y).getTarget(), 20 + (x * 24), 20 + (y * 24));
    		}
    	}
    	
    	RenderHelper.disableStandardItemLighting();
    	
    	Minecraft.getMinecraft().fontRenderer.drawString("Winner: " + BingoMod.currentGame.checkWinState(), 500, 20, 0);
    	
    	GlStateManager.scale(0.5, 0.5, 10);
    	Minecraft.getMinecraft().fontRenderer.drawString("Bingo " + "V1.0.0" + " by Darkhax", 25, 275, 0);
    	
    	GlStateManager.popMatrix();
    }
    
    private static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, double zLevel) {
    	
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }
}