package com.latmod.warp_pads.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.EnumPrivacyLevel;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.Slider;
import com.feed_the_beast.ftbl.lib.gui.TextBox;
import com.latmod.warp_pads.WarpPads;
import com.latmod.warp_pads.block.TileWarpPad;
import com.latmod.warp_pads.block.WarpPadNode;
import com.latmod.warp_pads.net.MessageSelectTeleporter;
import com.latmod.warp_pads.net.MessageSetName;
import com.latmod.warp_pads.net.MessageToggleActive;
import com.latmod.warp_pads.net.MessageTogglePrivacy;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
@SideOnly(Side.CLIENT)
public class GuiWarpPad extends GuiBase
{
	private static final IDrawableObject TEXTURE = ImageProvider.get(WarpPads.MOD_ID + ":textures/gui/warp_pad.png");
	private static final IDrawableObject BACKGROUND = TEXTURE.withUVfromCoords(0, 0, 126, 110, 128, 128);
	private static final IDrawableObject SLIDER_TEX = TEXTURE.withUVfromCoords(0, 110, 6, 10, 128, 128);
	private static final IDrawableObject AVAILABLE_ON = TEXTURE.withUVfromCoords(6, 110, 7, 7, 128, 128);
	private static final IDrawableObject AVAILABLE_OFF = TEXTURE.withUVfromCoords(13, 110, 7, 7, 128, 128);
	private static final IDrawableObject BAR_H = TEXTURE.withUVfromCoords(24, 111, 104, 1, 128, 128);
	private static final IDrawableObject BAR_V = TEXTURE.withUVfromCoords(127, 0, 1, 81, 128, 128);

	private class ButtonXPT extends Button
	{
		private final WarpPadNode node;

		private ButtonXPT(WarpPadNode n)
		{
			super(6, 0, 104, 11);
			node = n;
		}

		@Override
		public void onClicked(GuiBase gui, IMouseButton button)
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

	private final TileWarpPad teleporter;
	private final Button buttonPrivacy, buttonToggle;
	private final List<ButtonXPT> buttons;
	private final Slider slider;
	private final TextBox textBox;

	public GuiWarpPad(TileWarpPad te, List<WarpPadNode> t)
	{
		super(126, 110);
		teleporter = te;
		buttons = new ArrayList<>();

		for (WarpPadNode n : t)
		{
			buttons.add(new ButtonXPT(n));
		}

		buttonPrivacy = new Button(105, 5, 16, 16, EnumPrivacyLevel.ENUM_LANG_KEY.translate())
		{
			@Override
			public void onClicked(GuiBase gui, IMouseButton button)
			{
				GuiHelper.playClickSound();
				new MessageTogglePrivacy(teleporter.getPos(), button.isLeft()).sendToServer();
			}
		};

		buttonToggle = new Button(87, 5, 16, 16)
		{
			@Override
			public void onClicked(GuiBase gui, IMouseButton button)
			{
				GuiHelper.playClickSound();
				new MessageToggleActive(teleporter.getPos()).sendToServer();
			}

			@Override
			public String getTitle(GuiBase gui)
			{
				return (teleporter.active ? GuiLang.LABEL_ENABLED : GuiLang.LABEL_DISABLED).translate();
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
	public IDrawableObject getIcon(GuiBase gui)
	{
		return BACKGROUND;
	}

	public void updateData()
	{
		buttonPrivacy.setIcon(teleporter.getPrivacyLevel().getIcon());
		buttonToggle.setIcon(teleporter.active ? GuiIcons.ACCEPT : GuiIcons.ACCEPT_GRAY);
	}
}