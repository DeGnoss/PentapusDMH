package com.pentapus.pentapusdmh.HelperClasses;

/**
 * Created by konrad.fellmann on 01.04.2016.
 */
public class DiceHelper {

    public static Integer d20() {
        return (int) Math.floor(20*Math.random())+1;
    }

}
