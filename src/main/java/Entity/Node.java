package Entity;

import java.util.Date;

public class Node {
    private int id;
    private String nodeID;
    private Float nodeVal;
    private String battery;
    private String readTime;
    private String recvTime;

    public Node(String nodeID,Float nodeVal,String battery,String readTime,String recvTime)
    {
        this.battery=battery;
        this.readTime=readTime;
        this.nodeID=nodeID;
        this.nodeVal=nodeVal;
        this.recvTime=recvTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public Float getNodeVal() {
        return nodeVal;
    }

    public void setNodeVal(Float nodeVal) {
        this.nodeVal = nodeVal;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(String recvTime) {
        this.recvTime = recvTime;
    }
}
