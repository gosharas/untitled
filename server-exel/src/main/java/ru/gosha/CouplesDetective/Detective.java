package ru.gosha.CouplesDetective;

/*
План детектива:
0. Узнаю список времён: Начала и окончания пар.

Цикл:
1. Получаю список за день.
2. Анализирую, является ли день днём самостоятельных занятий.
3. Узнаю, по какому адресу занимаются в этот день.
4. Добавляю в исключения строки с адресами.
5. Разбиваю по двум строкам в GetCouplesByPeriod. Исключающие строки отправляются как "".

 */

import ru.gosha.CouplesDetective.xl.ExcelFileInterface;
import ru.gosha.serverClient.Seeker;
import ru.gosha.serverClient.SeekerType;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

// Класс, который занимается поиском расписания.
public class Detective {

    /**
     * Функция ищет занятия для seeker в файлах files.
     * @param seeker критерий поиска.
     * @param files список файлов, в которых требуется искать пары занятий.
     * @throws DetectiveException Появилась проблема, связанная с обработкой Excel файла
     * @throws IOException Во время работы с Excel file - файл стал недоступен.
     */
    public static List<Couple> startAnInvestigations(Seeker seeker, Iterable<ExcelFileInterface> files) throws DetectiveException, IOException {
        List<Couple> output = new LinkedList<>();
        int index = 0;
        for (ExcelFileInterface f : files)
            try {
                output.addAll(startAnInvestigation(seeker, f));
                index++;
            } catch (DetectiveException error) {
                throw new DetectiveException("Ошибка в файле с индексом " + index + ":" + error.getMessage());
            }
        return output;
    }


    /**
     * Функция ищет занятия для seeker в файле File.
     * @param seeker критерий поиска.
     * @param file файл, в котором требуется искать пары занятий.
     * @throws DetectiveException Появилась проблема, связанная с обработкой Excel файла
     * @throws IOException Во время работы с Excel file - файл стал недоступен.
     */
    public static List<Couple> startAnInvestigation(Seeker seeker, ExcelFileInterface file) throws DetectiveException, IOException {
        List<Couple> output = new LinkedList<>();
        Point WeekPositionFirst = SeekEverythingInLeftUp("Неделя", file);
        List<Point> IgnoresCoupleTitle = new LinkedList<>();
        int[] Times = GetTimes(WeekPositionFirst, file); // Узнать время начала и конца пар.
        int CountCouples = Times.length / 2; // Узнать количество пар за день.
        Point basePos;
        try {
            basePos = SeekFirstCouple(file); // Позиция первой записи "Предмет".
            // Обычно на R3C6.
        } catch (DetectiveException e) {
            System.out.println(e.getMessage());
            System.err.println(e.getMessage());
            return new LinkedList<>(); // Нет пар в этом листе.
        }
        // Ура! Мы нашли базовую позицию!
        for(
                int lastEC = 10, // Last entry count - Счётчик последней записи.
                posEntryX = basePos.x; // Это позиция записи. Указывает столбец, где есть запись "Предмет".

                lastEC != 0;

                lastEC--, posEntryX--
                )
        {
            if(file.getCellData(posEntryX, basePos.y).equals("Предмет"))
                for(int DayOfTheWeek = 1; DayOfTheWeek <= 7; DayOfTheWeek++) {
                    // Выставляем курсор на название первой пары дня.
                    Point cursor = new Point(posEntryX, basePos.y + 1 + DayOfTheWeek * CountCouples);
                    if(IsDayFree(cursor, CountCouples, IgnoresCoupleTitle, file)) continue; // Если день свободен, то ничего не добавляем.
                    String Address = GetAddressOfDay(cursor, CountCouples, seeker.defaultAddress, IgnoresCoupleTitle, file);
                    return FilterCouplesBySeekerType(
                            GetCouplesFromAnchor(posEntryX, basePos.y, seeker, Times, IgnoresCoupleTitle, file) /* Хорошо! Мы получили список занятий у группы. Если это группа - то просто добавить, если это преподаватель - то отфильтровать. */,
                            seeker
                    );
                }
        }
        return new LinkedList<>();
    }

    /**
     * Фильтрует пары по типу запроса.
     * @param couples Список пар.
     * @param seeker Критерий (Тип искателя и его название)
     * @return Отфильтрованный по критерию.
     */
    private static List<Couple> FilterCouplesBySeekerType(Collection<? extends Couple> couples, final Seeker seeker) {
        List<Couple> output = new LinkedList<>();
        for (Couple i : couples) {
            if (seeker.seekerType == SeekerType.StudyGroup) {
                if (i.NameOfGroup.toLowerCase().equals(seeker.nameOfSeeker.toLowerCase())) {
                    output.add(i);
                }
            } else {
                if (i.NameOfTeacher.toLowerCase().equals(seeker.nameOfSeeker.toLowerCase())) {
                    output.add(i);
                }
            }
        }
        return output;
    }

    /**
     * Отвечает на вопрос, является ли день свободным.
     * @param titleOfDay Позиция названия первой пары дня.
     * @param CountCouples Количество пар в дне.
     * @param IgnoresCoupleTitle Список адресов заголовков, которые следует игнорировать. Идёт только добавление элементов в список.
     * @param file Файл, откуда надо считывать данные.
     * @return Истина, если данный день является днём самостоятельных работ, или же если в дне нет записей. Иначе: False.
     */
    private static boolean IsDayFree(Point titleOfDay, int CountCouples, List<Point> IgnoresCoupleTitle, ExcelFileInterface file) throws IOException {
        for(int i = titleOfDay.y; i < titleOfDay.y + CountCouples*2; i++) {
            if(!file.getCellData(titleOfDay.x, i).isEmpty())
            {
                if(file.getCellData(titleOfDay.x, i).equals("День")) // Мы уже на второй строке встретим слово "День". Даю эту отпимизацию на то, что формат обозначения День самтостятельных занятий не изменится.
                {
                    for(; i < titleOfDay.y + CountCouples*2; i++)
                        IgnoresCoupleTitle.add(new Point(titleOfDay.x, i));
                    return true;
                }
                else return false; // Если встретили что-то другое, то это что-то не то! Вряд ли день самостоятельных занятий!
            }
            IgnoresCoupleTitle.add(new Point(titleOfDay.x, i));
        } // Опа! Все пары - пусты!
        return true;
    }

    /**
     * Функция узнаёт, по какому адресу занимаются.
     * @param titleOfDay Позиция названия первой пары дня.
     * @param CountCouples Количество пар в дне.
     * @param DefaultAddress Адрес по-умолчанию за день.
     * @param IgnoresCoupleTitle Список адресов заголовков, которые следует игнорировать. Идёт только добавление элементов в список.
     * @param file Файл, откуда надо считывать данные.
     * @return Адрес местоположения пары.
     */
    private static String GetAddressOfDay(Point titleOfDay, int CountCouples, String DefaultAddress, List<Point> IgnoresCoupleTitle, ExcelFileInterface file) throws IOException {
        String output = DefaultAddress; // Если никакой не найдётся, будет defaultAddress.
        for(int y = titleOfDay.y; y < titleOfDay.y + CountCouples*2; y++)
            if(file.getCellData(titleOfDay.x, y).trim().equals("Занятия по адресу:")) {
                output = file.getCellData(titleOfDay.x, y + 1);
                IgnoresCoupleTitle.add(new Point(titleOfDay.x, y));
                IgnoresCoupleTitle.add(new Point(titleOfDay.x, y + 1));
            }
        return output;
    }

    /**
     * Ищет в регионе 20x10 слово "Предмет" и возвращает его координаты.
     * Слово "Предмет" символизирует единицу расписания для группы.
     * Если слово "Предмет" не найдено, то фунуция вернёт {@code null}.
     * @param file Файл, в котором следует искать.
     * @return Координаты первого найденного слова "Предмет".
     */
    private static Point SeekFirstCouple(ExcelFileInterface file) throws DetectiveException {
        try {
            return SeekEverythingInLeftUp("Предмет", file);
        }
        catch (Exception e){
            throw new DetectiveException(e.getMessage() + "\nНевозможно найти хотя бы один предмет в таблице Excel.");
        }
    }

    /**
     * Ищет в регионе 20x10 заданную фразу и возвращает координаты.
     * @param Word Слово, которое следует искать.
     * @param file Файл, в котором требуется искать.
     * @return Координаты первого найденного слова "Предмет".
     * @throws DetectiveException Упс! Не нашёл!
     */
    private static Point SeekEverythingInLeftUp(String Word, ExcelFileInterface file) throws DetectiveException, IOException {
        for(int y = 1; y <= 10; y++)
            for(int x = 1; x <= 20; x++)
                if(file.getCellData(x, y).equals(Word)) return new Point(x, y);
        throw new DetectiveException("Невозможно найти заданное слово Word. Word = " + Word);
    }

    /**
     * Идёт считывание данных о предметах с определённого положения "Предмет".
     * @param column Столбец, где находится "Якорь" то есть ячейка с записью "Предмет".
     * @param row Строка, где находится "Якорь" то есть ячейка с записью "Предмет".
     * @param seeker Отсюда берётся начало и конец семестра.
     * @param times Отсюда берётся расписание времени в формате началок - конец. На все пары.
     * @param ignoresCoupleTitle Лист занятий, который надо игнорировать.
     * @param file Файл, откуда надо производить чтение.
     * @return Множество занятий у группы.
     */
    private static Collection<? extends Couple> GetCouplesFromAnchor(int column, int row, Seeker seeker, int[] times, List<Point> ignoresCoupleTitle, ExcelFileInterface file) throws IOException {
        LinkedList<Couple> coupleOfWeek = new LinkedList<>();
        int countOfCouples = times.length / 2;
        String nameOfGroup = file.getCellData(column, row - 1).trim();
        for(byte dayOfWeek = 1; dayOfWeek <= 7; dayOfWeek++)
        {
            int c = column;
            int r = (row + 1) + (dayOfWeek - 1) * countOfCouples * 2;
            coupleOfWeek.addAll(
                    GetCouplesFromDay
                            (c, r, nameOfGroup, dayOfWeek, seeker, ignoresCoupleTitle, times,
                                    GetAddressOfDay
                                            (new Point(c, r), countOfCouples, seeker.defaultAddress, ignoresCoupleTitle, file
                                            ),
                                    file
                            )
            );
        }
        return coupleOfWeek;
    }

    /**
     * Идёт считывание данных о предметах с определённого положения: первая пара в дне.
     * @param column Столбец, где находится "Якорь" то есть ячейка с записью "Предмет".
     * @param row Строка, где находится "Якорь" то есть ячейка с записью "Предмет".
     * @param nameOfGroup Имя группы.
     * @param dayOfWeek День недели.
     * @param seeker Отсюда берётся начало и конец семестра.
     * @param ignoresCoupleTitle Лист занятий, который надо игнорировать.
     * @param times Лист с началом и окончанием пары. {Начало1пары, конец1пары, начало2пары, конец2пары, ...}
     * @param address Адрес, где находятся пары
     * @param file Файл, откуда надо производить чтение.
     * @return Множество занятий у группы в конкретный день.
     */
    private static Collection<? extends Couple> GetCouplesFromDay(int column, int row, String nameOfGroup, byte dayOfWeek, Seeker seeker, List<Point> ignoresCoupleTitle, int[] times, String address, ExcelFileInterface file) throws IOException {
        LinkedList<Couple> coupleOfDay = new LinkedList<>();
        int countOfCouples = times.length / 2;
        for(Point cursor = new Point(column, row); cursor.y < row + countOfCouples*2; cursor.y++) { // Считываем каждую строчку
            if (IsEqualsInList(ignoresCoupleTitle, cursor))
                continue; // Если такая запись под игнором, то игнорируем, ничего не делаем.
            String[] titles = file.getCellData(cursor.x, cursor.y).trim().split("\r\n|\n"); // Регулярное выражение. Делать новую строку либо от \r\n либо от \n. Универсально!
            String[] typeOfLessons = file.getCellData(cursor.x + 1, cursor.y).trim().split("\r\n|\n");
            String[] teachers = file.getCellData(cursor.x + 2, cursor.y).trim().split(("\r\n|\n"));
            String[] audiences = file.getCellData(cursor.x + 3, cursor.y).trim().split(("\r\n|\n"));
            for (int indexInLine = 0; indexInLine < titles.length; indexInLine++) {
                Iterable<Couple> listCouplesOfLine = Couple.GetCouplesByPeriod(seeker.dateStart,
                        seeker.dateFinish,
                        times,
                        dayOfWeek,
                        nameOfGroup,
                        titles[indexInLine],
                        indexInLine < typeOfLessons.length ? typeOfLessons[indexInLine] : typeOfLessons[0],
                        indexInLine < teachers.length ? teachers[indexInLine] : teachers[0],
                        indexInLine < audiences.length ? audiences[indexInLine] : audiences[0],
                        address,
                        cursor.y - row);
                if(listCouplesOfLine != null) for (Couple coup : listCouplesOfLine)
                    coupleOfDay.add(coup); // Все пары, которые вернусь с GetCouplesByPeriod надо добавить в наш список.
            }
        }
        return coupleOfDay;
    }


    /**
     * Отвечает на вопрос, содержится ли элемент в списке. Сравнивается с помощью equals.
     * @param list Список, в котором требуется проверить наличие этого объекта.
     * @param reference Объект, который является эталоном для поиска.
     * @param <T> Произвольный тип данных. Желаетльно, чтобы equals был определён.
     * @return {@code True}, если в {@code list} есть содержание элемента {@code reference}. Иначе - {@code False}.
     */
    private static <T> boolean IsEqualsInList(Iterable<T> list, T reference){
        for(T v : list)
            if(v.equals(reference)) return true;
        return false;
    }


    /**
     * Говорит, сколько пар в одном дне.
     * @param CR Координаты фразы "Неделя".
     * @param file Файл, в котором надо подсчитать количество пар.
     * @return Количество пар в одном дне недели.
     * @throws DetectiveException
     */
    private static int GetCountCoupleInDay(Point CR, ExcelFileInterface file) throws DetectiveException, IOException {
        int OldNumber = Integer.MIN_VALUE; // Последнее число, которое было прочитано.
        int output = 0;
        int x = CR.x - 3; // Остаёмся на одном и том же столбце!
        for (int y = CR.y + 1; y < CR.y + 100; y++) { // Движемся вниз по строкам!
            if (!IsStringNumber(file.getCellData(x, y))) {// Если это не число
                throw new DetectiveException("Ошибка при поиске порядковых номеров пар. На ячейке R3C5 должно быть написано \"Неделя\" а в ячейке R4C2 требуется порядковый номер пары.");
            } else { // Если мы движемся правильно в сторону прочтения порядковых номеров (нам попадаются только числа), то
                if (file.getCellData(x, y).isEmpty()) continue;
                output++;
                int NewNumber = Integer.parseInt(file.getCellData(x, y), 10);
                if (NewNumber <= OldNumber) // Хоба! В резуьтате движения вних мы нашли тот момент, когда было 5... 6... и вот! 1!!!
                    return output; // Тут надо записывать по сути КОЛИЧЕСТВО_ПАР * 2. Судя по расписанию, номер пары соответсвует количеству пройденых пар, и если мы нашли номер последней пары, то знаем и количество пар. Это NewNumber.
                // Иначе продолжаем. continue;
            }
        }
        return 0;
    }

    /**
     * Функция, которая извлекает из указанного участка Excel файла времена начала и конца пар в минутах.
     * @param CR Столбец и строка, где находится якорь. Якорём является самая первая запись "Неделя".
     * @param file Excel файл.
     * @return Возвращает список времён в формате минут: {начало пары, конец пары}.
     */
    private static int[] GetTimes(Point CR, ExcelFileInterface file) throws  DetectiveException, IOException {
        int[] output = new int[2 * GetCountCoupleInDay(CR, file)];
        if(output.length == 0)
            throw new DetectiveException("Ошибка при поиске время начала и конца пар -> Пока программа спускалась вниз по строкам, считая, сколько пар в одном дне, она прошла окола 100 строк и сказала идити вы все, я столько не хочу обрабатывать.");
        // Ура, мы знаем количество. Это output.length. Теперь можно считывать времена.
        int indexArray = 0;
        for(int y = CR.y + 1; y <= CR.y + 1 + output.length*2; y+=2)
            for(int x = CR.x - 2; x <= CR.x - 1; x++)
                // Заполняем масив временем.
                output[indexArray++] = GetMinutesFromTimeString(file.getCellData(x, y));
        return output;
    }

    /**
     * Конвектирует время в формате HH-MM или HH:MM в минуты.
     * @param inputT Входящее время в строковом представлении.
     * @return Возвращает время в минутах.
     */
    private static int GetMinutesFromTimeString(String inputT) {
        if(inputT == null || inputT.isEmpty()) return 0;
        String[] HHMM = inputT.trim().split("-");
        if(HHMM.length == 1) HHMM = inputT.trim().split(":");
        return Integer.parseInt(HHMM[0]) * 60 + Integer.parseInt(HHMM[1]);
    }

    /**
     * Возвращает ответ, может ли являться текст записью десятичного числа.
     * @param input Входной текст, который следует проверить.
     * @return {@code True}, если в input записано число или же строка пуста, иначе - {@code False}.
     */
    private static boolean IsStringNumber(String input) {
        char[] buffer = input.trim().toCharArray();
        for(char a : buffer)
            if(a > '9' || a < '0') return false;
        return true;
    }
}
