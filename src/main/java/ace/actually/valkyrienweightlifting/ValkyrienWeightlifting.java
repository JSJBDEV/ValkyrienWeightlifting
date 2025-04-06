package ace.actually.valkyrienweightlifting;

import ace.actually.valkyrienweightlifting.items.ScaleItem;
import ace.actually.valkyrienweightlifting.items.StrengthTesterItem;
import io.github.kosmx.emotes.api.events.server.ServerEmoteAPI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChainBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;
import org.joml.Vector3i;
import org.valkyrienskies.core.api.ships.LoadedServerShip;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.GameTickForceApplier;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
import org.valkyrienskies.mod.util.RelocationUtilKt;

public class ValkyrienWeightlifting implements ModInitializer {

    public static final Identifier LIFT_PACKET = new Identifier("weightlifting","weightlifting_packet");
    public static final Identifier HOIST_SYNC = new Identifier("weightlifting","hoist_sync");

    @Override
    public void onInitialize() {
        EmoteInjector.injectEmote();
        registerItems();
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((itemGroup, fabricItemGroupEntries) ->
        {
            if(Registries.ITEM_GROUP.get(ItemGroups.FUNCTIONAL)==itemGroup)
            {
                fabricItemGroupEntries.add(ValkyrienWeightlifting.STRENGTH_TESTER_ITEM);
                fabricItemGroupEntries.add(ValkyrienWeightlifting.SCALE_ITEM);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(LIFT_PACKET,((minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) ->
        {
            int mode  = packetByteBuf.readInt();
            BlockPos lift = packetByteBuf.readBlockPos();
            minecraftServer.execute(()->
            {

                IGymBro gymBro = (IGymBro) serverPlayerEntity;
                ServerWorld world = serverPlayerEntity.getServerWorld();
                switch (mode)
                {
                    case 0 ->
                    {

                        if(serverPlayerEntity.isSneaking())
                        {

                            if(gymBro.getHoisting()!=-1)
                            {
                                gymBro.setHoisting(-1);
                                ServerEmoteAPI.forcePlayEmote(serverPlayerEntity.getUuid(),null);
                            }
                        }
                        else if(!world.getBlockState(lift).isAir() && gymBro.getHoisting()==-1)
                        {
                            if(VSGameUtilsKt.isBlockInShipyard(world,lift))
                            {
                                ServerShip serverShip = VSGameUtilsKt.getShipManagingPos(world,lift);
                                if(gymBro.getStrength()>=serverShip.getInertiaData().getMass())
                                {
                                    gymBro.setStrength(gymBro.getStrength()+(serverShip.getInertiaData().getMass()/4));
                                    gymBro.setHoisting(serverShip.getId());
                                    world.playSound(null,serverPlayerEntity.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS,1,1);
                                }
                                else
                                {
                                    world.playSound(null,serverPlayerEntity.getBlockPos(), SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.PLAYERS,1,1);
                                }

                            }
                            else
                            {

                                if(world.getBlockState(lift).getCollisionShape(world,lift).isEmpty())
                                {
                                    break;
                                }

                                String dimensionId = VSGameUtilsKt.getDimensionId(world);
                                ServerShip serverShip = VSGameUtilsKt.getShipObjectWorld(world).createNewShipAtBlock(VectorConversionsMCKt.toJOML(lift), false, 1, dimensionId);

                                BlockPos centerPos = VectorConversionsMCKt.toBlockPos(serverShip.getChunkClaim().getCenterBlockCoordinates(VSGameUtilsKt.getYRange(world),new Vector3i()));
                                RelocationUtilKt.relocateBlock(world, lift, centerPos, true, serverShip, BlockRotation.NONE);

                                if(world.getBlockState(centerPos).contains(ChainBlock.AXIS))
                                {
                                    Direction.Axis axis = world.getBlockState(centerPos).get(ChainBlock.AXIS);
                                    if(axis== Direction.Axis.Z)
                                    {
                                        RelocationUtilKt.relocateBlock(world, lift.north(), centerPos.north(), true, serverShip, BlockRotation.NONE);
                                        RelocationUtilKt.relocateBlock(world, lift.south(), centerPos.south(), true, serverShip, BlockRotation.NONE);
                                    }
                                    if(axis== Direction.Axis.X)
                                    {
                                        RelocationUtilKt.relocateBlock(world, lift.east(), centerPos.east(), true, serverShip, BlockRotation.NONE);
                                        RelocationUtilKt.relocateBlock(world, lift.west(), centerPos.west(), true, serverShip, BlockRotation.NONE);
                                    }
                                }

                                if(gymBro.getStrength()>=serverShip.getInertiaData().getMass())
                                {
                                    gymBro.setStrength(gymBro.getStrength()+(serverShip.getInertiaData().getMass()/4));
                                    gymBro.setHoisting(serverShip.getId());
                                    world.playSound(null,serverPlayerEntity.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS,1,1);
                                }
                                else
                                {
                                    world.playSound(null,serverPlayerEntity.getBlockPos(), SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.PLAYERS,1,1);

                                }

                            }
                        }
                    }
                    case 1 ->
                    {
                        if(VSGameUtilsKt.isBlockInShipyard(world,lift))
                        {

                            LoadedServerShip serverShip = VSGameUtilsKt.getShipObjectManagingPos(world,lift);

                            GameTickForceApplier gtfa = serverShip.getAttachment(GameTickForceApplier.class);
                            if(gtfa!=null)
                            {

                                Vec3d vec3d = serverPlayerEntity.getRotationVector().normalize().multiply(100000);

                                Vector3d v3d = VectorConversionsMCKt.toJOML(vec3d);
                                gtfa.applyInvariantForce(v3d);
                            }


                        }
                    }
                }




            });
        }));
    }

    public static final StrengthTesterItem STRENGTH_TESTER_ITEM = new StrengthTesterItem(new Item.Settings());
    public static final ScaleItem SCALE_ITEM = new ScaleItem(new Item.Settings());
    private void registerItems()
    {
        Registry.register(Registries.ITEM,new Identifier("valkyrienweightlifting","strength_tester"),STRENGTH_TESTER_ITEM);
        Registry.register(Registries.ITEM,new Identifier("valkyrienweightlifting","scale"),SCALE_ITEM);
    }
}
