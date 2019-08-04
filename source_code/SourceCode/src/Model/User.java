package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class User {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private int     userID, worksiteID;
    private String  username, fullName, firstName, lastName;
    private Worksite worksite;

    private static final String salt = "jvhoq)P3Nkf|IvNA%F}>*Lav!Q*d|2_sv(+6V}juUEU]yj@;%&yM{YWxYZ/X,-_52}<3@92]t#L|sI22|VwD9fiFjl&xJz$P%GvH";



      ////////////////////
     //Database Queries//
    ////////////////////
    public static  ResultSet selectAllUsers() throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT user_table.user_name, user_table.user_id, " +
                        "user_table.first_name, user_table.last_name, worksite.worksite_name, " +
                        "user_table.worksite_id " +
                        "FROM `user_table` " +
                        "INNER JOIN worksite ON worksite.worksite_id=user_table.worksite_id"
        ).executeQuery();

    }
    public static User selectUserById(int userID) throws Exception{
        ResultSet rs = DatabaseConfig.getConnection().prepareStatement(
                "SELECT user_table.user_name, user_table.user_id, " +
                        "user_table.first_name, user_table.last_name, worksite.worksite_name, " +
                        "user_table.worksite_id " +
                        "FROM `user_table` " +
                        "INNER JOIN worksite ON worksite.worksite_id=user_table.worksite_id " +
                        "WHERE user_table.user_id = " + userID
        ).executeQuery();
        rs.next();
        return new User(
                //int userID, int worksiteID, String username, String firstName, String lastName
                rs.getInt("user_id"),
                rs.getInt("worksite_id"),
                rs.getString("user_name"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                Worksite.generateWorksiteByID(rs.getInt("worksite_id"))
        );
    }

    //Method to compute the salted hashed password and returns the result as string
    // It accepts plain text password and reads the randomly  generated salt from a file

    public static String generateSaltedHashedPassword(String passwordToHash)  {
        String generatedHashedPassword = null;
        BufferedReader readSalt = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            //Plain text password: passwordToHash
            messageDigest.update(passwordToHash.getBytes(StandardCharsets.UTF_8));
            messageDigest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = messageDigest.digest();
            StringBuilder sBuilder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedHashedPassword = sBuilder.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        finally {
            if(readSalt!= null) {
                try {
                    readSalt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return generatedHashedPassword;
    }

    public static boolean verifyLogin(String password, String username)
    {
        boolean isRegistered=false;
        String query = "select * from login_table where user_name='"+username+"'";

        try {

            Connection conn = DatabaseConfig.getConnection();
            Statement stmts = (Statement) conn.createStatement();
            ResultSet rs = stmts.executeQuery(query);
            if (!rs.next()) {
                throw new SecurityException("username not registered");
            } else {
                String storedHashedPassword = rs.getString("password");
                String generatedHashedPassword = generateSaltedHashedPassword(password);
                if (storedHashedPassword.equals(generatedHashedPassword)) {
                    isRegistered = true;
                } else {
                    System.out.println("Login failed");
                }

            }
        } catch (Exception e) {
            System.out.println("Status: operation failed due to " + e);

        }
        return isRegistered;

    }

    public static void insertNewUser(User userToInsert) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement(
                "INSERT INTO `user_table` (user_name, worksite_id, first_name, last_name) " +
                        "VALUES (?,?,?,?)"
        );

        statement.setString(1, userToInsert.username);
        statement.setInt(2, userToInsert.worksiteID);
        statement.setString(3, userToInsert.fullName.split(" ")[0]);
        statement.setString(4, userToInsert.fullName.split(" ")[1]);

        statement.execute();
    }

    public static void updateUser(User userToUpdate) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement(
          "UPDATE user_table SET " +
                  "user_name = ?, " +
                  "worksite_id = ?, "+
                  "first_name = ?, "+
                  "last_name = ? "+
                  "WHERE user_id = " + userToUpdate.getUserID()
        );

        statement.setString(1, userToUpdate.getUsername());
        statement.setInt(2, userToUpdate.getWorksiteID());
        statement.setString(3, userToUpdate.getFirstName());
        statement.setString(4, userToUpdate.getLastName());

        statement.executeUpdate();
    }


      ///////////////////
     //List Generators//
    ///////////////////
    public static ObservableList<User> generateUserList() throws Exception{
        ResultSet rs = User.selectAllUsers();
        ObservableList<User> list = FXCollections.observableArrayList();
        while(rs.next()){
            list.add(new User(
                    rs.getInt("user_id"),
                    rs.getInt("worksite_id"),
                    rs.getString("user_name"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    Worksite.generateWorksiteByID(rs.getInt("worksite_id"))
            ));
        }

        return list;
    }


      ///////////////////
     //Getters/Setters//
    ///////////////////
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getWorksiteID() {
        return worksiteID;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String userName) {
        this.username = userName;
    }
    public String getFullName() {
        return fullName;
    }
    public String getWorksite() {
        return worksite.getWorksiteName();
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    ////////////////
     //Constructors//
    ////////////////
    public User() {
        userID =-1;
        worksiteID=-1;
        username=null;
        fullName=null;
        firstName=null;
        lastName=null;
        worksite=null;
    }

    public User(int worksiteID, String username, String firstName, String lastName) {
        this.worksiteID = worksiteID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
    }

    public User(String username, String firstName, String lastName, int userID) {
        this.userID = userID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
    }


    public User(int userID, int worksiteID, String username, String firstName, String lastName, Worksite worksite) {
        this.userID = userID;
        this.worksiteID = worksiteID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.worksite = worksite;
    }


      /////////////
     //Overrides//
    /////////////
    @Override
    public String toString() {
        return fullName;
    }



}
