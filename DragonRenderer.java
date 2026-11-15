package com.dragonriding.client;

import com.dragonriding.DragonRiding;
import com.dragonriding.entity.RideableDragon;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/**
 * Renders the Raevyx model. Assets (by the entity's registered id "rideable_dragon"):
 *   assets/dragonriding/geckolib/models/entity/rideable_dragon.geo.json
 *   assets/dragonriding/geckolib/animations/entity/rideable_dragon.animation.json
 *   assets/dragonriding/textures/entity/rideable_dragon.png
 * Raevyx model, animations and texture by LilRiceAroni (Saints Dragon).
 */
public class DragonRenderer<R extends LivingEntityRenderState & GeoRenderState>
        extends GeoEntityRenderer<RideableDragon, R> {

    public DragonRenderer(EntityRendererProvider.Context context) {
        super(context, DragonRiding.DRAGON);
    }
}
