package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

public class Worksite {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private int worksiteID, addressID;
    private String[] address;
    private String worksiteName, department;


      ////////////////////
     //Database Queries//
    ////////////////////
    private static ResultSet selectAllWorksites()throws Exception{
        return DatabaseConfig.getConnection().prepareStatement("SELECT * FROM worksite").executeQuery();
    }
    private static ResultSet selectWorksiteById(int worksiteID)throws Exception{
        return DatabaseConfig.getConnection().prepareStatement("SELECT * FROM worksite WHERE worksite_id = " + worksiteID).executeQuery();
    }
    private static ResultSet selectAddressByID(int addressID) throws Exception{
        return DatabaseConfig.getConnection().prepareStatement(
                "SELECT * FROM address WHERE address_id = " + addressID
        ).executeQuery();
    }

      ///////////////////
     //List Generators//
    ///////////////////
    @SuppressWarnings("Duplicates")
    public static ObservableList<Worksite> getWorksiteList() throws Exception{
        ResultSet rs = selectAllWorksites();
        ObservableList<Worksite> list = FXCollections.observableArrayList();

        while (rs.next()){
            ResultSet addr = selectAddressByID(rs.getInt("address_id"));
            addr.next();

            list.add(new Worksite(
                //int addressID, String[] address, String worksiteName, String department'
                rs.getInt("worksite_id"),
                rs.getInt("address_id"),
                new String[]{
                    addr.getString("streetaddr"),
                    addr.getString("city"),
                    addr.getString("state"),
                    addr.getString("country"),
                    addr.getString("zipcode")
                },
                rs.getString("worksite_name"),
                rs.getString("department")
            ));
        }
        return list;
    }

    @SuppressWarnings("Duplicates")
    public static Worksite generateWorksiteByID(int worksiteID) throws Exception{
        ResultSet rs = selectWorksiteById(worksiteID);
        rs.next();
        ResultSet addr = selectAddressByID(rs.getInt("address_id"));
        addr.next();

        return new  Worksite(
                //int addressID, String[] address, String worksiteName, String department'
                rs.getInt("worksite_id"),
                rs.getInt("address_id"),
                new String[]{
                        addr.getString("streetaddr"),
                        addr.getString("city"),
                        addr.getString("state"),
                        addr.getString("country"),
                        addr.getString("zipcode")
                },
                rs.getString("worksite_name"),
                rs.getString("department")
        );
    }

      ///////////////////
     //Getters/Setters//
    ///////////////////
    public int getWorksiteID() {
        return worksiteID;
    }
    public void setWorksiteID(int worksiteID) {
        this.worksiteID = worksiteID;
    }

    public int getAddressID() {
        return addressID;
    }
    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String[] getAddress() {
        return address;
    }
    public void setAddress(String[] address) {
        this.address = address;
    }

    public String getWorksiteName() {
        return worksiteName;
    }
    public void setWorksiteName(String worksiteName) {
        this.worksiteName = worksiteName;
    }

    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

      ////////////////
     //Constructors//
    ////////////////
    public Worksite(String[] address, String worksiteName, String department) {
        this.address = address;
        this.worksiteName = worksiteName;
        this.department = department;
    }
    public Worksite(int worksiteID, int addressID, String[] address, String worksiteName, String department) {
        this.worksiteID = worksiteID;
        this.addressID = addressID;
        this.address = address;
        this.worksiteName = worksiteName;
        this.department = department;
    }

    /////////////
     //Overrides//
    /////////////
    @Override
    public String toString(){
        if(department.equals("Admin"))
            return worksiteName;
        else
            return worksiteName + ": " + department;
    }
}
