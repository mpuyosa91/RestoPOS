/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.Crew;

/**
 *
 * @author mpuyosa91
 */
public class CrewMember {
    public enum Permision{
        Attendant(1),
        Manager(2),
        Administrator(4);
        
        private final int permissionCode;
        Permision(int permission){
            this.permissionCode = permission;
        }
        public int getPermisionCode(){
            return this.permissionCode;
        }
    }
    
    public CrewMember(){
        
    }

    public String getUser() {
        return user;
    }

    public void setUser(String nombre) {
        this.user = nombre;
    }
    
    public String getHash(){
        return hash;
    }
    
    public void setHash(String hash){
        this.hash = hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getPermisionLevel() {
        return permisionLevel;
    }

    public void setPermisionLevel(short permisionLevel) {
        this.permisionLevel = permisionLevel;
    }
    
    private String user;
    private String hash;
    private int id;
    private short permisionLevel;
}
