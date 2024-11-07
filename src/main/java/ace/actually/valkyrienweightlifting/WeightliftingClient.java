package ace.actually.valkyrienweightlifting;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;
import org.valkyrienskies.mod.common.world.RaycastUtilsKt;

public class WeightliftingClient implements ClientModInitializer {

    private static final KeyBinding LIFT_BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.weightlifting.lift", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_CAPS_LOCK, // The keycode of the key
            "category.weightlifting" // The translation key of the keybinding's category.
    ));

    private static final KeyBinding PUSH_BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.weightlifting.push", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_CAPS_LOCK, // The keycode of the key
            "category.weightlifting" // The translation key of the keybinding's category.
    ));


    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (LIFT_BIND.wasPressed()) {
                PacketByteBuf byteBuf = PacketByteBufs.create();
                RaycastContext context = new RaycastContext(client.getCameraEntity().getEyePos(), client.getCameraEntity().getEyePos().add(client.getCameraEntity().getRotationVector().multiply(2)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, client.player);
                BlockHitResult result = RaycastUtilsKt.clipIncludeShips(client.world,context);
                byteBuf.writeInt(0);
                byteBuf.writeBlockPos(result.getBlockPos());
                ClientPlayNetworking.send(ValkyrienWeightlifting.LIFT_PACKET,byteBuf);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (PUSH_BIND.wasPressed()) {
                PacketByteBuf byteBuf = PacketByteBufs.create();
                RaycastContext context = new RaycastContext(client.getCameraEntity().getEyePos(), client.getCameraEntity().getEyePos().add(client.getCameraEntity().getRotationVector().multiply(2)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, client.player);
                BlockHitResult result = RaycastUtilsKt.clipIncludeShips(client.world,context);
                byteBuf.writeInt(1);
                byteBuf.writeBlockPos(result.getBlockPos());
                ClientPlayNetworking.send(ValkyrienWeightlifting.LIFT_PACKET,byteBuf);
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(ValkyrienWeightlifting.HOIST_SYNC,(minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) ->
        {
            long id = packetByteBuf.readLong();
            minecraftClient.execute(()->
            {
                IGymBro gymBro = (IGymBro) minecraftClient.player;
                gymBro.setHoisting(id);
            });
        });
    }
}
