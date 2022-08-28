package xyz.edge.ac.packetevents.utils.boundingbox;

import java.util.Objects;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Entity;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class RayTraceResult
{
    private final Vector hitPosition;
    private final Block hitBlock;
    private final BlockFace hitBlockFace;
    private final Entity hitEntity;
    
    private RayTraceResult(@NotNull final Vector hitPosition, @Nullable final Block hitBlock, @Nullable final BlockFace hitBlockFace, @Nullable final Entity hitEntity) {
        Validate.notNull(hitPosition, "Hit position is null!");
        this.hitPosition = hitPosition.clone();
        this.hitBlock = hitBlock;
        this.hitBlockFace = hitBlockFace;
        this.hitEntity = hitEntity;
    }
    
    public RayTraceResult(@NotNull final Vector hitPosition) {
        this(hitPosition, null, null, null);
    }
    
    public RayTraceResult(@NotNull final Vector hitPosition, @Nullable final BlockFace hitBlockFace) {
        this(hitPosition, null, hitBlockFace, null);
    }
    
    public RayTraceResult(@NotNull final Vector hitPosition, @Nullable final Block hitBlock, @Nullable final BlockFace hitBlockFace) {
        this(hitPosition, hitBlock, hitBlockFace, null);
    }
    
    public RayTraceResult(@NotNull final Vector hitPosition, @Nullable final Entity hitEntity) {
        this(hitPosition, null, null, hitEntity);
    }
    
    public RayTraceResult(@NotNull final Vector hitPosition, @Nullable final Entity hitEntity, @Nullable final BlockFace hitBlockFace) {
        this(hitPosition, null, hitBlockFace, hitEntity);
    }
    
    @NotNull
    public Vector getHitPosition() {
        return this.hitPosition.clone();
    }
    
    @Nullable
    public Block getHitBlock() {
        return this.hitBlock;
    }
    
    @Nullable
    public BlockFace getHitBlockFace() {
        return this.hitBlockFace;
    }
    
    @Nullable
    public Entity getHitEntity() {
        return this.hitEntity;
    }
    
    @Override
    public int hashCode() {
        int result = 31 + this.hitPosition.hashCode();
        result = 31 * result + ((this.hitBlock == null) ? 0 : this.hitBlock.hashCode());
        result = 31 * result + ((this.hitBlockFace == null) ? 0 : this.hitBlockFace.hashCode());
        result = 31 * result + ((this.hitEntity == null) ? 0 : this.hitEntity.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RayTraceResult)) {
            return false;
        }
        final RayTraceResult other = (RayTraceResult)obj;
        return this.hitPosition.equals(other.hitPosition) && Objects.equals(this.hitBlock, other.hitBlock) && Objects.equals(this.hitBlockFace, other.hitBlockFace) && Objects.equals(this.hitEntity, other.hitEntity);
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RayTraceResult [hitPosition=");
        builder.append(this.hitPosition);
        builder.append(", hitBlock=");
        builder.append(this.hitBlock);
        builder.append(", hitBlockFace=");
        builder.append(this.hitBlockFace);
        builder.append(", hitEntity=");
        builder.append(this.hitEntity);
        builder.append("]");
        return builder.toString();
    }
}
