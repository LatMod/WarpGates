package com.latmod.warp_gates.client;

import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.GuiLang;
import com.feed_the_beast.ftblib.lib.gui.ScrollBar;
import com.feed_the_beast.ftblib.lib.gui.TextBox;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.EnumPrivacyLevel;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.latmod.warp_gates.WarpGates;
import com.latmod.warp_gates.block.TileWarpGate;
import com.latmod.warp_gates.block.WarpGateNode;
import com.latmod.warp_gates.net.MessageSelectTeleporter;
import com.latmod.warp_gates.net.MessageSetName;
import com.latmod.warp_gates.net.MessageToggleActive;
import com.latmod.warp_gates.net.MessageTogglePrivacy;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
@SideOnly(Side.CLIENT)
public class GuiWarpGate extends GuiBase
{
	private static final Icon TEXTURE = Icon.getIcon(WarpGates.MOD_ID + ":textures/gui/warp_gate.png");
	private static final Icon BACKGROUND = TEXTURE.withUVfromCoords(0, 0, 126, 110, 128, 128);
	private static final Icon SLIDER_TEX = TEXTURE.withUVfromCoords(0, 110, 6, 10, 128, 128);
	private static final Icon AVAILABLE_ON = TEXTURE.withUVfromCoords(6, 110, 7, 7, 128, 128);
	private static final Icon AVAILABLE_OFF = TEXTURE.withUVfromCoords(13, 110, 7, 7, 128, 128);
	private static final Icon BAR_H = TEXTURE.withUVfromCoords(24, 111, 104, 1, 128, 128);
	private static final Icon BAR_V = TEXTURE.withUVfromCoords(127, 0, 1, 81, 128, 128);

	private class ButtonXPT extends Button
	{
		private final WarpGateNode node;

		private ButtonXPT(GuiBase gui, WarpGateNode n)
		{
			super(gui, 6, 0, 104, 11);
			node = n;
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();

			if (node.available && button.isLeft())
			{
				new MessageSelectTeleporter(teleporter.getPos(), node.pos).sendToServer();
				gui.closeGui();
			}
		}

		@Override
		public void renderWidget()
		{
			int ax = getAX();
			int ay = getAY();
			BAR_H.draw(ax, ay + height, 104, 1);
			(node.available ? AVAILABLE_ON : AVAILABLE_OFF).draw(ax, ay + 2, 7, 7);
			drawString(node.name, ax + 10, ay + 2);

			String lvls = Integer.toString(node.energy);

			drawString(lvls, ax + width - getStringWidth(lvls) - 2, ay + 2, Color4I.rgb(node.available ? 0x56FF47 : 0xFF4646), 0);
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
	}

	private final TileWarpGate teleporter;
	private final Button buttonPrivacy, buttonToggle;
	private final List<ButtonXPT> buttons;
	private final ScrollBar scrollBar;
	private final TextBox textBox;

	public GuiWarpGate(TileWarpGate te, List<WarpGateNode> t)
	{
		super(126, 110);
		teleporter = te;
		buttons = new ArrayList<>();

		for (WarpGateNode n : t)
		{
			buttons.add(new ButtonXPT(this, n));
		}

		buttonPrivacy = new Button(this, 105, 5, 16, 16, EnumPrivacyLevel.ENUM_LANG_KEY.translate(), GuiIcons.SECURITY_PUBLIC)
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				new MessageTogglePrivacy(teleporter.getPos(), button.isLeft()).sendToServer();
			}

			@Override
			public Icon getIcon()
			{
				return teleporter.privacyLevel.getIcon();
			}
		};

		buttonToggle = new Button(this, 87, 5, 16, 16)
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				new MessageToggleActive(teleporter.getPos()).sendToServer();
			}

			@Override
			public String getTitle()
			{
				return (teleporter.active ? GuiLang.ENABLED : GuiLang.DISABLED).translate();
			}

			@Override
			public Icon getIcon()
			{
				return teleporter.active ? GuiIcons.ACCEPT : GuiIcons.ACCEPT_GRAY;
			}
		};

		scrollBar = new ScrollBar(this, 114, 23, 6, 81, 10)
		{
			@Override
			public void onMoved()
			{
			}
		};

		textBox = new TextBox(this, 6, 6, 78, 14)
		{
			@Override
			public void onEnterPressed()
			{
				new MessageSetName(teleporter.getPos(), getText()).sendToServer();
			}
		};

		textBox.writeText(teleporter.getName());
		textBox.charLimit = 20;

		//width = 220D;
		//height = 180D;
	}

	@Override
	public void onInit()
	{
		int y = 23;

		for (ButtonXPT b : buttons)
		{
			b.posY = y;
			y += b.height + 1D;
		}
	}

	@Override
	public void addWidgets()
	{
		add(scrollBar);
		add(buttonPrivacy);
		add(buttonToggle);
		addAll(buttons);
		add(textBox);
	}

	@Override
	public boolean isEnabled()
	{
		return !teleporter.isInvalid();
	}

	@Override
	public Icon getIcon()
	{
		return BACKGROUND;
	}
}