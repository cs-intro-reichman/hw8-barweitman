import java.util.HashMap;
/** Represents a social network. The network has users, who follow other users.
 *  Each user is an instance of the User class. */
public class Network {
    // Fields
    private User[] users;   // the users in this network (an array of User objects)
    private int userCount; // actual number of users in this network

    /** Creates a network with a given maximum number of users. */
    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    /** Creates a network with some users. The only purpose of this constructor is 
     *  to allow testing the toString and getUser methods, before implementing other methods. */
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

    /** Finds in this network, and returns, the user that has the given name.
     *  If there is no such user, returns null.
     *  Notice that the method receives a String, and returns a User object. */
    public User getUser(String name) {
        if (name == null) return null;
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equalsIgnoreCase(name)) {
                return users[i];
            }
        }
        return null;
    }

    /** Adds a new user with the given name to this network.
    *  If this network is full, does nothing and returns false;
    *  If the given name is already a user in this network, does nothing and returns false;
    *  Otherwise, creates a new user with the given name, adds the user to this network, and returns true. */
    public boolean addUser(String name) {
        if (name == null || getUser(name) != null || userCount >= users.length) {
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
        if (user1 == null || user2 == null || name1.equalsIgnoreCase(name2)) {
            return false;
        }
        return user1.addFollowee(name2);
    }

    /** For the user with the given name, recommends another user to follow. The recommended user is
     *  the user that has the maximal mutual number of followees as the user with the given name. */
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

    /** Computes and returns the name of the most popular user in this network: 
     *  The user who appears the most in the follow lists of all the users. */
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

    /** Returns a textual description of all the users in this network, and who they follow. */
    /** Returns a textual description of all the users in this network, and who they follow. */
    public String toString() {
        String result = "Network:";
        for (int i = 0; i < userCount; i++) {
            result += "\n" + users[i].getName() + " ->";
            String[] followees = users[i].getfFollows();
            int followeeCount = users[i].getfCount();
            if (followeeCount > 0) {
                for (int j = 0; j < followeeCount; j++) {
                    result += " " + followees[j];
                }
                result += " "; 
            } else {
                result += " ";
            }
        }
        return result.trim(); 
    }
}
