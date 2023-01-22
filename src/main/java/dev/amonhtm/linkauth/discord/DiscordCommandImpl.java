package dev.amonhtm.linkauth.discord;

import dev.amonhtm.linkauth.LinkAuth;
import dev.amonhtm.linkauth.commands.LinkCommand;
import dev.amonhtm.linkauth.element.CommandElement;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DiscordCommandImpl extends ListenerAdapter {

    private List<CommandElement> commands;

    public void load(LinkAuth linkAuth) {
        this.commands = new ArrayList<>();

        commands.add(new LinkCommand(linkAuth));

        CommandListUpdateAction action = linkAuth.jda().getGuildById(1062724294154670102L).getJDA().updateCommands();

        for(CommandElement element : commands) {
            action = action.addCommands(element.data());
        }

        action.queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for(CommandElement element : commands) {
            if(!element.data().getName().equalsIgnoreCase(event.getInteraction().getName()))continue;

            element.respond(event);
        }
    }
}
