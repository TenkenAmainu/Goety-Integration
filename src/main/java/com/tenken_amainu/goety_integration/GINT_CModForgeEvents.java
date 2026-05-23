package com.tenken_amainu.goety_integration;

import com.Polarice3.Goety.init.ModKeybindings;
import com.tenken_amainu.goety_integration.network.OpenBrewingPacket;
import com.tenken_amainu.goety_integration.network.OracleNetwork;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GINTMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GINT_CModForgeEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        // 使用 Goety 的女巫长袍键（索引 3）
        if (ModKeybindings.keyBindings.length > 3 && ModKeybindings.keyBindings[3] != null) {
            if (ModKeybindings.keyBindings[3].consumeClick()) {
                OracleNetwork.CHANNEL.sendToServer(new OpenBrewingPacket());
            }
        }
    }
}