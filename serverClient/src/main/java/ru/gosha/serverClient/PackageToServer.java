/*
Файл указывает правило, какие данные будут переданы серверу.
 */

package ru.gosha.serverClient;

import java.io.*;

public class PackageToServer implements Serializable {

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
     * Преобразует текущий класс в поток байтов.
     * @return Хранилище данного класса в виде байтов.
     */
    public byte[] toByteArray() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream outObj = new ObjectOutputStream(out);


            // conversion from "yourObject" to byte[]
            outObj.writeObject(this);
            outObj.flush();
            outObj.close();
            return out.toByteArray();
        }
        catch (IOException error){
            return new byte[]{};
        }
    }

    /**
     * Преобразует входящий массив байтов в текущее хранилище.
     * @param input Массив байтов, который необходимо перевести в текущий класс.
     * @return Представление хранилища в классе PackageToServer. Если ошибка, то null.
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
