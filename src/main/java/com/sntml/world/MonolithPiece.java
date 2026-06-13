package com.sntml.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.WrittenBookItem;

public class MonolithPiece extends StructurePiece {

    public static StructurePieceType TYPE;

    private final int originX;
    private final int originY;
    private final int originZ;

    public MonolithPiece(BlockPos pos) {
        super(TYPE, 0, new BoundingBox(
                pos.getX() - 2, pos.getY(), pos.getZ() - 2,
                pos.getX() + 2, pos.getY() + 12, pos.getZ() + 2
        ));
        this.originX = pos.getX();
        this.originY = pos.getY();
        this.originZ = pos.getZ();
    }

    public MonolithPiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
        super(TYPE, tag);
        this.originX = tag.getInt("ox");
        this.originY = tag.getInt("oy");
        this.originZ = tag.getInt("oz");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
        tag.putInt("ox", originX);
        tag.putInt("oy", originY);
        tag.putInt("oz", originZ);
    }

    @Override
    public void postProcess(WorldGenLevel level,
                            StructureManager structureManager,
                            ChunkGenerator generator,
                            RandomSource random,
                            BoundingBox box,
                            ChunkPos chunkPos,
                            BlockPos pos) {

        // Монолит — столб из чёрного камня высотой 10 блоков
        for (int dy = 0; dy < 10; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    // Делаем его тоньше к верху
                    if (dy > 6 && (Math.abs(dx) + Math.abs(dz)) > 1) continue;
                    BlockPos p = new BlockPos(originX + dx, originY + dy, originZ + dz);
                    if (box.isInside(p)) {
                        level.setBlock(p, Blocks.BLACKSTONE.defaultBlockState(), 2);
                    }
                }
            }
        }

        // Кафедра с книгой рядом
        BlockPos lecternPos = new BlockPos(originX + 2, originY, originZ);
        if (box.isInside(lecternPos)) {
            level.setBlock(lecternPos, Blocks.LECTERN.defaultBlockState(), 2);

            // Кладём книгу в кафедру
            if (level.getBlockEntity(lecternPos) instanceof LecternBlockEntity lectern) {
                ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
                CompoundTag bookTag = new CompoundTag();
                bookTag.putString("title", "DO NOT TOUCH");
                bookTag.putString("author", "???");
                net.minecraft.nbt.ListTag pages = new net.minecraft.nbt.ListTag();
                pages.add(net.minecraft.nbt.StringTag.valueOf(
                    Component.Serializer.toJson(Component.literal("DO NOT TOUCH\n\nDO NOT TOUCH\n\nDO NOT TOUCH\n\nDO NOT TOUCH"))));
                pages.add(net.minecraft.nbt.StringTag.valueOf(
                    Component.Serializer.toJson(Component.literal("it sees you when you look at it."))));
                bookTag.put("pages", pages);
                book.setTag(bookTag);
                lectern.setBook(book);
            }
        }
    }
}
