package ace.actually.valkyrienweightlifting.mixin;

import ace.actually.valkyrienweightlifting.IGymBro;
import ace.actually.valkyrienweightlifting.ValkyrienWeightlifting;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import io.github.kosmx.emotes.api.events.server.ServerEmoteAPI;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.core.api.world.ServerShipWorld;
import org.valkyrienskies.core.apigame.ShipTeleportData;
import org.valkyrienskies.core.impl.game.ShipTeleportDataImpl;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;
import org.valkyrienskies.mod.common.command.RelativeValue;
import org.valkyrienskies.mod.common.command.RelativeVector3;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements IGymBro {


	private static final TrackedData<NbtCompound> GYM_BRO = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}


	@Unique
	private static KeyframeAnimation HANDS_UP = null;

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		if (!getWorld().isClient && getHoisting() != -1) {
			if (HANDS_UP == null) {
				Optional<KeyframeAnimation> animation = ServerEmoteAPI.getLoadedEmotes().values().stream().filter(a -> a.extraData.get("name").equals("\"Lift\"")).findFirst();
				animation.ifPresent(keyframeAnimation -> HANDS_UP = keyframeAnimation);
			}


			ServerShipWorld serverShipWorld = (ServerShipWorld) VSGameUtilsKt.getVsCore().getHooks().getCurrentShipServerWorld();
			Optional<Ship> optShip = VSGameUtilsKt.getAllShips(getWorld()).stream().filter(a -> a.getId() == getHoisting()).findFirst();

			if (optShip.isPresent()) {
				ServerShip ship = (ServerShip) optShip.get();
				int yvar = (int) Math.ceil((double) (ship.getShipAABB().maxY() - ship.getShipAABB().minY()) / 2);


				double mass = ship.getInertiaData().getMass();

				if (mass * 2 > getStrength()) {
					addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 100, 2, true, false));
				} else if (mass * 3 > getStrength()) {
					addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 100, 1, true, false));
				}


				Vector3d pivot = getPos().add(0, getHeight() + (yvar - 0.5), 0).toVector3f().get(new Vector3d());
				RelativeValue value = new RelativeValue(0, true);
				RelativeVector3 relativeVector3 = new RelativeVector3(value, value, value);
				Vector3d c = new Vector3d();

				ShipTeleportData shipTeleportData = new ShipTeleportDataImpl(pivot, relativeVector3.toEulerRotationFromMCEntity(0, getYaw()), c, c, null, null);
				ValkyrienSkiesMod.getVsCore().teleportShip(serverShipWorld, ship, shipTeleportData);
				if (HANDS_UP != null) {
					if (!ServerEmoteAPI.isForcedEmote(uuid)) {
						ServerEmoteAPI.forcePlayEmote(uuid, HANDS_UP);
					}

				}

			}


		}
	}

	@Override
	public long getHoisting() {
		return dataTracker.get(GYM_BRO).getLong("hoisting");
	}

	@Override
	public void setHoisting(long id) {
		NbtCompound gym = dataTracker.get(GYM_BRO);
		gym.putLong("hoisting", id);
		dataTracker.set(GYM_BRO, gym);
		if(!getWorld().isClient)
		{
			ServerPlayerEntity spe = (ServerPlayerEntity)(Object) this;
			PacketByteBuf byteBuf = PacketByteBufs.create();
			byteBuf.writeLong(id);
			ServerPlayNetworking.send(spe, ValkyrienWeightlifting.HOIST_SYNC,byteBuf);
		}
	}

	@Override
	public double getStrength() {
		return dataTracker.get(GYM_BRO).getDouble("strength");
	}

	@Override
	public void setStrength(double strength) {
		System.out.println(strength);
		NbtCompound gym = dataTracker.get(GYM_BRO);
		gym.putDouble("strength", strength);
		dataTracker.set(GYM_BRO, gym);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		dataTracker.set(GYM_BRO, nbt.getCompound("gym_stats"));

	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.put("gym_stats", dataTracker.get(GYM_BRO));

	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	protected void initDataTracker(CallbackInfo ci) {
		NbtCompound a = new NbtCompound();
		a.putLong("hoisting", -1);
		a.putDouble("strength", 100);
		dataTracker.startTracking(GYM_BRO, a);
	}



}