package dev.clairton.yuki.predictionengine.movementtick;

import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.data.packetentity.PacketEntityRideable;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import org.bukkit.util.Vector;

public class MovementTickerPig extends MovementTickerRideable {
    public MovementTickerPig(GrimPlayer player) {
        super(player);
        movementInput = new Vector(0, 0, 1);
    }

    @Override
    public float getSteeringSpeed() { // Vanilla multiples by 0.225f
        PacketEntityRideable pig = (PacketEntityRideable) player.compensatedEntities.getSelf().getRiding();
        return (float) pig.getAttributeValue(Attributes.GENERIC_MOVEMENT_SPEED) * 0.225f;
    }
}
