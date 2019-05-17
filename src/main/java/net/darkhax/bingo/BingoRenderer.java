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
import org.lwjgl.opengl.GL11;

@EventBusSubscriber(modid = "bingo")
public class BingoRenderer {

	private static final int[][] teamUVs = new int[][] {new int[]{0, 0, 11, 11}, new int[]{11, 0, 11, 11}, new int[]{0, 11, 11, 11}, new int[]{11, 11, 11, 11}};
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
        float texSize = 256f;
    	GuiScreen.drawModalRectWithCustomSizedTexture(10, 10, 0, 0, 132, 132, texSize, texSize);   
        
        BufferBuilder buffBuilder = Tessellator.getInstance().getBuffer();
        buffBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        for (int x = 0; x < 5; x++) {
        
            for (int y = 0; y < 5; y++) {
            
                boolean[] states = BingoMod.currentGame.getCompletionStats(x, y);
            
                for (int corner = 0; corner < 4; corner++) {
                
                    if (states[corner]) {
                    
                        int[] uvs = teamUVs[corner];
                        float[] color = BingoAPI.getTeamColors(corner);
                    
                        int xOffset = 17 + (x * 24) + uvs[0];
                        int yOffset = 17 + (y * 24) + uvs[1];
                        float minU = (132 + uvs[0]) / texSize;
                        float maxU = (132 + uvs[0] + uvs[2]) / texSize;
                        float minV = uvs[1] / texSize;
                        float maxV = (uvs[1] + uvs[3]) / texSize;
                    
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
    			
    	        itemRender.renderItemAndEffectIntoGUI(player, BingoMod.currentGame.getGoal(x, y).getTarget(), 20 + (x * 24), 20 + (y * 24));
    		}
    	}
    	
    	RenderHelper.disableStandardItemLighting();
    	
    	Minecraft.getMinecraft().fontRenderer.drawString("Winner: " + BingoMod.currentGame.checkWinState(), 500, 20, 0);
    	
    	GlStateManager.scale(0.5, 0.5, 10);
    	Minecraft.getMinecraft().fontRenderer.drawString("Bingo " + "V1.0.0" + " by Darkhax", 25, 272, 0);
    	
    	GlStateManager.popMatrix();
    }
    
}