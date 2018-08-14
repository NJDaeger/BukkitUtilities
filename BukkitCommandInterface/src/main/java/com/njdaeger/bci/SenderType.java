package com.njdaeger.bci;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public enum SenderType {
    
    CONSOLE,
    PLAYER,
    ENTITY,
    BLOCK,
    UNKNOWN;
    
    public static SenderType of(CommandSender sender) {
        if (sender instanceof Player) return PLAYER;
        if (sender instanceof Entity) return ENTITY;
        if (sender instanceof BlockCommandSender) return BLOCK;
        if (sender instanceof ConsoleCommandSender) return CONSOLE;
        else return UNKNOWN;
    }
    
}
