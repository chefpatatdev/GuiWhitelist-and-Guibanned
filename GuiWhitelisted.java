package clientname.gui;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Mouse;

import clientname.Client;
import clientname.https.HTTPFunctions;
import clientname.https.HWID;
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


public class GuiWhitelisted extends GuiScreen {

	private ITextComponent[] message;
	private int messageLengthTimesFontHeight;
	private int secondsLeft = 0;

	private static Timer timer = new Timer();

	private GuiButton refreshButton;

	public GuiWhitelisted()
	{

		message = new ITextComponent[] {
				new TextComponentString("Hallo,"),
				new TextComponentString(""),
				new TextComponentString("Bedankt voor de Midusa-client te gebruiken!"),
				new TextComponentString(""),
				new TextComponentString("Op deze client staat wel een whitelist die je moet aanvragen bij chefpatat."),
				new TextComponentString(""),
				new TextComponentString("Als er om uw HWID gevraagt word:"),
				new TextComponentString(""),
				new TextComponentString(HWID.get()).setStyle(
						new Style().setColor(TextFormatting.GOLD)
						.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to copy your HWID").setStyle(new Style().setColor(TextFormatting.GREEN))))
						.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_CLIPBOARD, HWID.get()))
						),
				new TextComponentString(""),
		};

	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
	}

	@Override
	public void initGui()
	{
		this.buttonList.clear();

		this.messageLengthTimesFontHeight = this.message.length * this.fontRendererObj.FONT_HEIGHT;
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height - 25, 98, 20, TextFormatting.RED + "Close"));
		this.buttonList.add(refreshButton = new GuiButton(1, width / 2 + 2, height - 25, 98, 20, "Refresh"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.id == 0)
		{
			mc.displayGuiScreen(null);
		}
		if (button.id == 1 && secondsLeft == 0)
		{

			boolean isWhitelisted = HTTPFunctions.isWhitelisted();

			if(!isWhitelisted) {

				secondsLeft = 6;
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						secondsLeft--;
						if(secondsLeft == 0) {
							refreshButton.displayString = "Refresh Again";
							refreshButton.enabled = true;
							cancel();
							return;
						}

						refreshButton.enabled = false;
						refreshButton.displayString = TextFormatting.AQUA + "" + secondsLeft + "...";
					}
				}, 0, 1000);
			}
			else {
				Client.getInstance().isWhitelisted = true;
				mc.displayGuiScreen(null);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();

		int i = this.height / 2 - this.messageLengthTimesFontHeight / 2;


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
		int i = this.height / 2 - this.messageLengthTimesFontHeight / 2;

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

}