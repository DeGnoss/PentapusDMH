package com.pentapus.pentapusdmh.TableClasses;

/**
 * Created by Koni on 27.02.2016.
 */
public class Session {
    private int id;
    private int belongsTo;
    private String name;
    private String info;


    public Session(int id, int belongsTo, String name, String info){
        this.id=id;
        this.belongsTo=belongsTo;
        this.name=name;
        this.info=info;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public int getBelongsTo(){
        return belongsTo;
    }
    public void setBelongsTo(int belongsTo){
        this.belongsTo = belongsTo;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getInfo(){
        return info;
    }
    public void setInfo(String info){
        this.info = info;
    }

}
