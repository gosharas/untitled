// TODO: Надо доделать 1 функцию.

package ru.gosha.SG_Muwa;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Seeker {
    public final String NameOfSeeker;
    public final SeekerType seekerType;
    /**
     * Начало семестра.
     */
    public final Date dateStart;
    /**
     * Конец семестра.
     */
    public final Date dateFinish;
    /**
     * Адрес кампуса по-умолчанию.
     */
    public final String DefaultAddress;
    public List<Couple> Couples = new LinkedList<>();

    public Seeker(String nameOfSeeker, SeekerType seekerType, Date dateStart, Date dateFinish, String defaultAddress) {
        NameOfSeeker = nameOfSeeker;
        this.seekerType = seekerType;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        DefaultAddress = defaultAddress;
    }
}
