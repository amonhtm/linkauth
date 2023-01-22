package dev.amonhtm.linkauth.element;

import dev.amonhtm.linkauth.LinkAuth;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.md_5.bungee.api.plugin.Command;

public abstract class CommandElement extends Command {

    protected final LinkAuth linkAuth;

    public CommandElement(LinkAuth linkAuth, String name) {
        super(name);
        this.linkAuth = linkAuth;
    }

    public abstract CommandData data();
    public abstract void respond(SlashCommandInteractionEvent event);
}
