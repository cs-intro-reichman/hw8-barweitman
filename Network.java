/** Represents a social network. */
public class Network {
    private User[] users;  // Array of users in this network
    private int userCount; // Number of users in the network

    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        if (gettingStarted) {
            this.addUser("Foo");
            this.addUser("Bar");
            this.addUser("Baz");
        }
    }

    public int getUserCount() {
        return this.userCount;
    }

    /** Finds a user by name. */
    public User getUser(String name) {
        if (name == null) return null;
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equalsIgnoreCase(name)) {
                return users[i];
            }
        }
        return null;
    }

    /** Adds a user to the network. */
    public boolean addUser(String name) {
        if (name == null || getUser(name) != null || userCount >= users.length) {
            return false;
        }
        users[userCount++] = new User(name);
        return true;
    }

    /** Adds a followee relationship between two users. */
    public boolean addFollowee(String name1, String name2) {
        User user1 = getUser(name1);
        User user2 = getUser(name2);
        if (user1 == null || user2 == null || name1.equalsIgnoreCase(name2)) {
            return false;
        }
        return user1.addFollowee(name2);
    }

    /** Recommends a user to follow based on mutual followees. */
    public String recommendWhoToFollow(String name) {
        User user = getUser(name);
        if (user == null) return null;

        String recommendation = null;
        int maxMutual = 0;

        for (int i = 0; i < userCount; i++) {
            User candidate = users[i];
            if (!user.follows(candidate.getName()) && !user.getName().equalsIgnoreCase(candidate.getName())) {
                int mutual = user.countMutual(candidate);
                if (mutual > maxMutual) {
                    maxMutual = mutual;
                    recommendation = candidate.getName();
                }
            }
        }
        return recommendation;
    }

    /** Finds the most popular user in the network. */
    public String mostPopularUser() {
        String mostPopular = null;
        int maxFollowers = 0;

        for (int i = 0; i < userCount; i++) {
            String candidateName = users[i].getName();
            int followers = followeeCount(candidateName);
            if (followers > maxFollowers) {
                maxFollowers = followers;
                mostPopular = candidateName;
            }
        }
        return mostPopular;
    }

    /** Counts the number of followers of a user. */
    private int followeeCount(String name) {
        int count = 0;
        for (int i = 0; i < userCount; i++) {
            if (users[i].follows(name)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Network:\n");
        for (int i = 0; i < userCount; i++) {
            sb.append(users[i].toString()).append("\n");
        }
        return sb.toString().trim();
    }
}
