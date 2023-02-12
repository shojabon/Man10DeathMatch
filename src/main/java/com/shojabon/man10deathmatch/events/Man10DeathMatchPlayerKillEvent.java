package com.shojabon.man10deathmatch.events;

import com.shojabon.man10deathmatch.Man10DeathMatch;
import com.shojabon.man10deathmatch.data_class.DeathMatchGame;
import com.shojabon.man10deathmatch.data_class.DeathMatchPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class Man10DeathMatchPlayerKillEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private DeathMatchPlayer killer;
    private DeathMatchPlayer killed;
    private PlayerDeathEvent event;
    private DeathMatchGame game;

    public Man10DeathMatchPlayerKillEvent(DeathMatchGame game, DeathMatchPlayer killed, DeathMatchPlayer killer, PlayerDeathEvent event){
        this.killed = killed;
        this.killer = killer;
        this.event = event;
        this.game = game;
    }

    public DeathMatchPlayer getKilled() {
        return killed;
    }

    public DeathMatchPlayer getKiller() {
        return killer;
    }

    public PlayerDeathEvent getEvent() {
        return event;
    }

    public DeathMatchGame getGame() {
        return game;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}
