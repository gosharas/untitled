package ru.gosha;
import ru.gosha.SG_Muwa.Seeker;
import ru.gosha.SG_Muwa.SeekerType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class InputSeeker{

    /**
     * Функция работает с пользователем для ввода данных
     * @return обьект класса Seeker
     * @throws ParseException
     */

    public Seeker setSeeker() throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Scanner scanner = new Scanner(System.in);
        String teacher = null;
        String studyGroup = null;
        String date = null;
        String nameOfSeeker = null;
        String defaultAddress = null;
        Date dateStart;
        Date dateFinish;

        System.out.print("nameOfSeeker: ");
        nameOfSeeker = scanner.nextLine();

        System.out.print("teacher: ");
        teacher = scanner.nextLine();

        System.out.print("studyGroup: ");
        studyGroup = scanner.nextLine();

        System.out.print("dateStart yyyy-MM-dd: ");
        date = scanner.nextLine();
        dateStart = ft.parse(date);

        System.out.print("dateFinish yyyy-MM-dd: ");
        date = scanner.nextLine();
        dateFinish = ft.parse(date);

        System.out.print("defaultAddress: ");
        defaultAddress = scanner.nextLine();

        //System.out.println(nameOfSeeker +" "+ teacher+" "+ studyGroup+" "+dateStart+" "+dateFinish+" "+defaultAddress);


        return new Seeker(nameOfSeeker, teacher.equals("") ? SeekerType.StudyGroup : SeekerType.Teacher, dateStart, dateFinish ,defaultAddress);
    }
}
