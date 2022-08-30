package xyz.edge.ac.user.impl;

import java.util.Collection;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.Edge;
import java.util.ArrayDeque;
import xyz.edge.ac.user.User;

public final class Rotations
{
    private final User user;
    private float yaw;
    private float pitch;
    private float lastYaw;
    private float lastPitch;
    private float deltaYaw;
    private float deltaPitch;
    private float lastDeltaYaw;
    private float lastDeltaPitch;
    private float joltYaw;
    private float joltPitch;
    private float lastJoltYaw;
    private float lastJoltPitch;
    private float gcd;
    private int sensitivity;
    private int lastCinematic;
    private int cinematicTicks;
    private final ArrayDeque<Integer> sensitivitySamples;
    private boolean cinematic;
    private double finalSensitivity;
    
    public Rotations(final User user) {
        this.sensitivitySamples = new ArrayDeque<Integer>();
        this.user = user;
    }
    
    public void handle(final float yaw, final float pitch) {
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        this.yaw = yaw;
        this.pitch = pitch;
        this.lastDeltaYaw = this.deltaYaw;
        this.lastDeltaPitch = this.deltaPitch;
        this.deltaYaw = Math.abs(yaw - this.lastYaw) % 360.0f;
        this.deltaPitch = Math.abs(pitch - this.lastPitch);
        this.lastJoltPitch = this.joltPitch;
        this.lastJoltYaw = this.joltYaw;
        this.joltYaw = Math.abs(this.deltaYaw - this.lastDeltaYaw);
        this.joltPitch = Math.abs(this.deltaPitch - this.lastDeltaPitch);
        this.processCinematic();
        if (this.deltaPitch > 0.0f && this.deltaPitch < 30.0f) {
            this.processSensitivity();
        }
    }
    
    private void processCinematic() {
        final float yawAccelAccel = Math.abs(this.joltYaw - this.lastJoltYaw);
        final float pitchAccelAccel = Math.abs(this.joltPitch - this.lastJoltPitch);
        final boolean invalidYaw = yawAccelAccel < 0.05 && yawAccelAccel > 0.0f;
        final boolean invalidPitch = pitchAccelAccel < 0.05 && pitchAccelAccel > 0.0f;
        final boolean exponentialYaw = String.valueOf(yawAccelAccel).contains("E");
        final boolean exponentialPitch = String.valueOf(pitchAccelAccel).contains("E");
        if (this.sensitivity < 100 && (exponentialYaw || exponentialPitch)) {
            this.cinematicTicks += 3;
        }
        else if (invalidYaw || invalidPitch) {
            ++this.cinematicTicks;
        }
        else if (this.cinematicTicks > 0) {
            --this.cinematicTicks;
        }
        if (this.cinematicTicks > 20) {
            --this.cinematicTicks;
        }
        this.cinematic = (this.cinematicTicks > 8 || Edge.getInstance().getTickManager().getTicks() - this.lastCinematic < 120);
        if (this.cinematic && this.cinematicTicks > 8) {
            this.lastCinematic = Edge.getInstance().getTickManager().getTicks();
        }
    }
    
    private void processSensitivity() {
        final float gcd = (float)MathUtil.getGcd(this.deltaPitch, this.lastDeltaPitch);
        final double sensitivityModifier = Math.cbrt(0.8333 * gcd);
        final double sensitivityStepTwo = sensitivityModifier / 0.6 - 0.3333;
        final double finalSensitivity = sensitivityStepTwo * 200.0;
        this.finalSensitivity = finalSensitivity;
        this.sensitivitySamples.add((int)finalSensitivity);
        if (this.sensitivitySamples.size() >= 40) {
            this.sensitivity = MathUtil.getMode(this.sensitivitySamples);
            final float gcdOne = this.sensitivity / 200.0f * 0.6f + 0.2f;
            this.gcd = gcdOne * gcdOne * gcdOne * 1.2f;
            this.sensitivitySamples.clear();
        }
    }
    
    public User getUser() {
        return this.user;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public float getLastYaw() {
        return this.lastYaw;
    }
    
    public float getLastPitch() {
        return this.lastPitch;
    }
    
    public float getDeltaYaw() {
        return this.deltaYaw;
    }
    
    public float getDeltaPitch() {
        return this.deltaPitch;
    }
    
    public float getLastDeltaYaw() {
        return this.lastDeltaYaw;
    }
    
    public float getLastDeltaPitch() {
        return this.lastDeltaPitch;
    }
    
    public float getJoltYaw() {
        return this.joltYaw;
    }
    
    public float getJoltPitch() {
        return this.joltPitch;
    }
    
    public float getLastJoltYaw() {
        return this.lastJoltYaw;
    }
    
    public float getLastJoltPitch() {
        return this.lastJoltPitch;
    }
    
    public float getGcd() {
        return this.gcd;
    }
    
    public int getSensitivity() {
        return this.sensitivity;
    }
    
    public int getLastCinematic() {
        return this.lastCinematic;
    }
    
    public int getCinematicTicks() {
        return this.cinematicTicks;
    }
    
    public ArrayDeque<Integer> getSensitivitySamples() {
        return this.sensitivitySamples;
    }
    
    public boolean isCinematic() {
        return this.cinematic;
    }
    
    public double getFinalSensitivity() {
        return this.finalSensitivity;
    }
}
