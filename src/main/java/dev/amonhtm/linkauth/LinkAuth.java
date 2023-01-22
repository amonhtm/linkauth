package dev.amonhtm.linkauth;

import dev.amonhtm.linkauth.commands.LinkCommand;
import dev.amonhtm.linkauth.controller.LinkController;
import dev.amonhtm.linkauth.controller.RequestController;
import dev.amonhtm.linkauth.database.Database;
import dev.amonhtm.linkauth.discord.DiscordCommandImpl;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.IOException;
import java.sql.SQLException;

public class LinkAuth extends Plugin {

    private final String discordBotToken = "MTA2MjcyNDA5ODI4OTA1NzgwMw.GYj6Tg.r3m6aNUpE4a8fBfKD33vqUqer1ei3TknLqYsZw";
    private JDA jda;

    private Database database;

    private LinkController linkController;
    private RequestController requestController;

    @Override
    public void onEnable() {
        initDatabase();
        initDiscord();

        this.linkController = new LinkController(database.hikariDataSource());
        this.requestController = new RequestController();

        PluginManager pluginManager = getProxy().getPluginManager();

        pluginManager.registerCommand(this, new LinkCommand(this));
    }

    public void initDatabase() {
        this.database = new Database();

        database.connect();
    }

    public void initDiscord() {
        JDA bot;
        JDABuilder builder = JDABuilder.createDefault(discordBotToken);

        DiscordCommandImpl discordCommandImpl = new DiscordCommandImpl();

        builder.setActivity(Activity.playing("baaasty.de"));

        builder.addEventListeners(discordCommandImpl);

        try {
            bot = builder.build();
            bot.awaitReady();
            this.jda = bot;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        discordCommandImpl.load(this);
    }

    public EmbedBuilder embed(String title, String description) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0xf5b041);
        builder.setAuthor("Baaasty LinkAuth", "https://baaasty.de", "https://cdn.discordapp.com/avatars/800087492794515476/538f86c17f2cb6cca8623d6599687da8.webp?size=80");
        builder.setDescription(title + "\n" + ">>> " + description);

        return builder;
    }

    public JDA jda() {
        return jda;
    }

    public LinkController linkController() {
        return linkController;
    }
    public RequestController requestController() {
        return requestController;
    }
}
