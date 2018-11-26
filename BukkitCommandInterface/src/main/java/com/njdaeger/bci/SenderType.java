package com.njdaeger.bci;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Represents a specific sender type
 */
public enum SenderType {

    /**
     * Represents the console command sender type
     */
    CONSOLE,
    /**
     * Represents the player command sender type
     */
    PLAYER,
    /**
     * Represents the entity command sender type
     */
    ENTITY,
    /**
     * Represents the block command sender type
     */
    BLOCK,
    /**
     * Represents an unknown command sender type
     */
    UNKNOWN;
    
    public static SenderType of(CommandSender sender) {
        if (sender instanceof Player) return PLAYER;
        if (sender instanceof Entity) return ENTITY;
        if (sender instanceof BlockCommandSender) return BLOCK;
        if (sender instanceof ConsoleCommandSender) return CONSOLE;
        else return UNKNOWN;
    }
    
}
