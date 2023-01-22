package dev.amonhtm.linkauth.controller;

import de.chojo.sadu.base.QueryFactory;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

public class LinkController extends QueryFactory {

    public LinkController(DataSource dataSource) {
        super(dataSource);
    }

    public Optional<Long> linked(UUID uuid) {
        return builder(Long.class)
                .query("SELECT discordId FROM link WHERE uuid = ?")
                .parameter(stmt -> stmt.setUuidAsBytes(uuid))
                .readRow(row -> row.getLong("discordId"))
                .firstSync();
    }
    public Optional<UUID> linked(long discordId) {
        return builder(UUID.class)
                .query("SELECT uuid FROM link WHERE discordId = ?")
                .parameter(stmt -> stmt.setLong(discordId))
                .readRow(row -> row.getUuidFromBytes("uuid"))
                .firstSync();
    }

    public void link(UUID uuid, long discordId) {
        builder()
                .query("INSERT INTO link(uuid, discordId) VALUES (?,?)")
                .parameter(stmt -> stmt.setUuidAsBytes(uuid).setLong(discordId))
                .insert()
                .sendSync();
    }

    public void unlink(UUID uuid) {
        builder()
                .query("DELETE FROM link WHERE uuid = ?")
                .parameter(stmt -> stmt.setUuidAsBytes(uuid))
                .insert()
                .sendSync();
    }
    public void unlink(long discordId) {
        builder()
                .query("DELETE FROM link WHERE discordId = ?")
                .parameter(stmt -> stmt.setLong(discordId))
                .insert()
                .sendSync();
    }
}
