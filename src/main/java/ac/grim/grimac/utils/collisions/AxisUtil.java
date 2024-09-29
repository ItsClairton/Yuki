package ac.grim.grimac.utils.collisions;

import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import com.github.retrooper.packetevents.protocol.world.BlockFace;

public enum AxisUtil {
    EAST(box -> {
        box.maxX = 1;
        return box;
    }),
    WEST(box -> {
        box.minX = 0;
        return box;
    }),
    NORTH(box -> {
        box.minZ = 0;
        return box;
    }),
    SOUTH(box -> {
        box.maxZ = 1;
        return box;
    }),
    UP(box -> {
        box.minY = 0;
        return box;
    }),
    DOWN(box -> {
        box.maxY = 1;
        return box;
    });

    AxisSelect select;

    AxisUtil(AxisSelect select) {
        this.select = select;
    }

    // I couldn't figure out what Mojang was doing but I think this looks nice
    // Bounding boxes just have to be put into the modification thing before into this to be for faces
    public static SimpleCollisionBox combine(SimpleCollisionBox base, SimpleCollisionBox toMerge) {
        boolean insideX = toMerge.minX <= base.minX && toMerge.maxX >= base.maxX;
        boolean insideY = toMerge.minY <= base.minY && toMerge.maxY >= base.maxY;
        boolean insideZ = toMerge.minZ <= base.minZ && toMerge.maxZ >= base.maxZ;

        if (insideX && insideY && !insideZ) {
            return new SimpleCollisionBox(base.minX, base.maxY, Math.min(base.minZ, toMerge.minZ), base.minX, base.maxY, Math.max(base.maxZ, toMerge.maxZ));
        } else if (insideX && !insideY && insideZ) {
            return new SimpleCollisionBox(base.minX, Math.min(base.minY, toMerge.minY), base.minZ, base.maxX, Math.max(base.maxY, toMerge.maxY), base.maxZ);
        } else if (!insideX && insideY && insideZ) {
            return new SimpleCollisionBox(Math.min(base.minX, toMerge.maxX), base.minY, base.maxZ, Math.max(base.minX, toMerge.minX), base.minY, base.maxZ);
        }

        return base;
    }

    public static AxisSelect getAxis(BlockFace face) {
        return switch (face) {
            case EAST -> EAST.select;
            case WEST -> WEST.select;
            case NORTH -> NORTH.select;
            case SOUTH -> SOUTH.select;
            case UP -> UP.select;
            default -> DOWN.select;
        };
    }

    public static boolean isSameAxis(BlockFace one, BlockFace two) {
        return switch (one) {
            case WEST, EAST -> two == BlockFace.WEST || two == BlockFace.EAST;
            case NORTH, SOUTH -> two == BlockFace.NORTH || two == BlockFace.SOUTH;
            case UP, DOWN -> two == BlockFace.UP || two == BlockFace.DOWN;
            default -> false;
        };
    }
}
