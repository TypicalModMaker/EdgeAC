package xyz.edge.ac.user.impl;

import java.util.Iterator;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;

public class Speed
{
    public float getBaseSpeed(final Player player, final float base) {
        return base + this.getPotionLevel(player, PotionEffectType.SPEED) * 0.062f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    public double getBaseSpeed(final Player player) {
        return 0.362 + this.getPotionLevel(player, PotionEffectType.SPEED) * 0.062f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    public double getBaseGroundSpeed(final Player player) {
        return 0.289 + this.getPotionLevel(player, PotionEffectType.SPEED) * 0.062f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    public int getPotionLevel(final Player player, final PotionEffectType effect) {
        final int effectId = effect.getId();
        if (!player.hasPotionEffect(effect)) {
            return 0;
        }
        for (final PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().getId() == effectId) {
                return potionEffect.getAmplifier() + 1;
            }
        }
        return 0;
    }
}
