package ru.gosha;

import ru.gosha.SG_Muwa.*;
import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws Exception {
        OpenFile openFileXLS = new OpenFile("/Users/georgijfalileev/IdeaProjects/untitled/client-exel/delete1.xls");
        System.out.println(openFileXLS.getCellData(1,1));
        System.out.println(openFileXLS.getCellData(1,2));
        System.out.println(openFileXLS.getCellData(2,1));
        System.out.println(openFileXLS.getCellData(5,5));
        openFileXLS.close();
//
        InputSeeker inputSeeker = new InputSeeker();
        Seeker seeker = inputSeeker.setSeeker();
        System.out.print(seeker.DefaultAddress+" "+seeker.NameOfSeeker+" "+seeker.dateStart+" ");
        System.out.print(seeker.dateFinish+" "+seeker.seekerType + " ");


    }
}


