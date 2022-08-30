package xyz.edge.ac.util.hitbox;

import org.bukkit.entity.Entity;

public enum HitboxExpansion
{
    PLAYER("Player", 0.4), 
    CREEPER("Creeper", 0.4), 
    SKELETON("Skeleton", 0.4), 
    SPIDER("Spider", 0.8), 
    GIANT("Giant", 2.5), 
    ZOMBIE("Zombie", 0.4), 
    SLIME("Slime", 1.12), 
    GHAST("Ghast", 2.1), 
    PIG_ZOMBIE("PigZombie", 0.4), 
    ENDERMAN("Enderman", 0.4), 
    CAVE_SPIDER("CaveSpider", 0.45), 
    SILVERFISH("Silverfish", 0.3), 
    BLAZE("Blaze", 0.4), 
    MAGMA_CUBE("LavaSlime", 1.12), 
    ENDER_DRAGON("EnderDragon", 8.1), 
    WITHER("WitherBoss", 0.55), 
    BAT("Bat", 0.35), 
    WITCH("Witch", 0.4), 
    ENDERMITE("Endermite", 0.3), 
    GUARDIAN("Guardian", 0.525), 
    PIG("Pig", 0.55), 
    SHEEP("Sheep", 0.55), 
    COW("Cow", 0.55), 
    CHICKEN("Chicken", 0.3), 
    SQUID("Squid", 0.5), 
    WOLF("Wolf", 0.5), 
    MUSHROOM_COW("MushroomCow", 0.55), 
    SNOWMAN("SnowMan", 0.45), 
    OCELOT("Ozelot", 0.4), 
    IRON_GOLEM("VillagerGolem", 0.8), 
    HORSE("EntityHorse", 0.7982), 
    RABBIT("Rabbit", 0.3), 
    VILLAGER("Villager", 0.4);
    
    private final double expansion;
    private final String name;
    
    private HitboxExpansion(final String name, final double expansion) {
        this.name = name;
        this.expansion = expansion;
    }
    
    public static double getExpansion(final Entity entity) {
        for (final HitboxExpansion hitboxExpansion : values()) {
            final String name = entity.getType().getName();
            if (name != null && name.equalsIgnoreCase(hitboxExpansion.getName())) {
                return hitboxExpansion.getExpansion();
            }
        }
        return 0.4;
    }
    
    public double getExpansion() {
        return this.expansion;
    }
    
    public String getName() {
        return this.name;
    }
}
