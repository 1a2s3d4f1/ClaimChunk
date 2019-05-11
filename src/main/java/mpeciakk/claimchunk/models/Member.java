package mpeciakk.claimchunk.models;

import java.util.UUID;

public class Member {

    private final String name;
    private final UUID uuid;

    public Member(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }
}
