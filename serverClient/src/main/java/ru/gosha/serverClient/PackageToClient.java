/*
Файл указывает правило, какие данные будут переданы клиенту.
 */

package ru.gosha.serverClient;

import java.io.*;

public class PackageToClient implements Serializable {

    /**
     * Тут содержатся файл .iCal.
     */
    public final byte[] CalFile;

    /**
     * Тут содержится количество созданных мероприятий.
     */
    public final int Count;

    /**
     * Тут содержится сообщение от сервера. Например: "Найдено несколько преподавателей с этим именем: Иванов И.И. и Иванов И.А."
     */
    public final String Messages;

    /**
    Строит данные отправляемые на клиент.
     @param CalFile Тут содержатся файл .iCal.
     @param Count Тут содержится количество созданных мероприятий.
     */
    public PackageToClient(byte[] CalFile, int Count, String Messages) {
        this.CalFile = CalFile;
        this.Count = Count;
        this.Messages = Messages;
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
     * @return Представление хранилища в классе PackageToClient. Если ошибка, то null.
     * @throws ClassNotFoundException Данные не истинные.
     */
    public static PackageToClient fromByteArray(byte[] input) throws ClassNotFoundException {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(input);
            ObjectInputStream inObj = new ObjectInputStream(in);
            PackageToClient out = (PackageToClient) inObj.readObject();
            inObj.close();
            return out;
        } catch(IOException error) {
            return null;
        }
    }
}
