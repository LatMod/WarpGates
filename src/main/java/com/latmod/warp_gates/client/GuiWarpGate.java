package com.latmod.warp_gates.client;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.EnumPrivacyLevel;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.Slider;
import com.feed_the_beast.ftbl.lib.gui.TextBox;
import com.feed_the_beast.ftbl.lib.icon.Icon;
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

		private ButtonXPT(WarpGateNode n)
		{
			super(6, 0, 104, 11);
			node = n;
		}

		@Override
		public void onClicked(GuiBase gui, MouseButton button)
		{
			GuiHelper.playClickSound();

			if (node.available && button.isLeft())
			{
				new MessageSelectTeleporter(teleporter.getPos(), node.pos).sendToServer();
				gui.closeGui();
			}
		}

		@Override
		public void renderWidget(GuiBase gui)
		{
			int ax = getAX();
			int ay = getAY();
			BAR_H.draw(ax, ay + height, 104, 1, Color4I.NONE);
			(node.available ? AVAILABLE_ON : AVAILABLE_OFF).draw(ax, ay + 2, 7, 7, Color4I.NONE);
			getFont().drawString(node.name, ax + 10, ay + 2, 0xFFFFFFFF, false);

			String lvls = Integer.toString(node.energy);

			getFont().drawString(lvls, ax + width - getFont().getStringWidth(lvls) - 2, ay + 2, node.available ? 0xFF56FF47 : 0xFFFF4646, false);
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
	}

	private final TileWarpGate teleporter;
	private final Button buttonPrivacy, buttonToggle;
	private final List<ButtonXPT> buttons;
	private final Slider slider;
	private final TextBox textBox;

	public GuiWarpGate(TileWarpGate te, List<WarpGateNode> t)
	{
		super(126, 110);
		teleporter = te;
		buttons = new ArrayList<>();

		for (WarpGateNode n : t)
		{
			buttons.add(new ButtonXPT(n));
		}

		buttonPrivacy = new Button(105, 5, 16, 16, EnumPrivacyLevel.ENUM_LANG_KEY.translate())
		{
			@Override
			public void onClicked(GuiBase gui, MouseButton button)
			{
				GuiHelper.playClickSound();
				new MessageTogglePrivacy(teleporter.getPos(), button.isLeft()).sendToServer();
			}

			@Override
			public Icon getIcon(GuiBase gui)
			{
				return teleporter.privacyLevel.getIcon();
			}
		};

		buttonToggle = new Button(87, 5, 16, 16)
		{
			@Override
			public void onClicked(GuiBase gui, MouseButton button)
			{
				GuiHelper.playClickSound();
				new MessageToggleActive(teleporter.getPos()).sendToServer();
			}

			@Override
			public String getTitle(GuiBase gui)
			{
				return (teleporter.active ? GuiLang.ENABLED : GuiLang.DISABLED).translate();
			}

			@Override
			public Icon getIcon(GuiBase gui)
			{
				return teleporter.active ? GuiIcons.ACCEPT : GuiIcons.ACCEPT_GRAY;
			}
		};

		slider = new Slider(114, 23, 6, 81, 10)
		{
			@Override
			public void onMoved(GuiBase gui)
			{
			}
		};

		slider.slider = SLIDER_TEX;

		textBox = new TextBox(6, 6, 78, 14)
		{
			@Override
			public void onEnterPressed(GuiBase gui)
			{
				new MessageSetName(teleporter.getPos(), getText()).sendToServer();
			}
		};

		textBox.writeText(this, teleporter.getName());
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
		add(slider);
		add(buttonPrivacy);
		add(buttonToggle);
		addAll(buttons);
		add(textBox);
	}

	@Override
	public boolean isEnabled(GuiBase gui)
	{
		return !teleporter.isInvalid();
	}

	@Override
	public Icon getIcon(GuiBase gui)
	{
		return BACKGROUND;
	}
}