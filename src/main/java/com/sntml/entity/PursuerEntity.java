package com.sntml.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PursuerEntity extends PathfinderMob {

    private int tickCounter = 0;
    private boolean hasSentMessage = false;

    // Cooldown чтобы не срабатывало каждый тик
    private int blindCooldown = 0;

    public PursuerEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        // Случайное блуждание
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.5));
        // Смотреть на игрока
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 32.0f));

        // Цель — игрок
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            tickCounter++;

            if (blindCooldown > 0) {
                blindCooldown--;
            }

            Player nearestPlayer = this.level().getNearestPlayer(this, 64.0);

            if (nearestPlayer != null) {

                // === МЕХАНИКА СООБЩЕНИЯ ===
                // Каждые ~30 секунд (600 тиков) пишет "coming." когда игрок ближе 20 блоков
                if (!hasSentMessage && tickCounter > 200 && this.distanceTo(nearestPlayer) < 20.0) {
                    nearestPlayer.sendSystemMessage(Component.literal("coming."));
                    hasSentMessage = true;
                }

                // === МЕХАНИКА СЛЕПОТЫ И ИСЧЕЗНОВЕНИЯ ===
                // Если игрок подошёл на 3 блока — накладываем слепоту и исчезаем
                double distToPlayer = this.distanceTo(nearestPlayer);

                if (distToPlayer <= 3.0 && blindCooldown == 0) {
                    // Накладываем слепоту на 4 секунды (80 тиков)
                    nearestPlayer.addEffect(
                        new MobEffectInstance(MobEffects.BLINDNESS, 80, 0, false, false)
                    );

                    // Отправляем жуткое сообщение (опционально)
                    nearestPlayer.sendSystemMessage(Component.literal("i see you."));

                    // Чёрный туман перед исчезновением
                    spawnDarkFog();

                    // Удаляем сущность — она исчезает
                    this.discard();
                    return;
                }
            }

            // Сброс сообщения через ~30 секунд
            if (tickCounter > 600) {
                tickCounter = 0;
                hasSentMessage = false;
            }
        }
    }

    /**
     * Густой чёрный туман при исчезновении.
     * Три волны SQUID_INK частиц — широкое облако, плотное ядро, дым вверх.
     */
    private void spawnDarkFog() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        // Широкое рассеянное облако
        serverLevel.sendParticles(
            ParticleTypes.SQUID_INK,
            x, y + 1.0, z,
            60, 1.5, 1.2, 1.5, 0.02
        );

        // Плотное чёрное ядро
        serverLevel.sendParticles(
            ParticleTypes.SQUID_INK,
            x, y + 0.9, z,
            40, 0.4, 0.8, 0.4, 0.0
        );

        // Дым поднимается вверх
        serverLevel.sendParticles(
            ParticleTypes.SQUID_INK,
            x, y + 1.8, z,
            25, 0.6, 0.3, 0.6, 0.04
        );

        // Звук пещеры с заниженным питчем
        serverLevel.playSound(
            null,
            x, y, z,
            SoundEvents.AMBIENT_CAVE.value(),
            SoundSource.HOSTILE,
            1.5f,
            0.6f
        );
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 999.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25) // медленно идёт
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }
}
