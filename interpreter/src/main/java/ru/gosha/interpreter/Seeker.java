/*
[SG]Muwa Михаил Павлович Сидоренко. 2018.
Файл представляет из себя хранилище запроса.
Суть запроса: имя искателя, тип искателя (преподаватель, студент), дата начала и конца семестра, адрес по-умолчанию.
 */

package ru.gosha.interpreter;

import java.io.Serializable;
import java.time.LocalDate;

public class Seeker implements Serializable {
    /**
     * Имя искателя.
     */
    public final String nameOfSeeker;
    /**
     * Тип искателя. Преподаватель или студент группы?
     */
    public final SeekerType seekerType;
    /**
     * Начало семестра.
     */
    public final LocalDate dateStart;
    /**
     * Конец семестра.
     */
    public final LocalDate dateFinish;
    /**
     * Часовой пояс, в котором указано время в расписании. Указывается в секундах.
     */
    public final int timezone;
    /**
     * Адрес кампуса по-умолчанию.
     */
    public final String defaultAddress;

    //public List<Couple> Couples = new LinkedList<>(); Нельзя здесь зависимость от Couple.

    /**
     * Создаёт экземпляр запроса.
     * @param nameOfSeeker Имя искателя. Это либо имя преподавателя, либо имя студента.
     * @param seekerType Тип искателя. Это либо преподаватель, либо студент.
     * @param dateStart Дата начала составления расписания. С какого календарного дня надо составлять расписание? Дата указывается по местному времени.
     * @param dateFinish Дата конца составления расписания. До какого календарного дня надо составлять расписание? Дата указывается по местному времени.
     * @param timezone Часовой пояс, в котором указано время в расписании. Указывается в секундах. Например, UTC+3:00 указывается как +3 * 60 * 60. Пользователя надо обязательно спросить, переходит ли на зимнее/летнее часы! Если да, то хитро манипулировать датами, чтобы часовой пояс был верен.
     * @param defaultAddress Какой адрес корпуса по-умолчанию?
     */
    public Seeker(String nameOfSeeker, SeekerType seekerType, LocalDate dateStart, LocalDate dateFinish, int timezone, String defaultAddress) {
        this.nameOfSeeker = nameOfSeeker;
        this.seekerType = seekerType;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.timezone = timezone;
        this.defaultAddress = defaultAddress;
    }

    @Override
    public boolean equals(Object ex) {
        if (this == ex) {
            return true;
        }
        if(ex instanceof Seeker) {
            Seeker e = (Seeker) ex;
            return
                    nameOfSeeker.equals(e.nameOfSeeker) &&
                            seekerType.equals(e.seekerType) &&
                            dateStart.equals(e.dateStart) &&
                            dateFinish.equals(e.dateFinish) &&
                            timezone == e.timezone &&
                            defaultAddress.equals(e.defaultAddress);
        }
        return false;
    }
}
