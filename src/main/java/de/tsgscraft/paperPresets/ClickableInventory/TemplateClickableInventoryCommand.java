package de.tsgscraft.paperPresets.ClickableInventory;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.tsgscraft.paperPresets.ClickableInventory.Items.ChangeItem;
import de.tsgscraft.paperPresets.ClickableInventory.Items.ChangeItemBuilder;
import de.tsgscraft.paperPresets.ClickableInventory.Items.ChangeItemVariant;
import de.tsgscraft.paperPresets.ClickableInventory.Items.ColorItem;
import de.tsgscraft.paperPresets.PaperPresets;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.nio.Buffer;
import java.util.UUID;

public class TemplateClickableInventoryCommand {
    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("testCommand")
                .then(Commands.argument("arg1", IntegerArgumentType.integer(0))
                        .executes(TemplateClickableInventoryCommand::mainLogic)
                ).build();
    }

    private static int mainLogic(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getSender();
        int arg1 = ctx.getArgument("arg1", Integer.class);
        if (arg1 == 0){
            ClickableInventory inv = new ClickableInventory(PaperPresets.getInstance(), InventorySize.ONE)
                    .setItem(new ItemPos(0), Material.GRASS_BLOCK, null)
                    .setItem(new ItemPos(1), Material.DIRT, (event, inv1) -> event.getWhoClicked().sendMessage("Dirt Clicked!!!"))
                    .build(player);
        }else if (arg1 == 1){
            ClickableInventory inv = new ClickableInventory(PaperPresets.getInstance(), InventorySize.ONE, Component.text("test2"))
                    .setItem(new ItemPos(0), Material.GRASS_BLOCK, (event, inv1) -> {
                        new ClickableInventory(PaperPresets.getInstance(), InventorySize.TWO, Component.text("test2.2"))
                                .setItem(new ItemPos(0), Material.COBBLESTONE, null)
                                .setItem(new ItemPos(1), Material.STONE, (event2, inv2) -> event.getWhoClicked().sendMessage("Stone Clicked!!!")).build(player, inv1);
                    })
                    .setItem(new ItemPos(1), Material.DIRT, (event, inv1) -> event.getWhoClicked().sendMessage("Dirt Clicked!!!"))
                    .build(player);
        }else if (arg1 == 2){
            ClickableInventory inv = new ClickableInventory(PaperPresets.getInstance(), InventorySize.ONE, Component.text("test3"))
                    .setItem(new ItemPos(0), Material.GRASS_BLOCK, (event, inv1) -> {
                        new ClickableInventory(PaperPresets.getInstance(), InventorySize.TWO, Component.text("test3.2"))
                                .setItem(new ItemPos(0), Material.COBBLESTONE, (event2, inv2) -> {
                                    new ClickableInventory(PaperPresets.getInstance(), InventorySize.THREE, Component.text("test3.3"))
                                            .setItem(new ItemPos(0), Material.OAK_LOG, null)
                                            .setItem(new ItemPos(1), Material.OAK_PLANKS, (event3, inv3) -> event3.getWhoClicked().sendMessage("Stone Clicked!!!"))
                                            .build(player, inv2);
                                })
                                .setItem(new ItemPos(1), Material.STONE, (event2, inv2) -> event.getWhoClicked().sendMessage("Stone Clicked!!!"))
                                .build(player, inv1);
                    })
                    .setItem(new ItemPos(1), Material.DIRT, (event, inv1) -> event.getWhoClicked().sendMessage("Dirt Clicked!!!"))
                    .build(player);
        }else if (arg1 == 3){
            ChangeItemVariant variantOn = new ChangeItemVariant(Material.LIME_WOOL, "on");
            ChangeItemVariant variantOff = new ChangeItemVariant(Material.RED_WOOL, "off");

            ChangeItem changeItem = new ChangeItemBuilder(Material.BLACK_WOOL)
                    .setName(Component.text("test"))
                    .addVariants(variantOn, variantOff)
                    .build();

            changeItem.setActive("off");

            ClickableInventory inv = new ClickableInventory(PaperPresets.getInstance(), InventorySize.ONE, Component.text("test3.1"))
                    .setItem(new ItemPos(0), changeItem, (event, inv1) -> {
                        ClickableInventory.updateChangeItem(event, item -> {
                            ChangeItemVariant selected = item.getSelected();
                            if (selected != null)
                                return item.getSelected().getVariantID().equals("on") ? "off" : "on";
                            return "off";
                        });
                    })
                    .setItem(new ItemPos(1), Material.DIRT, (event, inv1) -> event.getWhoClicked().sendMessage("Dirt Clicked!!!"))
                    .build(player);
        }else if (arg1 == 4){
            ChangeItemVariant variantOn = new ChangeItemVariant(Material.LIME_WOOL, "on");
            ChangeItemVariant variantOff = new ChangeItemVariant(Material.RED_WOOL, "off");

            ChangeItem changeItem = new ChangeItemBuilder(Material.BLACK_WOOL)
                    .setName(Component.text("test"))
                    .addVariants(variantOn, variantOff)
                    .build();

            changeItem.setActive("off");

            ClickedAction action = (event, inv) -> ClickableInventory.updateChangeItem(event, item -> {
                ChangeItemVariant selected = item.getSelected();
                if (selected != null){
                    return item.getSelected().getVariantID().equals("on") ? "off" : "on";
                    }
                return "off";
            });

            ClickableInventory inv = new ClickableInventory(PaperPresets.getInstance(), InventorySize.ONE, Component.text("test3.1"))
                    .setItem(new ItemPos(0), changeItem, action)
                    .setItem(new ItemPos(1), Material.DIRT, (event, inv1) -> event.getWhoClicked().sendMessage("Dirt Clicked!!!"))
                    .build(player);

            ClickableInventory inv2 = new ClickableInventory(PaperPresets.getInstance(), InventorySize.ONE, Component.text("test3.2"))
                    .setItem(new ItemPos(0), changeItem, action)
                    .setItem(new ItemPos(1), Material.DIRT, (event, inv1) -> event.getWhoClicked().sendMessage("Dirt Clicked!!!"))
                    .build(Bukkit.getPlayer("tsgs25"));
        }else if (arg1 == 5){
            ClickableInventory inv = new ClickableInventory(PaperPresets.getInstance(), InventorySize.THREE, Component.text("Security System"))
                    .setItem(new ItemPos(2, 1), PaperPresets.getSecurityDisableItem(), PaperPresets.getSecurityDisableAction())
                    .setItem(new ItemPos(3, 1), PaperPresets.getSecurityResetItem(), PaperPresets.getSecurityResetAction())
                    .setItem(new ItemPos(6, 1), PaperPresets.getSecurityActivateItem(), PaperPresets.getSecurityActivateAction())
                    .build(player);
        }else if (arg1 == 6){
            ColorItem colorItem = new ColorItem(player);
            ColorItem colorItem2 = new ColorItem(player);

            ClickableInventory inv = new ClickableInventory(PaperPresets.getInstance(), InventorySize.THREE, Component.text("Security System"))
                    .setItem(new ItemPos(0, 0), colorItem.getChangeItem(), null)
                    .setItem(new ItemPos(1, 0), colorItem.getChangeItem(), null)
                    .setItem(new ItemPos(2, 0), colorItem.getChangeItem(), null)
                    .setItem(new ItemPos(3, 0), colorItem2.getChangeItem(), null)
                    .setItem(new ItemPos(2, 1), Material.WHITE_STAINED_GLASS_PANE, (event, inv1) -> {
                        ColorItem.setVariant((Player) event.getWhoClicked(), ColorItem.Color.WHITE);
                    })
                    .setItem(new ItemPos(6, 1), Material.BLACK_STAINED_GLASS_PANE, (event, inv1) -> {
                        ColorItem.setVariant((Player) event.getWhoClicked(), ColorItem.Color.BLACK);
                    })
                    .build(player);
        }else if (arg1 == 7){
            ColorItem.colorPicker(player, null, null);
        }else if (arg1 == 8){
            ColorItem.colorPicker(player, Component.text("Custom Name"), null);
        }else if (arg1 == 9){
            ClickableInventory inv = new ClickableInventory(PaperPresets.getInstance(), InventorySize.ONE, Component.text("test2"))
                    .setItem(new ItemPos(0), Material.GRASS_BLOCK, (event, inv1) -> {
                        ColorItem.colorPicker(player, Component.text("Custom Name"), inv1);
                    })
                    .setItem(new ItemPos(1), Material.DIRT, (event, inv1) -> event.getWhoClicked().sendMessage("Dirt Clicked!!!"))
                    .build(player);
        }else if (arg1 == 10){
            player.sendMessage(Component.translatable("some.translation.key", Component.text("The Argument")));
        }
        return Command.SINGLE_SUCCESS;
    }
}