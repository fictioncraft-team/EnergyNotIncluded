package com.github.wintersteve25.energynotincluded.common.utils;

import com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal.CoalGenTE;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base.GeckolibModelBase;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIBaseAnimatedBlockItem;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Supplier;

public final class ONIConstants {
    public static final class Geo {
        public static final GeckolibModelBase<CoalGenTE> COAL_GEN_TE = new GeckolibModelBase<>("machines/power/coal_generator.geo.json", "machines/power/coal_generator.png", "machines/power/coal_generator.animation.json");
        public static final GeckolibModelBase<ONIBaseAnimatedBlockItem> COAL_GEN_IB = new GeckolibModelBase<>(COAL_GEN_TE);
        public static final Supplier<BlockEntityWithoutLevelRenderer> COAL_GEN_ISTER = () -> new GeoItemRendererDefault<>(COAL_GEN_IB);

        private static class GeoItemRendererDefault<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {
            public GeoItemRendererDefault(GeoModel<T> modelProvider) {
                super(modelProvider);
            }
        }
    }

    public static final class PacketType {
        public static final byte REDSTONE_INPUT = 0;
        public static final byte REDSTONE_OUTPUT_LOW = 1;
        public static final byte REDSTONE_OUTPUT_HIGH = 2;
    }

    public static final class TextColor {
        public static final ChatFormatting FURNITURE_CAT_COLOR = ChatFormatting.YELLOW;
        public static final ChatFormatting POWER_CAT_COLOR = ChatFormatting.RED;
        public static final ChatFormatting OXYGEN_CAT_COLOR = ChatFormatting.AQUA;
        public static final ChatFormatting VENTILATION_CAT_COLOR = ChatFormatting.GREEN;
        public static final ChatFormatting PLUMING_CAT_COLOR = ChatFormatting.BLUE;
        public static final ChatFormatting TE_BOUNDING_CAT_COLOR = ChatFormatting.LIGHT_PURPLE;

        public static final ChatFormatting GADGETS = ChatFormatting.DARK_BLUE;
    }

    public static final class Resources {
        public static final ResourceLocation WIDGETS = new ResourceLocation(ONIUtils.MODID, "textures/gui/misc/widgets.png");
        public static final TextureElement BUTTON_BG = TextureElement.createDefault(0, 0);
        public static final TextureElement BUTTON_BG_HOVER = TextureElement.createDefault(16, 0);
        public static final TextureElement INFO_BUTTON = TextureElement.createDefault(34, 0);
        public static final TextureElement ALERT_BUTTON = TextureElement.createDefault(51, 0);
        public static final TextureElement REDSTONE_OUTPUT_BUTTON = TextureElement.createDefault(68, 0);
        public static final TextureElement MODIFICATION_BUTTON = TextureElement.createDefault(85, 0);
        public static final TextureElement REDSTONE_BUTTON_ON = TextureElement.createDefault(102, 0);
        public static final TextureElement REDSTONE_BUTTON_OFF = TextureElement.createDefault(119, 0);
        public static final TextureElement ITEM_SLOT = TextureElement.createSlot(0, 67);
        public static final TextureElement POWER_BAR = new TextureElement(0, 17, 16, 49);
        public static final TextureElement FLAME = new TextureElement(20, 17, 14, 14);
        public static final TextureElement RIGHT_ARROW_BIG = new TextureElement(36, 19, 24, 10);
        public static final TextureElement BATTERY_BG = TextureElement.createDefault(0, 85);
        public static final TextureElement RIGHT_ARROW_BIG_BG = new TextureElement(17, 88, 24, 10);
        public static final TextureElement FLAME_BG = new TextureElement(43, 86, 14, 14);
        public static final TextureElement POWER_BAR_BG = new TextureElement(0, 101, 18, 50);

        public static final ResourceLocation BLANK_GUI = new ResourceLocation(ONIUtils.MODID, "textures/gui/machines/blank_gui.png");
        public static final ResourceLocation CURIOS_GOGGLES = new ResourceLocation(ONIUtils.MODID, "gui/misc/curios/goggles");
    }

    public static final class LangKeys {
        public static final Component MOD_TOOLTIP = Component.translatable("oniutils.tooltips.items.modification");
        public static final Component HOLD_SHIFT = Component.translatable("oniutils.tooltips.items.holdShiftInfo");

        public static final String SEDIMENTARY_ROCK = "sedimentary_rock";
        public static final String COAL_GEN = "coal_generator";

        public static final String VELOCITY = "velocity";
        public static final String ENERGY = "energy";
        public static final String GAS = "gas";
        public static final String FLUID = "fluid";
        public static final String TEMPERATURE = "temperature";
        public static final String COMPLEXITY = "complexity";
    }

    public static final class PlacementConditions {
        private static boolean isValidReplaceableBlock(Level level, BlockPlaceContext context, BlockPos pos) {
            if (!isBlockLoaded(level, pos)) {
                return false;
            }
            
            return level.getBlockState(pos).canBeReplaced(context);
        }

        private static boolean isBlockLoaded(Level level, BlockPos pos) {
            if (level == null) return false;
            if (!level.isInWorldBounds(pos)) {
                return false;
            }
            return level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
        }

        public static boolean fourByFourCondition(BlockPlaceContext context, BlockState state) {
            Level world = context.getLevel();
            BlockPos ogPos = context.getClickedPos();

            switch (state.getValue(BlockStateProperties.FACING)) {
                case NORTH:
                    if (isValidReplaceableBlock(world, context, ogPos.east())) {
                        if (isValidReplaceableBlock(world, context, ogPos.above())) {
                            if (isValidReplaceableBlock(world, context, ogPos.east().above())) {
                                return true;
                            }
                        }
                    }
                    break;
                case SOUTH:
                    if (isValidReplaceableBlock(world, context, ogPos.west())) {
                        if (isValidReplaceableBlock(world, context, ogPos.above())) {
                            if (isValidReplaceableBlock(world, context, ogPos.west().above())) {
                                return true;
                            }
                        }
                    }
                    break;
                case WEST:
                    if (isValidReplaceableBlock(world, context, ogPos.north())) {
                        if (isValidReplaceableBlock(world, context, ogPos.above())) {
                            if (isValidReplaceableBlock(world, context, ogPos.north().above())) {
                                return true;
                            }
                        }
                    }
                    break;
                case EAST:
                    if (isValidReplaceableBlock(world, context, ogPos.south())) {
                        if (isValidReplaceableBlock(world, context, ogPos.above())) {
                            if (isValidReplaceableBlock(world, context, ogPos.south().above())) {
                                return true;
                            }
                        }
                    }
                    break;
            }

            return false;
        }
    }
}
