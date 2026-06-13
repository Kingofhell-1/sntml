package com.sntml.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class AltarStructure extends Structure {

    public static final Codec<AltarStructure> CODEC = simpleCodec(AltarStructure::new);

    public AltarStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        int y = context.chunkGenerator().getFirstOccupiedHeight(
                x, z, Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(), context.randomState());

        if (y < 60 || y > 180) return Optional.empty();

        BlockPos pos = new BlockPos(x, y, z);
        return Optional.of(new GenerationStub(pos,
                builder -> builder.addPiece(new AltarPiece(pos))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.ALTAR.get();
    }
}
