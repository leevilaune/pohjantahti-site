package dev.onesnzeroes.pt.service;

public class ClanNotFoundException extends RuntimeException {

    public ClanNotFoundException(String tag) {
        super("Clan " + tag + " is not tracked or has not been fetched yet");
    }
}
