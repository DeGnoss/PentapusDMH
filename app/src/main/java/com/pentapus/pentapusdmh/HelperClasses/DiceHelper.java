package com.pentapus.pentapusdmh.HelperClasses;

/**
 * Created by konrad.fellmann on 01.04.2016.
 */
public class DiceHelper {

    public static Integer d20() {
        return (int) Math.floor(20*Math.random())+1;
    }

    public static Integer d12() {
        return (int) Math.floor(12*Math.random())+1;
    }

    public static Integer d10() {
        return (int) Math.floor(10*Math.random())+1;
    }

    public static Integer d8() {
        return (int) Math.floor(8*Math.random())+1;
    }

    public static Integer d6() {
        return (int) Math.floor(6*Math.random())+1;
    }

    public static Integer d4() {
        return (int) Math.floor(4*Math.random())+1;
    }

}
