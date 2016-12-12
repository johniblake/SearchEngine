/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerdata;

import java.util.Hashtable;
import webobjects.*;

/**
 *
 * @author johnblake
 * @class FrontierDB a hashtable containing the Hash:Anchor pair for every URL in the frontier. Check new URLs against this in order to avoid duplicate URLs in frontier.
 */
public class FrontierList extends Hashtable<String,Anchor> {
    public Hashtable<String,Anchor> hashtable;
    public FrontierList(){
        hashtable = new Hashtable<String,Anchor>();
    }
    
}
