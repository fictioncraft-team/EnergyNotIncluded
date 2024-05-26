package com.github.wintersteve25.energynotincluded.common.data.capabilities.germ;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.ONICapabilityProvider;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.germ.api.EnumGermType;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.germ.api.Germs;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.germ.api.IGerms;
import com.github.wintersteve25.energynotincluded.common.registries.ONICapabilities;
import com.github.wintersteve25.energynotincluded.common.registries.ONIConfig;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.LangHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GermEventsHandler {
    public static void entityCapAttachEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity != null) {
            ONICapabilityProvider<IGerms> provider = new ONICapabilityProvider<>(Germs::new, ONICapabilities.GERMS);
            event.addCapability(new ResourceLocation(ONIUtils.MODID, "germs"), provider);
            event.addListener(provider::invalidate);
        }
    }

    public static void itemCapAttachEvent(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (stack != null) {
            ONICapabilityProvider<IGerms> provider = new ONICapabilityProvider<>(Germs::new, ONICapabilities.GERMS);
            event.addCapability(new ResourceLocation(ONIUtils.MODID, "germs"), provider);
            event.addListener(provider::invalidate);
        }
    }

    public static void teCapAttachEvent(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity tileAttached = event.getObject();
        if (tileAttached != null) {
            ONICapabilityProvider<IGerms> provider = new ONICapabilityProvider<>(Germs::new, ONICapabilities.GERMS);
            event.addCapability(new ResourceLocation(ONIUtils.MODID, "germs"), provider);
            event.addListener(provider::invalidate);
        }
    }

    public static void itemToolTipEvent(ItemTooltipEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            ItemStack itemStack = event.getItemStack();
            List<Component> tooltip = event.getToolTip();
            if (itemStack.isEmpty()) {
                return;
            }

            itemStack.getCapability(ONICapabilities.GERMS).ifPresent(i -> {
                tooltip.add(new TranslatableComponent("oniutils.tooltips.germs.itemGerms", Integer.toString(i.getGermAmount()), LangHelper.germ(i.getGermType().getName())));
            });
        }
    }

    public static void infectOnInteractEntitySpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        Player player = event.getPlayer();
        if (player != null) {
            if (!player.level.isClientSide()) {
                Entity target = event.getTarget();

                player.getCapability(ONICapabilities.GERMS).ifPresent(p -> {
                    target.getCapability(ONICapabilities.GERMS).ifPresent(t -> {
                        if (canTransferGerm(p)) {
                            t.addGerm(p.getGermType(), p.getGermAmount());
                            player.displayClientMessage(new TranslatableComponent("oniutils.message.germs.infectEntity", Integer.toString(p.getGermAmount()), LangHelper.germ(p.getGermType().getName())), true);
                        } else if (canTransferGerm(t)) {
                            p.addGerm(t.getGermType(), t.getGermAmount());
                            player.displayClientMessage(new TranslatableComponent("oniutils.message.germs.infectPlayer", Integer.toString(t.getGermAmount()), LangHelper.germ(t.getGermType().getName())), true);
                        }
                    });
                });
            }
        }
    }

    public static void infectOnPickItem(EntityItemPickupEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            if (!player.level.isClientSide()) {
                ItemStack target = event.getItem().getItem();

                player.getCapability(ONICapabilities.GERMS).ifPresent(p -> {
                    target.getCapability(ONICapabilities.GERMS).ifPresent(t -> {
                        if (canTransferGerm(p)) {
                            t.addGerm(p.getGermType(), p.getGermAmount());
                            player.displayClientMessage(new TranslatableComponent("oniutils.message.germs.infectItem", Integer.toString(p.getGermAmount()), LangHelper.germ(p.getGermType().getName())), true);
                        } else if (canTransferGerm(t)) {
                            p.addGerm(t.getGermType(), t.getGermAmount());
                            player.displayClientMessage(new TranslatableComponent("oniutils.message.germs.infectPlayer", Integer.toString(t.getGermAmount()), LangHelper.germ(t.getGermType().getName())), true);
                        }
                    });
                });
            }
        }
    }

    public static void infectOnTossItem(ItemTossEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            if (!player.level.isClientSide()) {
                ItemStack target = event.getEntityItem().getItem();

                player.getCapability(ONICapabilities.GERMS).ifPresent(p -> {
                    target.getCapability(ONICapabilities.GERMS).ifPresent(t -> {
                        if (canTransferGerm(p)) {
                            t.addGerm(p.getGermType(), p.getGermAmount());
                            player.displayClientMessage(new TranslatableComponent("oniutils.message.germs.infectItem", Integer.toString(p.getGermAmount()), LangHelper.germ(p.getGermType().getName())), true);
                            return;
                        } else if (canTransferGerm(t)) {
                            p.addGerm(t.getGermType(), t.getGermAmount());
                            player.displayClientMessage(new TranslatableComponent("oniutils.message.germs.infectPlayer", Integer.toString(t.getGermAmount()), LangHelper.germ(t.getGermType().getName())), true);
                        }
                    });
                });
            }
        }
    }

    public static void infectOnTileInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            if (!player.level.isClientSide()) {
                BlockPos pos = event.getPos();
                BlockEntity target = event.getWorld().getBlockEntity(pos);
                if (target != null) {
                    player.getCapability(ONICapabilities.GERMS).ifPresent(p -> {
                        target.getCapability(ONICapabilities.GERMS).ifPresent(t -> {
                            if (canTransferGerm(p)) {
                                t.addGerm(p.getGermType(), p.getGermAmount());
                                player.displayClientMessage(new TranslatableComponent("oniutils.message.germs.infectItem", Integer.toString(p.getGermAmount()), LangHelper.germ(p.getGermType().getName())), true);
                                return;
                            } else if (canTransferGerm(t)) {
                                p.addGerm(t.getGermType(), t.getGermAmount());
                                player.displayClientMessage(new TranslatableComponent("oniutils.message.germs.infectPlayer", Integer.toString(t.getGermAmount()), LangHelper.germ(t.getGermType().getName())), true);
                            }
                        });
                    });
                }
            }
        }
    }

    public static void keepGermWhilePlaced(BlockEvent.EntityPlaceEvent event) {
        BlockPos pos = event.getPos();
        BlockEntity tileEntity = event.getWorld().getBlockEntity(pos);

        if (event.getEntity() != null) {
            ItemStack itemStack = event.getEntity().getHandSlots().iterator().next();

            if (tileEntity != null) {
                itemStack.getCapability(ONICapabilities.GERMS).ifPresent(stack -> tileEntity.getCapability(ONICapabilities.GERMS).ifPresent(te -> te.setGerm(stack.getGermType(), stack.getGermAmount())));
            }
        }
    }

    private static int germDupSpeed = ONIConfig.Server.GERM_DUP_SPEED_PLAYER.get();

    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player != null) {
            if (!player.level.isClientSide()) {
                player.getCapability(ONICapabilities.GERMS).ifPresent(p -> {
                    germDupSpeed--;

                    EnumGermType germTypes = p.getGermType();
                    int germAmount = p.getGermAmount();

                    if (germAmount > 0 && germTypes != EnumGermType.NOTHING) {

                        if (germDupSpeed < 0) {
                            if (germAmount < ONIConfig.Server.GERM_STOP_DUP_AMOUNT.get()) {
                                p.removeGerm(100);
                            }
                            germDupSpeed = ONIConfig.Server.GERM_DUP_SPEED_PLAYER.get();
                        }

                        if (germTypes == EnumGermType.FOOD_POISON && germAmount > 50000) {
                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER));
                            if (germAmount > 85000) {
                                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS));
                                if (germAmount > 2000000) {
                                    player.hurt(getGermDamageSource(player), 6);
                                }
                            }
                        }

                        if (germTypes == EnumGermType.SLIME_LUNG && germAmount > 50000) {
                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN));
                            if (germAmount > 85000) {
                                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS));
                                if (germAmount > 2000000) {
                                    player.hurt(getGermDamageSource(player), 6);
                                }
                            }
                        }

                        if (germTypes == EnumGermType.ZOMBIE_SPORES && germAmount > 100000) {
                            player.addEffect(new MobEffectInstance(MobEffects.POISON));
                            if (germAmount > 1500000) {
                                player.hurt(getGermDamageSource(player), 6);
                            }
                        }

                        if (germTypes == EnumGermType.FLORAL_SCENTS) {

                        }
                    }
                });
            }
        }
    }

    private static DamageSource getGermDamageSource(Player player) {
        return player.level().damageSources().source(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ONIUtils.MODID, "germ")));
    }

    private static boolean canTransferGerm(IGerms stack) {
        return stack.getGermType() != EnumGermType.NOTHING && stack.getGermType() != EnumGermType.FLORAL_SCENTS && stack.getGermAmount() > 0;
    }
}
