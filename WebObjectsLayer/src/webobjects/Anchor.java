package webobjects;

import java.sql.Timestamp;

/**
 *
 * @author johnblake
 */
public class Anchor {
    private Domain domain;
    private String anchorHash;
    private String anchorUrl;
    private int scanStatus;
    private boolean activated;
    private Timestamp modified;
    private Timestamp created;
    private int hitcount; 

    public Domain getDomain() {
        return domain;
    }

    public String getAnchorHash() {
        return anchorHash;
    }

    public String getAnchorUrl() {
        return anchorUrl;
    }

    public int getScanStatus() {
        return scanStatus;
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
     public void incrementHits() {
         hitcount+=1;
         modified = Common.getTimestamp();
     }

    public Anchor(Domain domain, String anchorUrl) throws Exception{
        this.domain = domain;
        this.anchorHash = Hasher.toSHA256(anchorUrl);
        this.anchorUrl = anchorUrl;
        this.scanStatus = 0;
        this.activated = true;
        this.modified = Common.getTimestamp();
        this.created = Common.getTimestamp();
        this.hitcount = 0;
        
        
    }
    
    public Anchor(Domain domain, String anchorHash, String anchorUrl) {
        this.domain = domain;
        this.anchorHash = anchorHash;
        this.anchorUrl = anchorUrl;
        this.hitcount = 0;
    }

    public Anchor(Domain domain, String anchorHash, String anchorUrl, int scanStatus, boolean activated, Timestamp modified, Timestamp created) {
        this.domain = domain;
        this.anchorHash = anchorHash;
        this.anchorUrl = anchorUrl;
        this.scanStatus = scanStatus;
        this.activated = activated;
        this.modified = modified;
        this.created = created;
        this.hitcount = 0;
    }
    
    
}
