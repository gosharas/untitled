package ru.gosha;

import ru.gosha.interpreter.Seeker;
import ru.gosha.interpreter.SeekerType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.TimeZone;

public class InputSeeker {

    /**
     * Функция работает с пользователем для ввода данных
     * @return обьект класса Seeker
     * @throws ParseException
     */

    public Seeker setSeeker() throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        TimeZone timezone = TimeZone.getDefault();
        int timeZone = timezone.getRawOffset();
        Scanner scanner = new Scanner(System.in);
        String teacher = null;
        String date = null;
        String nameOfSeeker = null;
        String defaultAddress = null;
        LocalDate dateStart;
        LocalDate dateFinish;

        System.out.print("nameOfSeeker: ");
        nameOfSeeker = scanner.nextLine();

        System.out.print("teacher or group?  ");
        teacher = scanner.nextLine();

        System.out.print("dateStart yyyy-MM-dd: ");
        date = scanner.nextLine();
        dateStart = LocalDate.parse(date);

        System.out.print("dateFinish yyyy-MM-dd: ");
        date = scanner.nextLine();
        dateFinish = LocalDate.parse(date);

        System.out.print("defaultAddress: ");
        defaultAddress = scanner.nextLine();

        //System.out.println(nameOfSeeker +" "+ teacher+" "+ studyGroup+" "+dateStart+" "+dateFinish+" "+defaultAddress);


        return new Seeker(nameOfSeeker, teacher.equals("group") ? SeekerType.StudyGroup : SeekerType.Teacher, dateStart, dateFinish , timeZone/1000, defaultAddress);
    }
}
