package de.tsgscraft.paperPresets.ClickableInventory;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.tsgscraft.paperPresets.PaperPresets;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
        }
        return Command.SINGLE_SUCCESS;
    }
}