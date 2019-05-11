package mpeciakk.claimchunk.models;

import mpeciakk.claimchunk.config.ClaimManager;

import java.util.*;

public class ClaimData {

    private int x;
    private int z;
    private int dimension;
    private Member owner;
    private List<Member> members = new ArrayList<>();

    public ClaimData() { }

    public void setOwner(Member newOwner) {
        this.owner = newOwner;

        ClaimManager.save();
    }

    public Member getOwner() {
        return owner;
    }

    public void addMember(Member member) {
        if (members.contains(member)) return;

        members.add(member);

        ClaimManager.save();
    }

    public void removeMember(UUID uuid) {
        for (Member member : members) {
            if (member.getUuid().toString().equals(uuid.toString())) {
                members.remove(member);
            }
        }

        ClaimManager.save();
    }

    public boolean isOwned() {
        return owner != null;
    }

    public List<Member> getMembers() {
        return this.members;
    }

    public boolean isOwner(UUID uuid) {
        return owner.getUuid().toString().equals(uuid.toString());
    }

    public boolean isMember(UUID uuid) {
        for (Member member : members) {
            if (member.getUuid().toString().equals(uuid.toString())) {
                return true;
            }
        }

        return false;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getDimension() {
        return dimension;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
