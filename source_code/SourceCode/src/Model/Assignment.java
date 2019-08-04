package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Assignment {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private int     assignID, systemID;
    private Equipment system;
    private Date    issueDate, returnDate;
    private User    user = new User();
    private ArrayList<Accessory> accessoryList;


    ////////////////////
     //Database Queries//
    ////////////////////
    public static ResultSet selectAllAssignments() throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT\n" +
                        "    laptopassignment.assign_id,\n" +
                        "    laptopassignment.sys_id,\n" +
                        "    laptopassignment.user_id,\n" +
                        "    laptopassignment.date_issued,\n" +
                        "    laptopassignment.date_returned,\n" +
                        "    user_table.user_name,\n" +
                        "    user_table.user_id,\n" +
                        "    user_table.first_name AS first_name,\n" +
                        "    user_table.last_name AS last_name,\n" +
                        "    worksite.worksite_id AS worksite_id,\n" +
                        "    computersystem.computer_name,\n" +
                        "    computersystem.serialnum,\n" +
                        "    computersystem.system_type,\n" +
                        "    computermodels.model_alias,\n" +
                        "    computermodels.model_id,\n" +
                        "    computermakes.make_alias,\n" +
                        "    computermakes.make_id,\n" +
                        "    worksite.worksite_name\n" +
                        "FROM\n" +
                        "    `laptopassignment`\n" +
                        "INNER JOIN user_table ON user_table.user_id = laptopassignment.user_id\n" +
                        "INNER JOIN computersystem ON computersystem.sys_id = laptopassignment.sys_id\n" +
                        "INNER JOIN computermodels ON computermodels.model_id = computersystem.model_id\n" +
                        "INNER JOIN computermakes ON computermakes.make_id = computermodels.make_id\n" +
                        "INNER JOIN worksite ON user_table.worksite_id = worksite.worksite_id\n"
        ).executeQuery();
    }

    public static ResultSet selectAllCurrentAssignments() throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT\n" +
                        "    laptopassignment.assign_id,\n" +
                        "    laptopassignment.sys_id,\n" +
                        "    laptopassignment.user_id,\n" +
                        "    laptopassignment.date_issued,\n" +
                        "    laptopassignment.date_returned,\n" +
                        "    user_table.user_name,\n" +
                        "    user_table.user_id,\n" +
                        "    user_table.first_name AS first_name,\n" +
                        "    user_table.last_name AS last_name,\n" +
                        "    worksite.worksite_id AS worksite_id,\n" +
                        "    computersystem.computer_name,\n" +
                        "    computersystem.serialnum,\n" +
                        "    computersystem.system_type,\n" +
                        "    computermodels.model_alias,\n" +
                        "    computermodels.model_id,\n" +
                        "    computermakes.make_alias,\n" +
                        "    computermakes.make_id,\n" +
                        "    worksite.worksite_name\n" +
                        "FROM\n" +
                        "    `laptopassignment`\n" +
                        "INNER JOIN user_table ON user_table.user_id = laptopassignment.user_id\n" +
                        "INNER JOIN computersystem ON computersystem.sys_id = laptopassignment.sys_id\n" +
                        "INNER JOIN computermodels ON computermodels.model_id = computersystem.model_id\n" +
                        "INNER JOIN computermakes ON computermakes.make_id = computermodels.make_id\n" +
                        "INNER JOIN worksite ON user_table.worksite_id = worksite.worksite_id\n" +
                        "WHERE\n" +
                        "    is_assigned = 1 AND date_returned IS NULL"
        ).executeQuery();
    }

    public static ResultSet selectUserAssignments(int userID) throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT " +
                        "    laptopassignment.assign_id,\n" +
                        "    laptopassignment.sys_id,\n" +
                        "    laptopassignment.user_id,\n" +
                        "    laptopassignment.date_issued,\n" +
                        "    laptopassignment.date_returned,\n" +
                        "    user_table.user_name,\n" +
                        "    user_table.user_id,\n" +
                        "    user_table.first_name AS first_name,\n" +
                        "    user_table.last_name AS last_name,\n" +
                        "    worksite.worksite_id AS worksite_id,\n" +
                        "    computersystem.computer_name,\n" +
                        "    computersystem.serialnum,\n" +
                        "    computersystem.system_type,\n" +
                        "    computermodels.model_alias,\n" +
                        "    computermodels.model_id,\n" +
                        "    computermakes.make_alias,\n" +
                        "    computermakes.make_id,\n" +
                        "    worksite.worksite_name\n" +
                        "FROM `laptopassignment` " +
                        "INNER JOIN user_table ON user_table.user_id=laptopassignment.user_id " +
                        "INNER JOIN computersystem ON computersystem.sys_id=laptopassignment.sys_id " +
                        "INNER JOIN computermodels ON computermodels.model_id=computersystem.model_id " +
                        "INNER JOIN computermakes ON computermakes.make_id=computermodels.make_id " +
                        "INNER JOIN worksite ON user_table.worksite_id = worksite.worksite_id\n" +
                        "WHERE user_table.user_id = " + userID
        ).executeQuery();
    }

    public static ResultSet selectEquipmentAssignments(int systemID) throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT\n" +
                        "    laptopassignment.assign_id,\n" +
                        "    laptopassignment.sys_id,\n" +
                        "    laptopassignment.user_id,\n" +
                        "    laptopassignment.date_issued,\n" +
                        "    laptopassignment.date_returned,\n" +
                        "    user_table.user_name,\n" +
                        "    user_table.user_id,\n" +
                        "    user_table.first_name AS first_name,\n" +
                        "    user_table.last_name AS last_name,\n" +
                        "    worksite.worksite_id AS worksite_id,\n" +
                        "    computersystem.computer_name,\n" +
                        "    computersystem.serialnum,\n" +
                        "    computersystem.system_type,\n" +
                        "    computermodels.model_alias,\n" +
                        "    computermodels.model_id,\n" +
                        "    computermakes.make_alias,\n" +
                        "    computermakes.make_id,\n" +
                        "    worksite.worksite_name\n" +
                        "FROM\n" +
                        "    `laptopassignment`\n" +
                        "INNER JOIN user_table ON user_table.user_id = laptopassignment.user_id\n" +
                        "INNER JOIN computersystem ON computersystem.sys_id = laptopassignment.sys_id\n" +
                        "INNER JOIN computermodels ON computermodels.model_id = computersystem.model_id\n" +
                        "INNER JOIN computermakes ON computermakes.make_id = computermodels.make_id\n" +
                        "INNER JOIN worksite ON user_table.worksite_id = worksite.worksite_id\n" +
                        "WHERE\n" +
                        "    computersystem.sys_id = " + systemID +
                        "\nORDER BY\n" +
                        "    laptopassignment.assign_id\n" +
                        "DESC\n"
        ).executeQuery();
    }

    public static int selectAssignmentID(int systemID)throws Exception{
        ResultSet rs = DatabaseConfig.getConnection().prepareStatement(
                "SELECT assign_id FROM laptopassignment WHERE sys_id = " + systemID + "AND date_returned IS NULL"
        ).executeQuery();
        try{
            rs.next();
            return rs.getInt("assign_id");
        }catch (Exception e ){
            return 0;
        }
    }

    public static ResultSet selectAccessoryAssignmentsByID(int accessoryID) throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT laptopassignment.assign_id, laptopassignment.user_id,\n" +
                        "accessoryassignement.accassign_id, accessoryassignement.accessory_id,\n" +
                        "user_table.first_name, user_table.last_name, user_table.user_name,\n" +
                        "computeraccessories.accessory_alias, computeraccessories.amount_in_stock, computeraccessories.total\n" +
                        "FROM laptopassignment\n" +
                        "INNER JOIN accessoryassignement ON accessoryassignement.assign_id = laptopassignment.assign_id\n" +
                        "INNER JOIN user_table ON user_table.user_id = laptopassignment.user_id\n" +
                        "INNER JOIN computeraccessories ON computeraccessories.accessory_id = accessoryassignement.accessory_id\n" +
                        "WHERE laptopassignment.date_returned IS NULL AND accessoryassignement.accessory_id = " + accessoryID +
                        "\nORDER BY accessoryassignement.accessory_id"
        ).executeQuery();
    }
    @SuppressWarnings("Duplicates")
    public static int insertNewAssignment(User user, Equipment system, ObservableList<Accessory> accessory, String NewName) throws Exception{
        updateUnAssign(system.getSystemID(), user);
        Connection conn = DatabaseConfig.getConnection();
        PreparedStatement statement = conn.prepareStatement("INSERT INTO `laptopassignment` (`sys_id`, `user_id`, `date_issued`) " +
                "VALUES ( ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, system.getSystemID());
        statement.setInt(2, user.getUserID());
        statement.setDate(3, Date.valueOf(LocalDate.now()));

        statement.execute();
        system.updateIsAssigned(true);
        ResultSet rs = statement.getGeneratedKeys();
        if(rs.next()) {
            for (Accessory acc :
                    accessory) {

                if(acc.getAccessoryID() > 0)
                    Accessory.insertNewAccessoryAssignment(acc, rs.getInt(1));
            }
        }
        if(!(NewName.equals("") || NewName.equals(null))) {
            system.updateSystemName(NewName);
        }
        return rs.getInt(1);
    }

    /**
     * @throws Exception
     * @return assign_id
     */
    public static Assignment updateUnAssign(int systemID, User user) throws Exception{

        Connection conn = DatabaseConfig.getConnection();

        //Set the is_assigned flag to false
        conn.prepareStatement("UPDATE `computersystem` SET `is_assigned` = b'0' WHERE `computersystem`.`sys_id` = " + systemID).execute();

        //Set the return date to current day if null
        PreparedStatement statement = conn.prepareStatement("UPDATE `laptopassignment` SET `date_returned` = '"+ Date.valueOf(LocalDate.now()) +
                "' WHERE `laptopassignment`.`sys_id` = " + systemID +
                " AND date_returned IS NULL AND user_id = "+ user.getUserID());
        statement.executeUpdate();

        //Re-query for the latest assignment that was turned in on the same day
        PreparedStatement state = conn.prepareStatement(
                "SELECT assign_id FROM laptopassignment WHERE `laptopassignment`.`sys_id` = ? AND `date_returned` = ? AND user_id = ?"
        );
        state.setInt(1, systemID);
        state.setDate(2, Date.valueOf(LocalDate.now()));
        state.setInt(3, user.getUserID());
        ResultSet rs = state.executeQuery();
        Assignment rtn = null;
        while(rs.next()) {
            if (rs.isLast()) {
                rtn = new Assignment(
                        rs.getInt("assign_id"),
                        Accessory.unAssignAccessories(rs.getInt("assign_id"))
                );
            }
        }
        return rtn;
    }

    public static void updateUndoUnAssignment(Assignment reAssign) throws Exception{
        Connection conn = DatabaseConfig.getConnection();

        conn.prepareStatement("UPDATE `laptopassignment` SET `date_returned` = NULL WHERE `assign_id` = " + reAssign.getAssignID()).execute();
        ResultSet rs = conn.prepareStatement("SELECT sys_id FROM laptopassignment " +
                "WHERE `laptopassignment`.`assign_id` = " + reAssign.getAssignID()).executeQuery();
        try {
            rs.next();
            conn.prepareStatement("UPDATE `computersystem` SET `is_assigned` = b'1' WHERE `computersystem`.`sys_id` = " + rs.getInt("sys_id")).execute();
        } catch (Exception e){
            System.out.println("Could not update field `is_assigned` in `computersystem` using ReAssignID = " + reAssign.getAssignID());
        }
        try{
            for (Accessory acc : reAssign.getAccessoryList()){
                Accessory.updateIncrementAccessoryStock(acc, -1);
            }
        }catch (Exception e){
            System.out.println("Could not reassign all accessories.");
        }
    }

      ///////////////////
     //List Generators//
    ///////////////////
    public static ObservableList<Assignment> generateAssignmentList(ResultSet AssignmentSet) throws Exception {
        ResultSet rs = AssignmentSet;
        ObservableList<Assignment> list = FXCollections.observableArrayList();
        while (rs.next()) {
            list.add(new Assignment(
                    //int assignID, int systemID, Equipment system, User user,
                    //                      Date issueDate, Date returnDate
                    rs.getInt("assign_id"),
                    rs.getInt("sys_id"),
                    new Equipment(
                            //int systemID, int makeID, int modelID, String systemName, String serialNumber, String make, String model, String type
                            rs.getInt("sys_id"),
                            rs.getInt("make_id"),
                            rs.getInt("model_id"),
                            rs.getString("computer_name"),
                            rs.getString("serialnum"),
                            rs.getString("make_alias"),
                            rs.getString("model_alias"),
                            rs.getString("system_type")
                    ),
                    new User(
                            rs.getInt("user_id"),
                            rs.getInt("worksite_id"),
                            rs.getString("user_name"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            Worksite.generateWorksiteByID(rs.getInt("worksite_id"))
                    ),
                    rs.getDate("date_issued"),
                    rs.getDate("date_returned")

            ));
        }
        return list;
    }

    public static ObservableList<Assignment> generateAssignmentList(int accessoryID) throws Exception {
        ResultSet rs = selectAccessoryAssignmentsByID(accessoryID);
        ObservableList<Assignment> list = FXCollections.observableArrayList();
        while (rs.next()) {
            ArrayList<Accessory> accList = new ArrayList<Accessory>();
            accList.add(new Accessory(
                    //int accessoryID, int inStock, int total, String accessoryName
                    rs.getInt("accessory_id"),
                    rs.getInt("amount_in_stock"),
                    rs.getInt("total"),
                    rs.getString("accessory_alias")
            ));

            list.add(new Assignment(
                    //AssignmentID
                    rs.getInt("assign_id"),
                    //AssignedUser
                    new User(
                            rs.getString("user_name"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getInt("user_id")
                    ),
                    //Accessory List
                    accList
            ));
        }
        return list;
    }

    public static User getLatestUser(int systemID) throws Exception{
        ResultSet rs = selectEquipmentAssignments(systemID);
        while(rs.next()) {
            return User.selectUserById(rs.getInt("user_id"));
        }
        return null;
    }

    ///////////////////
     //Getters/Setters//
    ///////////////////
    public int getAssignID() {
        return assignID;
    }
    public int getSystemID() {
        return systemID;
    }
    public int getUserID() {
        return this.user.getUserID();
    }
    public String getSystemName() {
        return system.getSystemName();
    }
    public String getUserName() {
        return this.user.getUsername();
    }
    public String getUserFullName(){
        return this.user.toString();
    }
    public String getFirstName(){
        return this.user.getFirstName();
    }
    public String getLastName(){
        return this.user.getLastName();
    }
    public User getUser() {
        return this.user;
    }
    public String getSystemModel(){ return system.getModel(); }
    public Equipment getSystem(){ return this.system; }
    public String getSystemType() {
        return system.getType();
    }
    public String getIssueDate() {
        return issueDate.toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
    public String getReturnDate() {
        if(returnDate != null)
            return returnDate.toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        else
            return "Not Returned";
    }
    public void setReturnDate(Date valueOf) {
        returnDate = valueOf;
    }
    public String getSystemMake() {
        return system.getMake();
    }
    public String getSystemMakeModel(){return system.getMake() + " " + system.getModel();}
    public String getSystemSerial() {
        return system.getSerialNumber();
    }
    public ArrayList<Accessory> getAccessoryList() {
        return accessoryList;
    }
    public void setAccessoryList(ArrayList<Accessory> accessoryList) {
        this.accessoryList = accessoryList;
    }

    ////////////////
     //Constructors//
    ////////////////
    public Assignment(int assignID, int systemID, Equipment system, User user,
                      Date issueDate, Date returnDate) {
        this.assignID = assignID;
        this.systemID = systemID;
        this.system = system;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.user = user;
    }
    public Assignment(int assignID, int systemID, Equipment system, User user, Date issueDate) {
        this.assignID = assignID;
        this.systemID = systemID;
        this.issueDate = issueDate;
        this.system = system;
        this.user = user;
    }

    public Assignment(int assignID, User user, ArrayList<Accessory> accessoryList) {
        this.assignID = assignID;
        this.user = user;
        this.accessoryList = accessoryList;
    }

    public Assignment(int assignID, ArrayList<Accessory> accessoryList) {
        this.assignID = assignID;
        this.accessoryList = accessoryList;
    }

    public static Assignment unAssign(int systemID) {
        try {
            return Assignment.updateUnAssign(systemID, getLatestUser(systemID));
        } catch (Exception e) {
            System.out.println("Cannot un-assign from previous user");
        }
        return null;
    }

    /////////////
    //Overrides//
    /////////////
    @Override
    public String toString(){
        return this.getSystemName();
    }

}
