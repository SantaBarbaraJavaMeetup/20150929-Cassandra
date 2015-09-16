package paulfife.javameetup.cassandra.model;

/**
 * POJO for a User
 */
public class User {
    private String userId;
    private String fullName;
    private int birthMonth;
    private int birthDay;
    private int birthYear;

    public User() {
    }

    public User(String userId, String fullName, int birthYear, int birthMonth, int birthDay) {
        this.userId = userId;
        this.fullName = fullName;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
}
