package com.kychnoo.jdiary.TestsHelper;

import android.content.Context;

import com.kychnoo.jdiary.Database.DatabaseHelper;

public class TestsInitializator {

    private Context context;
    private DatabaseHelper databaseHelper;

    public TestsInitializator(Context context) {
        this.context = context;
        if(context != null) databaseHelper = new DatabaseHelper(context);
    }

    public void initializeAllTests() {
        testForFiveClasses();
    }

    public void testForFiveClasses() {
        if(!databaseHelper.isTestExists("История. Крестовые походы", "5А")) {
            long testId1 = databaseHelper.addTest("История. Крестовые походы", 5, 10, "5А");

            long q1Id = databaseHelper.addQuestion((int)testId1, "В каком году начались Первые крестовые походы?");
            databaseHelper.addAnswer((int)q1Id, "1096", true);
            databaseHelper.addAnswer((int)q1Id, "1066", false);
            databaseHelper.addAnswer((int)q1Id, "1204", false);

            long q2Id = databaseHelper.addQuestion((int)testId1, "Как назывался поход, завершившийся взятием Иерусалима?");
            databaseHelper.addAnswer((int)q2Id, "Первый крестовый поход", true);
            databaseHelper.addAnswer((int)q2Id, "Второй крестовый поход", false);
            databaseHelper.addAnswer((int)q2Id, "Третий крестовый поход", false);
            databaseHelper.addAnswer((int)q2Id, "Четвёртый крестовый поход", false);

            long q3Id = databaseHelper.addQuestion((int)testId1, "Кто возглавил крестовые походы на Востоке?");
            databaseHelper.addAnswer((int)q3Id, "Папа Урбан II", true);
            databaseHelper.addAnswer((int)q3Id, "Фридрих Барбаросса", false);
            databaseHelper.addAnswer((int)q3Id, "Ричард Львиное Сердце", false);

            long q4Id = databaseHelper.addQuestion((int)testId1, "Кто из этих лидеров участвовал в Первом крестовом походе?");
            databaseHelper.addAnswer((int)q4Id, "Готфрид Бульонский", true);
            databaseHelper.addAnswer((int)q4Id, "Чингисхан", false);
            databaseHelper.addAnswer((int)q4Id, "Наполеон", false);

            long q5Id = databaseHelper.addQuestion((int)testId1, "Что было основной целью крестовых походов?");
            databaseHelper.addAnswer((int)q5Id, "Освобождение Святых мест", true);
            databaseHelper.addAnswer((int)q5Id, "Завоевание новых земель", false);
            databaseHelper.addAnswer((int)q5Id, "Установление торговых связей", false);
            databaseHelper.addAnswer((int)q5Id, "Расширение империи", false);
        }
        if (!databaseHelper.isTestExists("Наука и технологии. Великие открытия", "5А")) {
            long testId = databaseHelper.addTest("Наука и технологии. Великие открытия", 6, 12, "5А");

            long q1Id = databaseHelper.addQuestion((int) testId, "Кто открыл закон всемирного тяготения?");
            databaseHelper.addAnswer((int) q1Id, "Исаак Ньютон", true);
            databaseHelper.addAnswer((int) q1Id, "Альберт Эйнштейн", false);
            databaseHelper.addAnswer((int) q1Id, "Галилео Галилей", false);

            long q2Id = databaseHelper.addQuestion((int) testId, "Какое изобретение принадлежит Николе Тесле?");
            databaseHelper.addAnswer((int) q2Id, "Телефон", false);
            databaseHelper.addAnswer((int) q2Id, "Переменный ток", true);
            databaseHelper.addAnswer((int) q2Id, "Лампа накаливания", false);

            long q3Id = databaseHelper.addQuestion((int) testId, "Как называется первая электронная вычислительная машина?");
            databaseHelper.addAnswer((int) q3Id, "ENIAC", true);
            databaseHelper.addAnswer((int) q3Id, "IBM PC", false);
            databaseHelper.addAnswer((int) q3Id, "Macintosh", false);

            long q4Id = databaseHelper.addQuestion((int) testId, "Кто является основоположником современной космонавтики?");
            databaseHelper.addAnswer((int) q4Id, "Юрий Гагарин", false);
            databaseHelper.addAnswer((int) q4Id, "Сергей Королёв", true);
            databaseHelper.addAnswer((int) q4Id, "Константин Циолковский", false);

            long q5Id = databaseHelper.addQuestion((int) testId, "Какое открытие сделал Александр Флеминг?");
            databaseHelper.addAnswer((int) q5Id, "Пенициллин", true);
            databaseHelper.addAnswer((int) q5Id, "Вакцину от оспы", false);
            databaseHelper.addAnswer((int) q5Id, "Инсулин", false);

            long q6Id = databaseHelper.addQuestion((int) testId, "Что из перечисленного является языком программирования?");
            databaseHelper.addAnswer((int) q6Id, "HTML", false);
            databaseHelper.addAnswer((int) q6Id, "Python", true);
            databaseHelper.addAnswer((int) q6Id, "CSS", false);
        }
        if(!databaseHelper.isTestExists("Математика. Алгебра", "5Б")) {
            long testId2 = databaseHelper.addTest("Математика. Алгебра", 4, 8, "5Б");

            long q1Id_math = databaseHelper.addQuestion((int)testId2, "Решите уравнение: 2x + 5 = 13.");
            databaseHelper.addAnswer((int)q1Id_math, "x = 4", true);
            databaseHelper.addAnswer((int)q1Id_math, "x = 5", false);
            databaseHelper.addAnswer((int)q1Id_math, "x = 3", false);

            long q2Id_math = databaseHelper.addQuestion((int)testId2, "Какой дискриминант у уравнения: x² - 4x + 3 = 0?");
            databaseHelper.addAnswer((int)q2Id_math, "D = 4", true);
            databaseHelper.addAnswer((int)q2Id_math, "D = 2", false);
            databaseHelper.addAnswer((int)q2Id_math, "D = 0", false);

            long q3Id_math = databaseHelper.addQuestion((int)testId2, "Результат выражения: 3² - 2*3 + 1 равен?");
            databaseHelper.addAnswer((int)q3Id_math, "4", true);
            databaseHelper.addAnswer((int)q3Id_math, "6", false);
            databaseHelper.addAnswer((int)q3Id_math, "2", false);
            databaseHelper.addAnswer((int)q3Id_math, "5", false);

            long q4Id_math = databaseHelper.addQuestion((int)testId2, "Как называется прямая, проходящая через центр окружности?");
            databaseHelper.addAnswer((int)q4Id_math, "Диаметр", true);
            databaseHelper.addAnswer((int)q4Id_math, "Хорда", false);
            databaseHelper.addAnswer((int)q4Id_math, "Тангенс", false);
        }
        if(!databaseHelper.isTestExists("Общество и культура. Всемирные традиции", "5В")) {
            long testId3 = databaseHelper.addTest("Общество и культура. Всемирные традиции", 6, 12, "5В");

            long q1Id_soc = databaseHelper.addQuestion((int)testId3, "Как называется японская традиция любования цветущей сакурой?");
            databaseHelper.addAnswer((int)q1Id_soc, "Ханами", true);
            databaseHelper.addAnswer((int)q1Id_soc, "Икебана", false);
            databaseHelper.addAnswer((int)q1Id_soc, "Каллиграфия", false);

            long q2Id_soc = databaseHelper.addQuestion((int)testId3, "Что символизирует немецкая традиция Октоберфест?");
            databaseHelper.addAnswer((int)q2Id_soc, "Празднование урожая", false);
            databaseHelper.addAnswer((int)q2Id_soc, "Праздник пива и баварской культуры", true);
            databaseHelper.addAnswer((int)q2Id_soc, "День reunification", false);

            long q3Id_soc = databaseHelper.addQuestion((int)testId3, "Назовите традиционный мексиканский праздник, отмечаемый 1-2 ноября.");
            databaseHelper.addAnswer((int)q3Id_soc, "День Мёртвых", true);
            databaseHelper.addAnswer((int)q3Id_soc, "День независимости", false);
            databaseHelper.addAnswer((int)q3Id_soc, "Фиеста де ла Санация", false);

            long q4Id_soc = databaseHelper.addQuestion((int)testId3, "Кто из следующих писателей является классиком мировой литературы?");
            databaseHelper.addAnswer((int)q4Id_soc, "Шекспир", true);
            databaseHelper.addAnswer((int)q4Id_soc, "Достоевский", false);
            databaseHelper.addAnswer((int)q4Id_soc, "Толстой", false);

            long q5Id_soc = databaseHelper.addQuestion((int)testId3, "Что из перечисленного является древней цивилизацией?");
            databaseHelper.addAnswer((int)q5Id_soc, "Месопотамия", true);
            databaseHelper.addAnswer((int)q5Id_soc, "Рим", false);
            databaseHelper.addAnswer((int)q5Id_soc, "Древний Египет", true);
            databaseHelper.addAnswer((int)q5Id_soc, "Древняя Индия", true);

            long q6Id_soc = databaseHelper.addQuestion((int)testId3, "Как называется праздник, отмечаемый в Индии в честь победы добра над злом?");
            databaseHelper.addAnswer((int)q6Id_soc, "Дивали", true);
            databaseHelper.addAnswer((int)q6Id_soc, "Холи", false);
            databaseHelper.addAnswer((int)q6Id_soc, "Наваратри", false);
        }
    }

}
