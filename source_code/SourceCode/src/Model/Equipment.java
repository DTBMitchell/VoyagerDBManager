package Model;

import Controller.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Equipment {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private int systemID, makeID, modelID;
    private String systemName, serialNumber, make, model, type;
    private boolean isAssigned;


      ////////////////////
     //Database Queries//
    ////////////////////

    //Selects
    /**
     * @return ResultSet of query
     * @throws Exception
     */
    //Equipment.queryAllEquipment();
    public static ResultSet queryAllEquipment() throws Exception{
          return DatabaseConfig.getConnection().prepareStatement(
                  "SELECT computersystem.*, computermodels.*, computermakes.make_alias " +
                          "FROM `computersystem` " +
                          "INNER JOIN computermodels ON computermodels.model_id=computersystem.model_id " +
                          "INNER JOIN computermakes ON computermakes.make_id=computermodels.make_id").executeQuery();
      }
    public static ResultSet queryAllEquipmentGrouped() throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT computersystem.*, computermodels.*, computermakes.make_alias " +
                        "FROM `computersystem` " +
                        "INNER JOIN computermodels ON computermodels.model_id=computersystem.model_id " +
                        "INNER JOIN computermakes ON computermakes.make_id=computermodels.make_id " +
                        "GROUP BY computersystem.model_id").executeQuery();
    }

    public static ResultSet queryAllSerialNumbers(int modelID, int makeID) throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT computersystem.serialnum "+
                        "FROM computersystem" +
                        " INNER JOIN computermodels ON computermodels.model_id= " + modelID +
                        " INNER JOIN computermakes ON computermakes.make_id= " + makeID
        ).executeQuery();
    }

    public static ResultSet queryUnassignedSerialNumbers (int modelID, int makeID) throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT computersystem.serialnum "+
                        "FROM computersystem" +
                        " INNER JOIN computermodels ON computermodels.model_id=  computersystem.model_id "+
                        " INNER JOIN computermakes ON computermakes.make_id= computermodels.make_id " +
                        "WHERE is_assigned = 0 && computermakes.make_id = " + makeID +
                            " && computermodels.model_id = " + modelID
        ).executeQuery();
    }

    public static ResultSet queryAllMakes() throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT * "+
                        "FROM computermakes"
        ).executeQuery();
    }

    public static ResultSet queryAllModels() throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT * "+
                        "FROM computermodels"
        ).executeQuery();
    }

    public static int queryMakeByName(String make_alias) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement("SELECT make_id FROM computermakes WHERE make_alias = ?");
        statement.setString(1, make_alias);
        ResultSet rs = statement.executeQuery();
        rs.next();
        return rs.getInt("make_id");
    }

    public static  int queryModelByName(String model_alias, int makeID) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement("SELECT * FROM computermodels WHERE model_alias = ? AND make_id = ?");
        statement.setString(1, model_alias);
        statement.setInt(2, makeID);
        ResultSet rs = statement.executeQuery();
        rs.next();
        return rs.getInt("model_id");
    }

    public static  ResultSet queryModelByID(int modelID) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement("SELECT * FROM computermodels WHERE model_id= ?");
        statement.setInt(1, modelID);
        return statement.executeQuery();
    }


    /**
     * @param make_alias
     * @return ResultSet of Query
     * @throws Exception
     */
    private static ResultSet queryModelsByMake(String make_alias) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement(
                "SELECT * FROM `computermodels`\n" +
                        "INNER JOIN computermakes ON computermodels.make_id=computermakes.make_id\n" +
                        "WHERE make_alias = ?");
        statement.setString(1, make_alias);
        return statement.executeQuery();
    }

    //Inserts
    public static int insertNewMake(String make_alias) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement(
                "INSERT INTO `computermakes` (`make_alias`) VALUES (?)"
        );
        statement.setString(1, make_alias);
        statement.execute();

        return queryMakeByName(make_alias);
    }

    public static int insertNewModel(String model_alias, int makeID) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement(
                "INSERT INTO `computermodels` (`model_alias`, `make_id`) VALUES (?, ?);"
        );
        statement.setString(1, model_alias);
        statement.setInt(2, makeID);
        statement.execute();

        return queryModelByName(model_alias, makeID);
    }

    public static void insertNewEquipment(String serialNumber, String type, int modelID) throws Exception{
        PreparedStatement  statement = DatabaseConfig.getConnection().prepareStatement(
                "INSERT INTO `computersystem` (`computer_name`, `serialnum`, `model_id`, `system_type`, `is_assigned`) " +
                        "VALUES (?, ?, ?, ?, 0);"
        );
        String typeAbbr = type.toLowerCase() == "laptop" ? "LT" : "DT";
        statement.setString(1, "Stock" + typeAbbr + serialNumber.substring(serialNumber.length()-4));
        statement.setString(2, serialNumber);
        statement.setInt(3, modelID);
        statement.setString(4, type);

        statement.execute();
    }


    //Updates
    /**
     * @param modelID
     * @param makeID
     * @param model_alias
     * @throws Exception
     */
    public static void updateComputerModels(int modelID, int makeID, String model_alias) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement(
                "UPDATE `computermodels` SET " +
                        "`make_id` = ?, " +
                        "`model_alias` = ?, " +
                        "`amount_in_stock` = ?, " +
                        "`total` = ?" +
                        "WHERE `computermodels`.`model_id` = ?;"
        );
        statement.setInt(1, makeID);
        statement.setString(2, model_alias);
        ResultSet rs = queryModelByID(modelID);
        rs.next();
        statement.setInt(3, rs.getInt("amount_in_stock")+1);
        statement.setInt(4, rs.getInt("total")+1);
        statement.setInt(5, modelID);
    }

    public static void updateSystem(int SystemID, String NewName, String Type, String Make, String Model, String SerialNumber, LocalDate DateAssigned, LocalDate DateReturned) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement(
                "UPDATE `computersystem`, `computermodels`, `laptopassignment` " +
                        "SET " +
                        "`computersystem`.`computer_name` = ?, " +
                        "`computersystem`.`serialnum` = ?, " +
                        "`computersystem`.`model_id` = ?, " +
                        "`computersystem`.`system_type` = ?, " +

                        "WHERE `computersystem`.`sys_id` = ?;"
        );
        statement.setString(1, NewName);
        statement.setString(2, SerialNumber);
        statement.setInt(3, queryModelByName(Model, queryMakeByName(Make)));
        statement.setString(4, Type);
        statement.setString(5, Model);
        statement.setInt(6, SystemID);
        statement.execute();


        PreparedStatement state = DatabaseConfig.getConnection().prepareStatement(
                "UPDATE `laptopassignment` " +
                        "SET " +
                        "`laptopassignment`.`date_issued` = ?, " +
                        "`laptopassignment`.`date_returned` = ? " +
                        "WHERE assign_id = ?"
        );
        state.setDate(1, Date.valueOf(DateAssigned));
        try {
            state.setDate(2, Date.valueOf(DateReturned));
        }catch (Exception e){
            state.setNull(2, Types.DATE);
        }
        state.setInt(3, Main.getAssignmentFocus().getAssignID());

        state.execute();
    }

    public void updateSystemName(String NewName) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement(
                "UPDATE `computersystem` SET `computer_name` = ? WHERE `computersystem`.`sys_id` = ?;"
        );
        statement.setString(1, NewName);
        statement.setInt(2, this.getSystemID());
        statement.execute();
    }

    public void updateIsAssigned(boolean isAssigned) throws Exception{
        int bit = isAssigned ? 1 : 0;
        DatabaseConfig.getConnection().prepareStatement(
                "UPDATE `computersystem` SET `is_assigned` = b'"+bit+"' WHERE `computersystem`.`sys_id` = " + this.systemID
        ).execute();
    }


      ///////////////////
     //List Generators//
    ///////////////////
    public static ObservableList<Equipment> generateEquipmentList(ResultSet rs)throws Exception{
        ObservableList<Equipment> list = FXCollections.observableArrayList();
        while (rs.next()) {
            list.add(new Equipment(
                    rs.getInt("sys_id"),
                    rs.getInt("make_id"),
                    rs.getInt("model_id"),
                    rs.getString("computer_name"),
                    rs.getString("serialnum"),
                    rs.getString("make_alias"),
                    rs.getString("model_alias"),
                    rs.getString("system_type"),
                    rs.getByte("is_assigned") != 0
            ));
        }
        return list;
    }

    public static ObservableList<String> generateSerialList(int modelID, int makeID) throws Exception{
        ResultSet rs = Equipment.queryUnassignedSerialNumbers(modelID, makeID);
        ObservableList<String> list = FXCollections.observableArrayList();
        while(rs.next()){
            list.add(rs.getString("serialnum"));
        }
        return list;
    }

    public static ArrayList<String> generateMakeList() throws Exception{
        ResultSet rs = queryAllMakes();
        ArrayList<String> list = new ArrayList<>();
        while (rs.next()){
            list.add(rs.getString("make_alias"));
        }
        return list;
    }

    public static ArrayList<String> generateModelList(String make_alias) throws Exception{
        ResultSet rs = queryModelsByMake(make_alias);
        ArrayList<String> list = new ArrayList<>();
        while (rs.next()){
            list.add(rs.getString("model_alias"));
        }
        return list;
    }


    ///////////////////
     //Getters/Setters//
    ///////////////////
    public int getSystemID() {
        return systemID;
    }
    public int getMakeID() {
        return makeID;
    }
    public int getModelID() {
        return modelID;
    }
    public String getSystemName() {
        return systemName;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }
    public String getType() {
        return type;
    }
    public boolean isAssigned() {
        return isAssigned;
    }
    public boolean setAssigned() {
        this.isAssigned = !this.isAssigned;
        return this.isAssigned;
    }

      ////////////////
     //Constructors//
    ////////////////
    public Equipment(int systemID, int makeID, int modelID, String systemName, String serialNumber, String make, String model, String type, boolean isAssigned) {
        this.systemID = systemID;
        this.makeID = makeID;
        this.modelID = modelID;
        this.systemName = systemName;
        this.serialNumber = serialNumber;
        this.make = make;
        this.model = model;
        this.type = type;
        this.isAssigned = isAssigned;
    }

    public Equipment(int systemID, int makeID, int modelID, String systemName, String serialNumber, String make, String model, String type) {
        this.systemID = systemID;
        this.makeID = makeID;
        this.modelID = modelID;
        this.systemName = systemName;
        this.serialNumber = serialNumber;
        this.make = make;
        this.model = model;
        this.type = type;
    }

    /////////////
    //Overrides//
    /////////////
    @Override
    public String toString(){
        return make + " " + model;
    }
}
