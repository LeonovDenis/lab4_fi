/*
 * Приложение "Messager".
 * Класс Messager.java.
 * (C) Ю.Д.Заковряшин, 2020
 */
package jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Класс типа {@link javax.persistence.Entity Entity} представляет одну запись
 * из таблицы сообщений Users.
 *
 * @author Ю.Д.Заковряшин, 2008-2020
 */
@Entity
@Table(name = "USERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findByLogin", query = "SELECT u FROM Users u WHERE u.login = :login")})
public class Users implements Serializable {

    /**
     * Уникальный идентификатор сериализуемого класса. Может использоваться при
     * операциях сериализации и десериализации объектов данного класса.
     */
    private static final long serialVersionUID = 1L;
    @Id
//    Избыточные аннотации, которые могут приводить к возникновению исключения на 
//    стадии выполнения, поскольку они дублируют свойства поля, определённые в 
//    аннотации @Column.
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 8)
    @Column(name = "LOGIN", length = 8)
    private String login;
    @OneToMany(mappedBy = "login")
    private List<Messages> messagesList;
    @OneToMany(mappedBy = "login")
    private List<Score> scoreList;

    /**
     * Конструктор по умолчанию.
     */
    public Users() {
        this(null, null,null);
    }

    /**
     * Конструктор с параметром login, устанавливающий логин
     * (идентификатор/первичный ключ) пользователя .
     *
     * @param login Задаваемый логин (идентификатор/первичный ключ)
     * пользователя.
     */
    public Users(String login) {
        this(login, null,null);
    }
    

    /**
     * Основной конструктор конструктор класса с параметрами login и message,
     * который устанавливает логин пользователя и сообщение пользователя
     * (первичный ключ).
     *
     * @param login Логин (идентификатор) пользователя.
     * @param message Сообщение пользователя.
     */
    public Users(String login, String message,Integer number) {
        this.login = login;
        messagesList = new ArrayList<>();
        scoreList= new ArrayList<>();
        if (message != null) {
            messagesList.add(new Messages(this, message));
        }
        if (number != null) {
            scoreList.add(new Score(this, number));
        }
    }
    public Users(String login, String message) {
        this(login, message,null);
        
    }
    public Users(String login, Integer number) {
        this(login, null,number);
        
    }

    /**
     * Метод возвращает логин (идентификатор) пользователя.
     *
     * @return Логин (идентификатор) пользователя.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Метод определяет логин (идентификатор) пользователя.
     *
     * @param login Логин (идентификатор) пользователя.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Метод возвращает список сообщений пользователя.
     *
     * @return Список сообщений пользователя.
     */
    @XmlTransient
    public List<Messages> getMessagesList() {
        return messagesList;
    }

    /**
     * Метод определяет коллекцию типа {@link java.util.List List} сообщений
     * пользователя.
     *
     * @param messagesList Список сообщений пользователя.
     */
    public void setMessagesList(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }

    /**
     * Переопределение стандартного метода hashCode() класса Object.
     *
     * @return хеш-код объекта.
     */
    @Override
    public int hashCode() {
        return (login != null ? login.hashCode() : 0);
    }

    /**
     * Переопределение стандартного метода equals() класса Object.
     *
     * @param object Ссылка на объект, с которым сравнивается данных объект.
     * @return Метод возвращает значение true, если первичные ключи обоих
     * объектов совпадают, что возможно только в случае сравнения объекта с
     * самим собой. В остальных случаях метод возвращает значение false.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Users)) {
            return false;
        }
        return this.login.equals(((Users) object).login);
    }

    /**
     * Метод переопределяет стандартный метод класса Object.
     *
     * @return Строковое представление объекта.
     */
    @Override
    public String toString() {
        return "jpa.Users[ login=" + login + " ]";
    }

    @XmlTransient
    public List<Score> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Score> scoreList) {
        this.scoreList = scoreList;
    }
}
