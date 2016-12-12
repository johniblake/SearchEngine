/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerdata;

import java.util.*;
import webobjects.*;


/**
 *
 * @author johnblake
 * @class FrontierQueue A priority queue of the frontier URLs sorted by URL weight. At every step the crawler takes a URL from the head of the queue. 
 */
public class FrontierQueue extends LinkedList<Domain>{
//    public PriorityQueue<Domain> pq;
    public LinkedList<Domain> pq;
    public FrontierQueue(){
//        pq = new PriorityQueue<Domain>(10, new Comparator<Domain>(){
//
//            @Override
//            public int compare(Domain a, Domain b) {
//                if (a.getWeight() < b.getWeight()){
//                    return -1;
//                }
//                else if (a.getWeight() > b.getWeight()){
//                    return 1;
//                }else{
//                    return 0;
//                }
//            }
//        }); 

        pq = new LinkedList();
    }
}
