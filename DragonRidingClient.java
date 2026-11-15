package com.dragonriding.client;

import com.dragonriding.DragonInputPayload;
import com.dragonriding.DragonRiding;
import com.dragonriding.entity.RideableDragon;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;

/**
 * Riding controls - same layout as Saint's Dragons (Raevyx):
 *   W/A/S/D + mouse ... move / steer
 *   Jump (or Ascend key) ... take off / rise        Left Alt (Descend) ... descend
 *   Sprint (or Accelerate key) ... boost            Left click ... bite / horn gore
 *   X ... toggle melee (bite <-> horn gore)         R ... roar
 *   H ... summon storm                              G (hold) ... beam (fire)
 *   Ctrl+4 ... flex
 * All the extra keys are rebindable in Controls.
 */
public class DragonRidingClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(DragonRiding.MOD_ID, "main"));

    private static final KeyMapping ASCEND = key("ascend", InputConstants.UNKNOWN.getValue());
    private static final KeyMapping DESCEND = key("descend", InputConstants.KEY_LALT);
    private static final KeyMapping ACCELERATE = key("accelerate", InputConstants.UNKNOWN.getValue());
    private static final KeyMapping ROAR = key("ability_primary", InputConstants.KEY_R);
    private static final KeyMapping STORM = key("ability_secondary", InputConstants.KEY_H);
    private static final KeyMapping BEAM = key("ability_tertiary", InputConstants.KEY_G);
    private static final KeyMapping TOGGLE_MELEE = key("toggle_melee", InputConstants.KEY_X);
    private static final KeyMapping FLEX = key("flex", InputConstants.KEY_4);

    private static KeyMapping key(String name, int code) {
        return KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key." + DragonRiding.MOD_ID + "." + name, InputConstants.Type.KEYSYM, code, CATEGORY));
    }

    private static boolean sent = false;
    private static boolean lastBeam, lastBoost, lastUp, lastDown, lastAttack;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(DragonRiding.DRAGON, DragonRenderer::new);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            LocalPlayer player = client.player;
            if (player == null) {
                sent = false;
                return;
            }
            if (player.getVehicle() instanceof RideableDragon dragon && dragon.getControllingPassenger() == player
                    && client.screen == null) {
                boolean beam = BEAM.isDown();
                boolean boost = client.options.keySprint.isDown() || ACCELERATE.isDown();
                boolean up = client.options.keyJump.isDown() || ASCEND.isDown();
                boolean down = DESCEND.isDown();
                boolean attack = client.options.keyAttack.isDown();

                int action = DragonInputPayload.ACTION_NONE;
                if (attack && !lastAttack) action = DragonInputPayload.ACTION_ATTACK;
                if (ROAR.consumeClick()) action = DragonInputPayload.ACTION_ROAR;
                if (STORM.consumeClick()) action = DragonInputPayload.ACTION_STORM;
                if (TOGGLE_MELEE.consumeClick()) action = DragonInputPayload.ACTION_TOGGLE_MELEE;
                if (FLEX.consumeClick() && ctrlDown(client)) action = DragonInputPayload.ACTION_FLEX;

                dragon.setRiderInput(beam, boost, up, down);
                if (!sent || beam != lastBeam || boost != lastBoost || up != lastUp || down != lastDown
                        || action != DragonInputPayload.ACTION_NONE) {
                    ClientPlayNetworking.send(new DragonInputPayload(beam, boost, up, down, action));
                    sent = true;
                }
                lastBeam = beam;
                lastBoost = boost;
                lastUp = up;
                lastDown = down;
                lastAttack = attack;
            } else {
                sent = false;
                lastAttack = false;
            }
        });
    }

    private static boolean ctrlDown(Minecraft client) {
        return InputConstants.isKeyDown(client.getWindow(), InputConstants.KEY_LCONTROL)
                || InputConstants.isKeyDown(client.getWindow(), InputConstants.KEY_RCONTROL);
    }
}
