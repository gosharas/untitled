/*
Файл указывает правило, какие данные будут переданы серверу.
 */

package ru.gosha.interpreter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class PackageToServer extends Package {

    /**
    Тут содержатся файлы и .xls и .xlsx
    Это массив файлов. Каждый файл - это массив байтов.
     */
    public final byte[][] ExcelsFiles;
    /**
    Тут содержатся критерии запроса.
     */
    public final Seeker QueryCriteria;

    /**
    Строит данные отправляемые на сервер.
     @param ExcelsFiles Тут содержатся файлы и .xls и .xlsx. Это массив файлов. Каждый файл - это массив байтов.
     @param QueryCriteria Тут содержатся критерии запроса.
     */
    public PackageToServer(byte[][] ExcelsFiles, Seeker QueryCriteria) {
        this.ExcelsFiles = ExcelsFiles;
        this.QueryCriteria = QueryCriteria;
    }



    /**
     * Преобразует входящий массив байтов в текущее хранилище.
     * @param input Массив байтов, который необходимо перевести в текущий класс.
     * @return Представление хранилища в классе PackageToClient. Если ошибка, то null.
     * @throws ClassNotFoundException Данные не истинные.
     */
    public static PackageToServer fromByteArray(byte[] input) throws ClassNotFoundException {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(input);
            ObjectInputStream inObj = new ObjectInputStream(in);
            PackageToServer out = (PackageToServer) inObj.readObject();
            inObj.close();
            return out;
        } catch(IOException error) {
            return null;
        }
    }
}
