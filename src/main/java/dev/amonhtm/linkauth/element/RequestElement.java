package dev.amonhtm.linkauth.element;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RequestElement {

    private static Map<UUID, RequestElement> uuidRequestElementMap = new HashMap<>();
    private static Map<Long, RequestElement> longRequestElementMap = new HashMap<>();

    public static RequestElement of(UUID uuid) {
        return uuidRequestElementMap.getOrDefault(uuid, new RequestElement(uuid));
    }
    public static RequestElement of(long discordId) {
        return longRequestElementMap.getOrDefault(discordId, new RequestElement(discordId));
    }

    private final Optional<UUID> optionalUuid;
    private final Optional<Long> optionalDiscordId;

    public RequestElement(UUID uuid) {
        this.optionalUuid = Optional.of(uuid);
        this.optionalDiscordId = Optional.empty();

        uuidRequestElementMap.put(uuid, this);
    }

    public RequestElement(long discordId) {
        this.optionalUuid = Optional.empty();
        this.optionalDiscordId = Optional.of(discordId);

        longRequestElementMap.put(discordId, this);
    }

    public Optional<UUID> optionalUuid() {
        return optionalUuid;
    }
    public Optional<Long> optionalDiscordId() {
        return optionalDiscordId;
    }
}
