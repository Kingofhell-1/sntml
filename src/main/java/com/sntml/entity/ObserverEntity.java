package com.sntml.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Random;

import com.sntml.arg.ARGMechanicHandler;

public class ObserverEntity extends Mob {

    public static final ResourceKey<Level> OBSERVER_DIMENSION =
            ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION,
                new ResourceLocation("sntml", "void_dimension")
            );

    private enum ObserverState {
        WATCHING,
        TRIGGERED,
        FLEEING,
        CHASING
    }

    private ObserverState state = ObserverState.WATCHING;
    private int stateTimer = 0;
    private final Random random = new Random();
    private boolean hasGrantedSpeed = false;

    public ObserverEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        // Смотреть на игрока в режиме наблюдения
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 64.0f, 1.0f));

        // Открывать двери при погоне
        this.goalSelector.addGoal(2, new OpenDoorGoal(this, true));

        // Цель для погони — нужна для MeleeAttackGoal и навигации
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            stateTimer++;

            Player nearestPlayer = this.level().getNearestPlayer(this, 64.0);
            if (nearestPlayer == null) return;

            double dist = this.distanceTo(nearestPlayer);

            // Simulate paranoia increase
            if (nearestPlayer != null && dist < 30) {
                ARGMechanicHandler.checkAndCreateWarningFile((net.minecraft.server.level.ServerPlayer) nearestPlayer, (int)(30 - dist) * 3);
            }

            switch (state) {

                case WATCHING:
                    // Стоит и смотрит — не двигается, скорость 0
                    this.getNavigation().stop();
                    if (dist <= 10.0) {
                        state = ObserverState.TRIGGERED;
                        stateTimer = 0;
                        nearestPlayer.sendSystemMessage(Component.literal("..."));
                    }
                    break;

                case TRIGGERED:
                    this.getNavigation().stop();
                    if (stateTimer >= 20) {
                        if (random.nextBoolean()) {
                            state = ObserverState.FLEEING;
                        } else {
                            state = ObserverState.CHASING;
                            startChasing();
                        }
                        stateTimer = 0;
                    }
                    break;

                case FLEEING:
                    spawnDarkFog();
                    this.discard();
                    break;

                case CHASING:
                    // Строиться к игроку — смотреть в его сторону во время бега
                    this.getLookControl().setLookAt(
                        nearestPlayer,
                        30.0f,
                        30.0f
                    );
                    this.getNavigation().moveTo(nearestPlayer, 1.8);

                    if (dist <= 2.5) {
                        spawnDarkFog();
                        teleportPlayerToDimension(nearestPlayer);
                        this.discard();
                    }

                    if (stateTimer >= 200) {
                        spawnDarkFog();
                        this.discard();
                    }
                    break;
            }
        }
    }

    private void startChasing() {
        if (!hasGrantedSpeed) {
            var speedAttr = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttr != null) {
                speedAttr.setBaseValue(0.55);
            }
            hasGrantedSpeed = true;
        }
    }

    private void spawnDarkFog() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        serverLevel.sendParticles(ParticleTypes.SQUID_INK,
            x, y + 1.0, z, 60, 1.5, 1.2, 1.5, 0.02);

        serverLevel.sendParticles(ParticleTypes.SQUID_INK,
            x, y + 0.9, z, 40, 0.4, 0.8, 0.4, 0.0);

        serverLevel.sendParticles(ParticleTypes.SQUID_INK,
            x, y + 1.8, z, 25, 0.6, 0.3, 0.6, 0.04);

        serverLevel.playSound(null, x, y, z,
            SoundEvents.AMBIENT_CAVE.value(),
            SoundSource.HOSTILE,
            1.5f, 0.6f);
    }

    private void teleportPlayerToDimension(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 120, 1, false, false));
        player.sendSystemMessage(Component.literal("you found me."));

        ServerLevel targetLevel = serverLevel.getServer().getLevel(OBSERVER_DIMENSION);
        if (targetLevel == null) {
            targetLevel = serverLevel.getServer().getLevel(Level.END);
        }
        if (targetLevel == null) return;

        BlockPos safePos = findSafePos(targetLevel, 0, 0);

        serverPlayer.teleportTo(targetLevel,
            safePos.getX() + 0.5,
            safePos.getY(),
            safePos.getZ() + 0.5,
            serverPlayer.getYRot(),
            serverPlayer.getXRot());
    }

    private BlockPos findSafePos(ServerLevel level, int x, int z) {
        level.getChunk(x >> 4, z >> 4);
        for (int y = 5; y < 80; y++) {
            BlockPos feet   = new BlockPos(x, y,     z);
            BlockPos head   = new BlockPos(x, y + 1, z);
            BlockPos ground = new BlockPos(x, y - 1, z);
            if (level.getBlockState(feet).isAir()
             && level.getBlockState(head).isAir()
             && !level.getBlockState(ground).isAir()) {
                return feet;
            }
        }
        return new BlockPos(x, 12, z);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 999.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)  // стоит на месте — скорость 0
                .add(Attributes.FOLLOW_RANGE, 64.0);
    }

    // Запрет садиться в лодку
    @Override
    public boolean canRide(net.minecraft.world.entity.Entity vehicle) {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean isAggressive() {
        return false;
    }
}
