package com.shiny.joypadmod;

/*
 * Main class for Joypad mod. This initializes everything.
 */

import net.minecraft.client.Minecraft;

import com.shiny.joypadmod.helpers.LogHelper;
import com.shiny.joypadmod.helpers.McGuiHelper;
import com.shiny.joypadmod.helpers.ModVersionHelper;
import com.shiny.joypadmod.lwjglVirtualInput.VirtualKeyboard;
import com.shiny.joypadmod.lwjglVirtualInput.VirtualMouse;
import com.shiny.joypadmod.minecraftExtensions.JoypadMouseHelper;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = JoypadMod.MODID, name = JoypadMod.NAME, version = ModVersionHelper.VERSION + "-" + JoypadMod.MINVERSION
		+ JoypadMod.REVISION)
// 1.6.4
// @NetworkMod(serverSideRequired = false)
public class JoypadMod
{
	public static final String MODID = "JoypadSplitscreenMod";
	public static final String NAME = "Joypad / SplitScreen Mod";
	public static final float MINVERSION = 0.0952f;
	public static final String REVISION = "";

	public static ControllerSettings controllerSettings;

	private ModVersionHelper modHelper;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LogHelper.Info("preInit");
		controllerSettings = new ControllerSettings(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		LogHelper.Info("init");
		if (!ControllerSettings.grabMouse)
		{
			try
			{
				if (!(Minecraft.getMinecraft().mouseHelper instanceof net.minecraft.util.MouseHelper))
				{
					LogHelper.Warn("Replacing Mousehelper that may have already been replaced by another mod!");
				}
				Minecraft.getMinecraft().mouseHelper = new JoypadMouseHelper();
				LogHelper.Info("Replaced mousehelper in Minecraft with JoypadMouseHelper");
			}
			catch (Exception ex)
			{
				LogHelper.Warn("Unable to exchange mousehelper. Game may grab mouse from keyboard players!");
			}
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		LogHelper.Info("postInit");
		controllerSettings.init();
		try
		{
			VirtualKeyboard.create();
		}
		catch (Exception ex)
		{
			LogHelper.Fatal("Unable to initialize VirtualKeyboard.  Limited compatibility with some mods likely. "
					+ ex.toString());
		}

		try
		{
			VirtualMouse.create();
		}
		catch (Exception ex)
		{
			LogHelper.Fatal("Unable to initialize VirtualMouse.  Unable to continue. " + ex.toString());
			ControllerSettings.modDisabled = true;
		}

		try
		{
			McGuiHelper.create();
		}
		catch (Exception ex)
		{
			LogHelper.Fatal("Unable to initialize McGuiHelper.  Unable to continue. " + ex.toString());
			ControllerSettings.modDisabled = true;
		}

		modHelper = new ModVersionHelper();
		modHelper.gameInit();
	}

}
