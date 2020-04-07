/*
 * Приложение "Messager".
 * Класс Messager.java.
 * (C) Ю.Д.Заковряшин, 2020
 */
package jms;

import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import jpa.DemoJPALocal;
import java.util.logging.Logger;
import javax.jms.TextMessage;

/**
 * Класс, демонстрирующий простую реализацию компонента, управляемого
 * сообщениями (Message Driven Bean - MDB). Данный компонент получает сообщение
 * вида "ключ-значение" ({@link javax.jms.MapMessage MapMessage}) от сервлета и
 * передаёт его компоненту типа {@link javax.ejb.Singleton Singleton} для
 * сохранения в базе данных. Параметры аннотации данного класса определяют тип и
 * JNDI имя очереди, сообщения из которой будут передаваться объектам данного
 * типа для дальнейшей обработки.
 *
 * @author Ю.Д.Заковряшин, 2008-2020
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(
            // JNDI имя очереди
            propertyName = "destinationLookup", propertyValue = "jms/DemoTopic")
    ,
        @ActivationConfigProperty(
            // Тип очереди
            propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class DemoMessageBean implements MessageListener {

    /**
     * Ссылка на компонент типа {@link javax.ejb.Singleton Singleton}, который
     * должен сохранить сообщение в базе данных.
     */
    @EJB
    private DemoJPALocal jpa;

    /**
     * Ссылка объект, протоколирующий работу класса.
     */
    private static Logger log;

    /**
     * Конструктор по умолчанию, его можно явно не определять.
     */
    public DemoMessageBean() {
    }

    /**
     * Метод, вызываемый после создания объекта данного класса.
     */
    @PostConstruct
    public void init() {
        log = Logger.getLogger("Messager");
        log.log(Level.INFO, "MDB DemoMessageBean created ({0})...", hashCode());
    }

    /**
     * Основной метод класса, который вызывается контейнером для обработки
     * полученного сообщения. Контейнер извлекает сообщение из очереди и
     * передаёт его в качестве параметра вызова данного метода.
     *
     * @param message полученное сообщениею
     */
    @Override
    public void onMessage(Message message) {
        try {
            // Проверка соответствия типа сообщения ожидаемому типу
            if (message instanceof TextMessage) {
                TextMessage msg = (TextMessage) message;
                // Извлечение значения ключа login
                String login = msg.getStringProperty("login");
                // Извлечение значения ключа message
                String text = msg.getText();
                // Передача полученного сообщения для записи в базу данных.
                jpa.addMessage(login, text);
            }
        } catch (JMSException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод, вызываемый перед удалением объекта из памяти.
     */
    @PreDestroy
    public void delete() {
        log.log(Level.INFO, "MDB DemoMessageBean deleted ({0}).", hashCode());
    }
}
