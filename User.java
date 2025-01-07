/** Represents a user in a social network. */
public class User {
    static int maxfCount = 10; // Maximum number of users a user can follow

    private String name;       // Name of this user
    private String[] follows;  // Array of usernames that this user follows
    private int fCount;        // Actual number of followees (must be <= maxfCount)

    /** Creates a user with an empty list of followees. */
    public User(String name) {
        this.name = name;
        this.follows = new String[maxfCount];
        this.fCount = 0;
    }

    /** Creates a user with some followees for testing purposes. */
    public User(String name, boolean gettingStarted) {
        this(name);
        if (gettingStarted) {
            this.addFollowee("Foo");
            this.addFollowee("Bar");
            this.addFollowee("Baz");
        }
    }

    public String getName() {
        return name;
    }

    public int getfCount() {
        return fCount;
    }

    public String[] getfFollows() {
        return follows;
    }

    /** Checks if this user follows a given username. */
    public boolean follows(String username) {
        if (username == null) return false;
        for (int i = 0; i < fCount; i++) {
            if (follows[i].equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /** Adds a new followee. */
    public boolean addFollowee(String username) {
        if (username == null || follows(username) || fCount >= maxfCount) {
            return false;
        }
        follows[fCount++] = username;
        return true;
    }

    /** Removes a followee. */
    public boolean removeFollowee(String username) {
        for (int i = 0; i < fCount; i++) {
            if (follows[i].equalsIgnoreCase(username)) {
                follows[i] = follows[--fCount]; // Replace with the last followee
                follows[fCount] = null;        // Nullify the last slot
                return true;
            }
        }
        return false;
    }

    /** Counts mutual followees with another user. */
    public int countMutual(User other) {
        if (other == null) return 0;

        int mutualCount = 0;
        for (int i = 0; i < fCount; i++) {
            if (other.follows(follows[i])) {
                mutualCount++;
            }
        }
        return mutualCount;
    }

    /** Checks if two users are mutual friends. */
    public boolean isFriendOf(User other) {
        return other != null && this.follows(other.getName()) && other.follows(this.getName());
    }

    @Override
    public String toString() {
        String result = name + " ->";
        for (int i = 0; i < fCount; i++) {
            result += " " + follows[i];
        }
        return result;
    }
}
