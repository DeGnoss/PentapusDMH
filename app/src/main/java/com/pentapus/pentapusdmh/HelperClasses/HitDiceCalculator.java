package com.pentapus.pentapusdmh.HelperClasses;

/**
 * Created by Koni on 06.12.2016.
 */

public class HitDiceCalculator {

    public static String calculateHdType(int numberofhitdice, String size, int conmod){
        int hdmod;
        String hdtype ="";
        hdmod = numberofhitdice * conmod;
        switch(size){
            case "Tiny":
                hdtype = "d4";
                break;
            case "Small":
                hdtype = "d6";
                break;
            case "Medium":
                hdtype = "d8";
                break;
            case "Large":
                hdtype = "d10";
                break;
            case "Huge":
                hdtype = "d12";
                break;
            case "Gargantuan":
                hdtype = "d20";
                break;
            default:
                break;
        }
        if(conmod != 0){
            if(conmod >= 0){
                hdtype = hdtype + " + " + String.valueOf(hdmod);
            }else{
                hdtype = hdtype + " - " + String.valueOf(Math.abs(hdmod));
            }
        }
        return hdtype;
    }

    public static int calculateAverageHp(int numberofhitdice, String size, int conmod){
        int hp;
        switch(size){
            case "Tiny":
                hp = (int) Math.floor((numberofhitdice*2.5) + (conmod*numberofhitdice));
                break;
            case "Small":
                hp = (int) Math.floor((numberofhitdice*3.5) + (conmod*numberofhitdice));
                break;
            case "Medium":
                hp = (int) Math.floor((numberofhitdice*4.5) + (conmod*numberofhitdice));
                break;
            case "Large":
                hp = (int) Math.floor((numberofhitdice*5.5) + (conmod*numberofhitdice));
                break;
            case "Huge":
                hp = (int) Math.floor((numberofhitdice*6.5) + (conmod*numberofhitdice));
                break;
            case "Gargantuan":
                hp = (int) Math.floor((numberofhitdice*10.5) + (conmod*numberofhitdice));
                break;
            default:
                hp = 0;
                break;
        }
        if(hp<0){
            hp = 0;
        }
        return hp;
    }
}
