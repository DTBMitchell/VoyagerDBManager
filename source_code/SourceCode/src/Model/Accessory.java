package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Accessory {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private int accessoryID, inStock, total;
    private String accessoryName;


    ////////////////////
     //Database Queries//
    ////////////////////
    public static ResultSet selectAllAccessories() throws Exception{
      return DatabaseConfig.getConnection().prepareStatement(
              "SELECT * FROM computeraccessories"
      ).executeQuery();
    }

    public static ResultSet selectAccessoriesInAssignment(int assign_id) throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT * FROM computeraccessories " +
                        "INNER  JOIN `accessoryassignement` ON `accessoryassignement`.accessory_id = computeraccessories.accessory_id " +
                        "WHERE assign_id = '"+assign_id+"'"
        ).executeQuery();
    }
    public static ResultSet selectAccessoryyByID(int accessoryID) throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT * FROM `computeraccessories` WHERE accessory_id = " + accessoryID
        ).executeQuery();
    }

    public static ResultSet selectAccessoryyByName(String accessoryName) throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT * FROM `computeraccessories` WHERE accessory_alias = " + accessoryName
        ).executeQuery();
    }

    public static void insertNewAccessory(Accessory accessory) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement(
                "INSERT INTO `computeraccessories` (`accessory_alias`, `amount_in_stock`, `total`) VALUES (?, ?, ?);"
        );
        statement.setString(1, accessory.getAccessoryName());
        statement.setInt(2, accessory.getInStock());
        statement.setInt(3, accessory.getInStock());
        statement.execute();
    }

    public static void insertNewAccessoryAssignment(Accessory accessory, int assignID) throws Exception{
        PreparedStatement statement = DatabaseConfig.getConnection().prepareStatement("INSERT INTO `accessoryassignement` (`assign_id`, `accessory_id`) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, assignID);
        statement.setInt(2, accessory.getAccessoryID());
        System.out.println(statement.toString());
        statement.execute();

        ResultSet rs = statement.getGeneratedKeys();
        while(rs.next()) {
            updateIncrementAccessoryStock(accessory, -1);
        }
    }

    public static void updateAccessoryStock(int accessoryID, int newAmountInStock, int newTotal) throws Exception{
        PreparedStatement updateStock = DatabaseConfig.getConnection().prepareStatement(
                "UPDATE `computeraccessories` SET `amount_in_stock`=?,`total`= ? " +
                        "WHERE accessory_id = " + accessoryID
        );
        updateStock.setInt(1, newAmountInStock);
        updateStock.setInt(2, newTotal);
        updateStock.execute();
    }

    public static void updateIncrementAccessoryStock(Accessory accessory, int updateIncrement) throws Exception{
        PreparedStatement updateStock = DatabaseConfig.getConnection().prepareStatement(
                "UPDATE `computeraccessories` SET `amount_in_stock` = `amount_in_stock`+? WHERE `computeraccessories`.`accessory_id` = ?;"
        );
        updateStock.setInt(1, updateIncrement);
        updateStock.setInt(2, accessory.getAccessoryID());
        updateStock.execute();
    }


      ///////////////////
     //List Generators//
    ///////////////////
    public static ObservableList<Accessory> generateAccessoryList() throws Exception{
        ResultSet rs = selectAllAccessories();
        ObservableList<Accessory> list = FXCollections.observableArrayList();
        list.add(new Accessory());
        while(rs.next()){
            list.add(new Accessory(
              rs.getInt("accessory_id"),
              rs.getInt("amount_in_stock"),
              rs.getInt("total"),
              rs.getString("accessory_alias")
            ));
        }
        return list;
    }

    public static ArrayList<Accessory> unAssignAccessories(int assignID) throws Exception{
        ResultSet rs = selectAccessoriesInAssignment(assignID);
        ArrayList<Accessory> rtn = new ArrayList<>();
        while(rs.next()){
            //Create the accessory
            Accessory accessory = new Accessory(
                    //int accessoryID, int inStock, int total, String accessoryName
                    rs.getInt("accessory_id"),
                    rs.getInt("amount_in_stock"),
                    rs.getInt("total"),
                    rs.getString("accessory_alias")
            );
            //Add it to the list
            rtn.add(accessory);
            updateIncrementAccessoryStock(accessory, 1);
        }
        //Send the list to the ranch
        return rtn;
    }


      ///////////////////
     //Getters/Setters//
    ///////////////////
    public int getAccessoryID() {
        return accessoryID;
    }
    public int getInStock() {
        return inStock;
    }
    public int getTotal() {
        return total;
    }
    public String getAccessoryName() {
        return accessoryName;
    }


      ////////////////
     //Constructors//
    ////////////////
    public Accessory(int accessoryID, int inStock, int total, String accessoryName) {
        this.accessoryID = accessoryID;
        this.inStock = inStock;
        this.total = total;
        this.accessoryName = accessoryName;
    }
    public Accessory(String accessoryName, int inStock) {
        this.inStock = inStock;
        this.accessoryName = accessoryName;
    }
    public Accessory(String accessoryName) {
        this.accessoryName = accessoryName;
    }
    public Accessory(int accessoryID){
        try {
            ResultSet rs = selectAccessoryyByID(accessoryID);
            rs.next();
            this.accessoryID = accessoryID;
            this.accessoryName = rs.getString("accessory_alias");
            this.inStock = rs.getInt("amount_in_stock");
            this.total = rs.getInt("total");

        } catch (Exception e){
            System.out.println("Failed to generate the 'Accessory'");
        }
    }
    public Accessory(ResultSet rs) throws Exception{
        if(rs.next()){
            this.accessoryID = rs.getInt(1);
            this.accessoryName = rs.getString(2);
            this.inStock = rs.getInt(3);
            this.total = rs.getInt(4);
        }

    }

    public Accessory() {
        this.accessoryID = -1;
        this.inStock = -1;
        this.total = -1;
        this.accessoryName = "";
    }

    /////////////
    //Overrides//
    /////////////
    @Override
    public String toString(){
        return this.accessoryName;
    }

    public boolean CompareTo(Accessory accessory){
        if(this.accessoryName.equals(accessory.getAccessoryName())){
            return true;
        }
        else
            return false;
    }
}
