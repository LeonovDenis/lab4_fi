/*
 * Приложение "Messager".
 * Интерфейс DemoJPA.java.
 * (C) Ю.Д.Заковряшин, 2020
 */
package jpa;

import java.util.List;
import javax.ejb.Local;

/**
 * Интерфейс для взаимодействия с базой данных на основе JPA.
 *
 * @author Ю.Д.Заковряшин, 2008-2020
 */
@Local
public interface DemoJPALocal {

    /**
     * Метод добавляет в базу данных новое сообщение от заданного пользователя.
     *
     * @param login Логин пользователя.
     * @param message Сообщение пользователя.
     * @return Возвращает значение true в случае успешной записи сообщения в
     * базу данных, false - в противном случае.
     */
    boolean addMessage(String login, String message);
    
    boolean addMessage(String login, Integer message);

    /**
     * Метод возвращает список сообщений, введённых ранее пользователем.
     *
     * @param login Логин пользователя.
     * @return Список ранее введённых сообщений пользователя или null в случае,
     * если такого пользователя нет.
     */
    List<Messages> getList(String login);
    long getSum(String login);
}
