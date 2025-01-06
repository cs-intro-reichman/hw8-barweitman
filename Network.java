import java.util.HashMap;

/** Represents a social network. The network has users, who follow other uesrs.
 *  Each user is an instance of the User class. */
public class Network {

    // Fields
    private User[] users;  // the users in this network (an array of User objects)
    private int userCount; // actual number of users in this network

    /** Creates a network with a given maximum number of users. */
    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    /** Creates a network  with some users. The only purpose of this constructor is 
     *  to allow testing the toString and getUser methods, before implementing other methods. */
    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        users[0] = new User("Foo");
        users[1] = new User("Bar");
        users[2] = new User("Baz");
        userCount = 3;
    }

    public int getUserCount() {
        return this.userCount;
    }
    /** Finds in this network, and returns, the user that has the given name.
     *  If there is no such user, returns null.
     *  Notice that the method receives a String, and returns a User object. */
    public User getUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equals(name)) {
                return users[i];
            }
        }
        return null;
    }

    /** Adds a new user with the given name to this network.
    *  If ths network is full, does nothing and returns false;
    *  If the given name is already a user in this network, does nothing and returns false;
    *  Otherwise, creates a new user with the given name, adds the user to this network, and returns true. */
    public boolean addUser(String name) {
        if (userCount >= users.length || getUser(name) != null) {
            return false;
        }
        users[userCount++] = new User(name);
        return true;
    }

    /** Makes the user with name1 follow the user with name2. If successful, returns true.
     *  If any of the two names is not a user in this network,
     *  or if the "follows" addition failed for some reason, returns false. */
    public boolean addFollowee(String name1, String name2) {
        User user1 = getUser(name1);
        User user2 = getUser(name2);
        if (user1 == null || user2 == null) {
            return false;
        }
        return user1.addFollowee(name2);
    }
    
    /** For the user with the given name, recommends another user to follow. The recommended user is
     *  the user that has the maximal mutual number of followees as the user with the given name. */
    public String recommendWhoToFollow(String name) {
        User user = getUser(name);
        if (user == null) {
            return null;
        }

        HashMap<String, Integer> mutualFolloweeCount = new HashMap<>();
        for (String followee : user.getFollows()) {
            User followeeUser = getUser(followee);
            if (followeeUser != null) {
                for (String theirFollowee : followeeUser.getFollows()) {
                    if (!theirFollowee.equals(name) && !user.follows(theirFollowee)) {
                        mutualFolloweeCount.put(theirFollowee, mutualFolloweeCount.getOrDefault(theirFollowee, 0) + 1);
                    }
                }
            }
        }

        String recommendation = null;
        int maxMutuals = 0;
        for (String candidate : mutualFolloweeCount.keySet()) {
            if (mutualFolloweeCount.get(candidate) > maxMutuals) {
                maxMutuals = mutualFolloweeCount.get(candidate);
                recommendation = candidate;
            }
        }

        return recommendation;
    }


    /** Computes and returns the name of the most popular user in this network: 
     *  The user who appears the most in the follow lists of all the users. */
    public String mostPopularUser() {
        HashMap<String, Integer> popularity = new HashMap<>();

        for (int i = 0; i < userCount; i++) {
            for (String followee : users[i].getFollows()) {
                if (followee != null) {
                    popularity.put(followee, popularity.getOrDefault(followee, 0) + 1);
                }
            }
        }

        String mostPopular = null;
        int maxCount = 0;
        for (String user : popularity.keySet()) {
            if (popularity.get(user) > maxCount) {
                maxCount = popularity.get(user);
                mostPopular = user;
            }
        }

        return mostPopular;
    }

    /** Returns the number of times that the given name appears in the follows lists of all
     *  the users in this network. Note: A name can appear 0 or 1 times in each list. */
    private int followeeCount(String name) {
        int count = 0;
        for (int i = 0; i < userCount; i++) {
            if (users[i].follows(name)) {
                count++;
            }
        }
        return count;
    }

    // Returns a textual description of all the users in this network, and who they follow.
    public String toString() {
        StringBuilder sb = new StringBuilder("Network:\n");
        for (int i = 0; i < userCount; i++) {
            sb.append(users[i].toString()).append("\n");
        }
        return sb.toString();
    }

    private static class User {
        static int maxfCount = 10; // Maximum number of users that a user can follow
        private String name; // name of this user
        private String[] follows; // array of user names that this user follows
        private int fCount; // actual number of followees (must be <= maxfCount)

        /** Creates a user with an empty list of followees. */
        public User(String name) {
            this.name = name;
            this.follows = new String[maxfCount];
            this.fCount = 0;
        }

        /** Returns the name of this user. */
        public String getName() {
            return name;
        }

        /** Returns the array of followees for this user. */
        public String[] getFollows() {
            return follows;
        }

        /** Checks if this user already follows the given name. */
        public boolean follows(String name) {
            for (int i = 0; i < fCount; i++) {
                if (follows[i].equals(name)) {
                    return true;
                }
            }
            return false;
        }

        /** Adds a new followee for this user, if possible. */
        public boolean addFollowee(String name) {
            if (fCount < maxfCount && !follows(name)) {
                follows[fCount++] = name;
                return true;
            }
            return false;
        }

        /** Returns this user's name, and the names that they follow. */
        public String toString() {
            StringBuilder ans = new StringBuilder(name + " -> ");
            for (int i = 0; i < fCount; i++) {
                ans.append(follows[i]).append(" ");
            }
            return ans.toString().trim();
        }
    }
}
