package ru.gosha.interpreter;

import java.io.*;

public abstract class Package implements Serializable {

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
}
