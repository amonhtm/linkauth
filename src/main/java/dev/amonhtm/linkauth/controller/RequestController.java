package dev.amonhtm.linkauth.controller;

import dev.amonhtm.linkauth.element.RequestElement;

import java.util.*;

public class RequestController {

    private Map<RequestElement, String> requestMap;
    private Map<String, Long> expireMap;

    public RequestController() {
        this.requestMap = new HashMap<>();
        this.expireMap = new HashMap<>();
    }

    public void insert(String token, RequestElement request) {
        long expire = System.currentTimeMillis() + 600000;

        requestMap.put(request, token);
        expireMap.put(token, expire);
    }

    public Optional<String> optionalToken(RequestElement element) {
        if(requestMap.containsKey(element)) {
            return Optional.of(requestMap.get(element));
        }

        return Optional.empty();
    }
    public Optional<RequestElement> optionalRequest(String token) {
        for(RequestElement request : requestMap.keySet()) {
            if(Objects.equals(requestMap.get(request), token)) {
                return Optional.of(request);
            }
        }

        return Optional.empty();
    }

    public Optional<Long> expire(String token) {
        return Optional.of(expireMap.getOrDefault(token, System.currentTimeMillis()));
    }

    public void removeAll(UUID uuid, long discordId) {
        RequestElement optionalUuid = RequestElement.of(uuid);

        if(optionalToken(optionalUuid).isPresent()) {
            String token = optionalToken(optionalUuid).get();
            requestMap.remove(optionalRequest(token).get());
        }

        RequestElement optionalDiscordId = RequestElement.of(discordId);

        if(optionalToken(optionalDiscordId).isPresent()) {
            String token = optionalToken(optionalDiscordId).get();
            requestMap.remove(optionalRequest(token).get());
        }
    }
}
