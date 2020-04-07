/*
 * Приложение "Messager".
 * Интерфейс DemoJPA.java.
 * (C) Ю.Д.Заковряшин, 2020
 */
package jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "MESSAGES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Messages.findAll", query = "SELECT m FROM Messages m")
    , @NamedQuery(name = "Messages.findById", query = "SELECT m FROM Messages m WHERE m.id = :id")
    , @NamedQuery(name = "Messages.findByMessage", query = "SELECT m FROM Messages m WHERE m.message = :message")})
public class Messages implements Serializable {

    /**
     * Уникальный идентификатор сериализуемого класса. Может использоваться при
     * операциях сериализации и десериализации объектов данного класса.
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Избыточная аннотация, которая может приводить к возникновению исключения на 
//    стадии выполнения, поскольку она дублирует описание поля в аннотации @Column.
//    @Basic (optional=false)
    @Column(name = "ID", nullable = false)
    private Integer id;
//    Также избыточная аннотация (см.комментарий к полю id.
//    @Size(max = 255)
    @Column(name = "MESSAGE", length = 255)
    private String message;
    @JoinColumn(name = "LOGIN", referencedColumnName = "LOGIN")
    @ManyToOne
    private Users login;

    /**
     * Конструктор по умолчанию.
     */
    public Messages() {
    }

    /**
     * Конструктор с параметром id, устанавливающий идентификатор объекта
     * (первичный ключ).
     *
     * @param id Идентификатор сообщения.
     */
    public Messages(Integer id) {
        this(id, null, null);
    }

    /**
     * Конструктор с параметрами, устанавливающий пользователя - автора
     * сообщения и текст сообщения.
     *
     * @param user Ссылка на объект, определяющий автора сообщения.
     * @param message Сообщение пользователя.
     */
    public Messages(Users user, String message) {
        this(null, user, message);
    }

    /**
     * Наиболее "полный" конструктор класс, устанавливающий все атрибуты
     * сообщения: идентификатор сообщения, пользователя - автора сообщения и
     * текст сообщения.
     *
     * @param id Идентификатор сообщения.
     * @param user Ссылка на объект, определяющий автора сообщения.
     * @param message Сообщение пользователя.
     */
    public Messages(Integer id, Users user, String message) {
        this.login = user;
        this.message = message;
    }

    /**
     * Метод возвращает идентификатор сообщения.
     *
     * @return Идентификатор сообщения.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Метод определяет идентификатор сообщения.
     *
     * @param id Идентификатор сообщения.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Метод возвращает текст сообщения пользователя.
     *
     * @return Текст сообщения пользователя.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Метод определяет текст сообщения.
     *
     * @param message Текст сообщения.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Метод возвращает идентификатор пользователя.
     *
     * @return Идентификатор пользователя.
     */
    public Users getLogin() {
        return login;
    }

    /**
     * Метод определяет идентификатор пользователя.
     *
     * @param login Идентификатор пользователя.
     */
    public void setLogin(Users login) {
        this.login = login;
    }

    /**
     * Переопределение стандартного метода hashCode() класса Object.
     *
     * @return хеш-код объекта.
     */
    @Override
    public int hashCode() {
        return id | ((message != null) ? message.hashCode() : 0)
                | ((login != null) ? login.hashCode() : 0);
    }

    /**
     * Переопределение стандартного метода equals() класса Object.
     *
     * @param object Ссылка на объект, с которым выполняется сравнение текущего
     * объекта.
     * @return Метод возвращает значение true, если первичные ключи обоих
     * объектов совпадают, что возможно только в случае сравнения объекта с
     * самим собой. В остальных случаях метод возвращает значение false.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Messages)) {
            return false;
        }
        Messages other = (Messages) object;
        return this.id != null && other.id != null && this.id.equals(other.id);
    }

    /**
     * Метод переопределяет стандартный метод класса Object.
     *
     * @return Строковое представление объекта.
     */
    @Override
    public String toString() {
        return "jpa.Messages[ id=" + id + " ]";
    }

}
