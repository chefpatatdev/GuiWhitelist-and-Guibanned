package clientname.gui;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.lwjgl.opengl.GL11;

import clientname.https.HWID;
import clientname.util.AnimatedResourceLocation;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.MathHelper;

public class GuiHWIDBanned extends GuiScreen {

	private AnimatedResourceLocation gif;

	static Clip clip; //must be static for resizing the window issue of double music

	private ITextComponent[] message;
	private int messageLengthTimesFontHeight;
	
	public GuiHWIDBanned(String reason)
	{
		gif = new AnimatedResourceLocation("clientname/ban", 44, 1);
		
		message = new ITextComponent[] {
				new TextComponentString(TextFormatting.GOLD + "" + TextFormatting.BOLD + "You have been HWID banned from ExampleClient!"),
				new TextComponentString(""),
				new TextComponentString("You have been banned for: "),
				new TextComponentString(""),
				new TextComponentString(TextFormatting.AQUA + reason),
				new TextComponentString(""),
				new TextComponentString("You can appeal your ban at " + TextFormatting.YELLOW + "http://exampleclient.tk/").setStyle(
						new Style()
						.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://exampleclient.tk/"))
						.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to open link").setStyle(new Style().setColor(TextFormatting.GREEN))))),
				new TextComponentString(""),
				new TextComponentString("Your HWID is: " + TextFormatting.RED + HWID.get()).setStyle(
						new Style()
						.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to copy your HWID").setStyle(new Style().setColor(TextFormatting.GREEN))))
						.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_CLIPBOARD, HWID.get()))
						),
				new TextComponentString(""),
		};
		
	}


	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, "Guess i'll just go outside"));
		gif = new AnimatedResourceLocation("clientname/ban", 44, 1);
		
		if(clip == null) {
			try {
				InputStream in = mc.mcDefaultResourcePack.getInputStream(new ResourceLocation("clientname/ban/music.wav"));
				clip = AudioSystem.getClip();
		        clip.open(AudioSystem.getAudioInputStream(in));
		        clip.start();
			    
			}
			catch(Exception e) {
				
			}
		}
		this.messageLengthTimesFontHeight = this.message.length * this.fontRendererObj.FONT_HEIGHT;
		
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
		
		gif.update();
		mc.getTextureManager().bindTexture(gif.getTexture());
		
		GL11.glPushMatrix();
		//GL11.glScaled(0.2,  0.2, 0.2);
		GL11.glTranslated(this.width / 2 - 494 / 5, this.height / 2 - 60, 0);
		GL11.glScaled(0.4, 0.4, 0);
		drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 498, 494, 498, 494);
		GL11.glPopMatrix();

		int i = 50 - this.messageLengthTimesFontHeight / 2;


		for (ITextComponent s : this.message)
		{
			this.drawCenteredString(this.fontRendererObj, s.getFormattedText(), this.width / 2, i, 16777215);
			i += this.fontRendererObj.FONT_HEIGHT;


		}

		handleComponentHover(findChatComponent(mouseX, mouseY), mouseX, mouseY);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private ITextComponent findChatComponentLine(int mouseY)
	{
		int i = 50 - this.messageLengthTimesFontHeight / 2;

		for (ITextComponent s : this.message)
		{
			int yTop = i;
			int yBottom = i + this.fontRendererObj.FONT_HEIGHT;
			if (mouseY >= yTop && mouseY < yBottom) {
				return s;
			}
			i += this.fontRendererObj.FONT_HEIGHT;
		}

		return null;
	}

	private ITextComponent findChatComponent(int mouseX, int mouseY) {

		ITextComponent s = findChatComponentLine(mouseY);

		if (s == null || !(s instanceof TextComponentString)) {
			return null;
		}

		int stringWidth = this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(((TextComponentString)s).getText(), false));
		int xLeft = this.width / 2 - stringWidth / 2;
		int xRight = this.width / 2 + stringWidth / 2;
		if (mouseX >= xLeft && mouseX < xRight) {
			return s;
		}

		return null;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0)
		{
			ITextComponent ichatcomponent = findChatComponent(mouseX, mouseY);

			if (this.handleComponentClick(ichatcomponent))
			{
				return;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}


	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		mc.shutdown();
	}
	
	@Override
	public void onGuiClosed() {
		try {
			clip.close();
		}
		catch(Exception e) {
			
		}
		super.onGuiClosed();
	}

}