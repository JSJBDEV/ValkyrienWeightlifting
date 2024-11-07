package ace.actually.valkyrienweightlifting.mixin;

import ace.actually.valkyrienweightlifting.IGymBro;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.apigame.collision.ConvexPolygonc;
import org.valkyrienskies.mod.common.util.EntityShipCollisionUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Mixin(EntityShipCollisionUtils.class)
public class EntityShipCollisionUtilsMixin {

    @Inject(at = @At("HEAD"), method = "getShipPolygonsCollidingWithEntity",cancellable = true)
    private void shipPolygons(Entity entity, Vec3d movement, Box entityBoundingBox, World world, CallbackInfoReturnable<List<ConvexPolygonc>> cir)
    {
        if(entity instanceof IGymBro gymBro)
        {
            if(gymBro.getHoisting()!=-1)
            {
                cir.setReturnValue(new ArrayList<>());
            }
        }
    }

}
