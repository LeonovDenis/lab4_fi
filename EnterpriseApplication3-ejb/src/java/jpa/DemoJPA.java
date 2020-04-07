/*
 * Приложение "Messager".
 * Класс DemoJPA.java.
 * (C) Ю.Д.Заковряшин, 2020
 */
package jpa;

import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

/**
 * Класс демонстрирует основы работы с базой данных с использованием JPA.
 *
 * @author Ю.Д.Заковряшин, 2008-2020
 */
@Singleton
public class DemoJPA implements DemoJPALocal {

    /**
     * Стандартные сообщения пользователю.
     */
    public static final String OK = "Entity Manager готов к работе";
    public static final String ERROR = "Ошибка: {0}!";

    /**
     * Ссылка на объект типа
     * {@link javax.persistence.EntityManager EntityManager}, представляющий
     * контекст JPA. В данном примере используется объект, управляемый
     * контейнером.
     */
    @PersistenceContext
    EntityManager em;

    /**
     * Ссылка на объект, обеспечивающий протоколирование работы класса.
     */
    private static Logger log;

    /**
     * Метод, вызываемый контейнером сразу после создания объекта этого типа.
     */
    @PostConstruct
    public void init() {
        log = Logger.getLogger("Messager");
        // Протоколирование проверки доступности контекста.
        if (em != null) {
            log.log(Level.INFO, OK);
        } else {
            log.log(Level.SEVERE, ERROR, " Entity Manager равен null.");
        }
    }

    /**
     * Метод заносит сообщение в базу данных.
     *
     * @param login Логин пользователя.
     * @param message Сообщение пользователя.
     * @return Значение true в случае, если сообщение было успешно занесено в
     * базу данных, false - в противном случае.
     */
    @Override
    public boolean addMessage(String login, String message) {
        if (login == null || login.trim().isEmpty() || message == null) {
            return false;
        }
        try {
            // Поиск записи по логину пользователя.
            Users u = em.find(Users.class, login);
            if (u == null) {
                // Если запись не найдена, то создаётся новый объект типа Users.
                u = new Users(login);
            }
            // Создание нового объекта с текстом сообщения.
            Messages m = new Messages(u, message);
            // Добавление сообщения к списку сообщений пользователя.
            u.getMessagesList().add(m);
            // Добавление сообщения и пользователя к контексту EntityManager
            if (!em.contains(u)) {
                em.persist(u);
            }
            em.persist(m);
            // Обновление записей в базе данных.
            em.merge(m);
            em.merge(u);
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, ERROR, e.getMessage());
            return false;
        }
    }

    /**
     * Метод возвращает список сообщений заданного пользователя.
     *
     * @param login Логин пользователя.
     * @return Список ранее введённых сообщений пользователя или null в случае,
     * если такого пользователя нет.
     */
    @Override
    public List<Messages> getList(String login) {
        // Поиск записи по логину пользователя.
        Users u = em.find(Users.class, login);
        // Если пользователь найден, то возвращается список его сообщений, в 
        // противном случае возвращается значение null
        return u != null ? u.getMessagesList() : null;
    }

    @Override
    public long getSum(String login) {
        // Поиск записи по логину пользователя.
        Users u = em.find(Users.class, login);
        // Если пользователь найден, то возвращается список его сообщений, в 
        // противном случае возвращается значение null
        long summ = 0;
        if (u != null) {
            List<Score> scoreList = u.getScoreList();
            for (Score s : scoreList) {
                Integer message = s.getMessage();
                summ += message;
            }
        }
        return summ;
    }

    @Override
    public boolean addMessage(String login, Integer message) {

        if (login == null || login.trim().isEmpty() || message == null) {
            return false;
        }
        try {
            // Поиск записи по логину пользователя.
            Users u = em.find(Users.class, login);
            if (u == null) {
                // Если запись не найдена, то создаётся новый объект типа Users.
                u = new Users(login);
            }
            // Создание нового объекта с текстом сообщения.
            Score s = new Score(u, message);
            // Добавление сообщения к списку сообщений пользователя.
            u.getScoreList().add(s);
            // Добавление сообщения и пользователя к контексту EntityManager
            if (!em.contains(u)) {
                em.persist(u);
            }
            em.persist(s);
            // Обновление записей в базе данных.
            em.merge(s);
            em.merge(u);
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, ERROR, e.getMessage());
            return false;
        }
    }
}
