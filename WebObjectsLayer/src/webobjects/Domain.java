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
public class Domain {
    private String domainHash;
    private String domainUrl;
    private boolean activated;
    private Timestamp modified;
    private Timestamp created;
    private int weight;

    public String getDomainHash() {
        return domainHash;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public Timestamp getModified() {
        return modified;
    }

    public Timestamp getCreated() {
        return created;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public Domain(String domainUrl) throws Exception {
        this.domainHash = Hasher.toSHA256(domainUrl);
        this.domainUrl = domainUrl;
        this.activated = true;
        this.created = Common.getTimestamp();
        this.modified = Common.getTimestamp();
        this.weight = 1;
        
    }

    public Domain(String domainHash, String domainUrl) {
        this.domainHash = domainHash;
        this.domainUrl = domainUrl;
        this.weight = 1;
    }

    public Domain(String domainHash, String domainUrl, boolean activated, Timestamp modified, Timestamp created) {
        this.domainHash = domainHash;
        this.domainUrl = domainUrl;
        this.activated = activated;
        this.modified = modified;
        this.created = created;
        this.weight = 1;
    }
    
    
}
