package dev.amonhtm.linkauth.commands;

import dev.amonhtm.linkauth.LinkAuth;
import dev.amonhtm.linkauth.element.CommandElement;
import dev.amonhtm.linkauth.element.RequestElement;
import dev.amonhtm.linkauth.util.Generator;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LinkCommand extends CommandElement {

    public LinkCommand(LinkAuth linkAuth) {
        super(linkAuth, "link");
    }

    @Override
    public CommandData data() {
        return Commands.slash("link", "Link actions for our discord account")
                .addOption(OptionType.STRING, "token", "The token u got from minecraft");
    }

    @Override
    public void respond(SlashCommandInteractionEvent event) {
        User user = event.getUser();

        RequestElement element = RequestElement.of(user.getIdLong());

        if(linkAuth.linkController().linked(user.getIdLong()).isPresent()) {
            //IF USER IS ALREADY VERIFIED

            event.replyEmbeds(linkAuth.embed(":dizzy: — **Bereits verbunden**", """
                    Hey!
                    
                    Du bist **bereits** mit einem Minecraft-Account **verbunden**.
                    
                    Um die **verbindung aufzuheben**, kannst du einfach **/unlink** benutzen.
                    Viel Spaß noch :^)
                    """).build())
                    .setEphemeral(true).addContent(user.getAsMention()).queue();
            return;
        }

        if(event.getOption("token") == null) {
            //CREATE AND SEND A TOKEN

            String token = linkAuth.requestController().optionalToken(element).orElse(Generator.token(10));

            linkAuth.requestController().insert(token, element);

            event.replyEmbeds(linkAuth.embed(":label: — **Token generiert**", """
                    Hey!
                    
                    Es wurde ein **persönlicher Token** erstellt.
                    
                    Dein **Token** lautet || %s ||.
                    Der **Token** läuft <t:%s:R> ab.
                    
                    Bitte gib **niemandem** dein Token weiter. Der **Token** darf nur für **deinen Account** benutzen werden.
                    Viel Spaß damit :^)
                    """.formatted(token, TimeUnit.MILLISECONDS.toSeconds(linkAuth.requestController().expire(token).orElse(System.currentTimeMillis())))
                    ).build())
                    .setEphemeral(true).addContent(user.getAsMention()).queue();

        }else {
            //LINK THE ACCOUNT

            String token = Objects.requireNonNull(event.getOption("token")).getAsString();

            if(linkAuth.requestController().optionalRequest(token).isEmpty()) {
                event.replyEmbeds(linkAuth.embed(":label: — **Token generiert**", """
                    Hey!
                    
                    Es wurde leider **kein Anfrage** mit dem Token gefunden.
                    Du kannst dir einen **neuen Token** mit **/link** holen.
                    
                    Viel Spaß noch :^)
                    """).build())
                        .setEphemeral(true).addContent(user.getAsMention()).queue();
                return;
            }

            element = linkAuth.requestController().optionalRequest(token).get();

            if(element.optionalUuid().isEmpty()) {
                event.replyEmbeds(linkAuth.embed(":label: — **Token generiert**", """
                    Hey!
                    
                    Dein **Token** kann **nur** auf dem **Minecraft Server** verwendet werden.
                    
                    Viel Spaß noch :^)
                    """).build())
                        .setEphemeral(true).addContent(user.getAsMention()).queue();
                return;
            }

            UUID uuid = element.optionalUuid().get();
            String head = "http://cravatar.eu/helmavatar/" + uuid.toString() + "/160";

            event.replyEmbeds(linkAuth.embed(":label: — **Token generiert**", """
                Hey!
                    
                Dein **Discord** wurde mit einem **Minecraft Account** verbunden!
                    
                Viel Spaß noch :^)
                """).setThumbnail(head).build())
                    .setEphemeral(true).addContent(user.getAsMention()).queue();

            linkAuth.linkController().link(uuid, user.getIdLong());
            linkAuth.requestController().removeAll(uuid, user.getIdLong());
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer player))return;

        RequestElement element = RequestElement.of(player.getUniqueId());

        if(linkAuth.linkController().linked(player.getUniqueId()).isPresent()) {
            player.sendMessage(" §8\u25cf §eLink §8〣 §7Du bist bereits §everifiziert§7. Benutze §e/unlink§7, um die §eVerbindung aufzuheben§7.");
            return;
        }

        if(args.length == 0) {
            String token = linkAuth.requestController().optionalToken(element).orElse(Generator.token(10));

            linkAuth.requestController().insert(token, element);

            player.sendMessage(" §8\u25cf §eLink §8〣 §7Es wurde ein §eToken generiert§7.");
            player.sendMessage(copyTokenComponent(token));
        }else {
            String token = args[0];

            if(linkAuth.requestController().optionalRequest(token).isEmpty()) {
                player.sendMessage(" §8\u25cf §eLink §8〣 §7Es wurde §ekein Token gefunden§7.");
                return;
            }

            element = linkAuth.requestController().optionalRequest(token).get();

            if(element.optionalDiscordId().isEmpty()) {
                player.sendMessage(" §8\u25cf §eLink §8〣 §7Dieses §eToken §7kann nur auf dem §eDiscord §7verwendet werden.");
                return;
            }

            long discordId = element.optionalDiscordId().get();
            String name = linkAuth.jda().getUserById(discordId).getName();

            player.sendMessage(" §8\u25cf §eLink §8〣 §7Dein §eAccount §7wurde mit §e" + name + " §7verbunden.");

            linkAuth.linkController().link(player.getUniqueId(), discordId);
            linkAuth.requestController().removeAll(player.getUniqueId(), discordId);
        }
    }

    public TextComponent copyTokenComponent(String token) {
        TextComponent tokenComponent = new TextComponent(token);
        tokenComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, token));
        tokenComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7§oKlicke zum Kopieren")));

        TextComponent component = new TextComponent(" §8\u25cf §eLink §8〣 §7Token§8: §e");
        component.addExtra(tokenComponent);

        return component;
    }
}
