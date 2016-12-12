/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webobjects;

import java.sql.Timestamp;

/**
 *
 * @author johnblake
 */
public class Common {
    public static Timestamp getTimestamp(){
        java.util.Date date = new java.util.Date();
        return new Timestamp(date.getTime());
    }
}
