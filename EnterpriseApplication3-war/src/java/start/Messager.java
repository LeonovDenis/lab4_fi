/*
 * Приложение "Messager".
 * Класс Messager.java.
 * (C) Ю.Д.Заковряшин, 2020
 */
package start;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jpa.DemoJPALocal;
import jpa.Messages;

/**
 * Класс сервлета, обрабатывающего запросы к приложению.
 *
 * @author Ю.Д.Заковряшин, 2008-2020
 */
@WebServlet(name = "Messager",
        urlPatterns = {"/Messager"})
public class Messager extends HttpServlet implements HtmlTemplate {

    /**
     * Вложенное перечисление, определяющее типы форм приложения.
     */
    private static enum FormTypes {
        LOGIN, MESSAGE, LIST, SUMM;
    }
    /**
     * Ссылка на объект, представляющий собой "фабрику" соединений с очередью
     * сообщений.
     */
    @Resource(lookup = "jms/DemoTopicFactory")
    private javax.jms.TopicConnectionFactory factory;
    /**
     * Ссылка на объект, представляющий очередь сообщений. В этот объект
     * помещаются все сообщения пользователя.
     */
    @Resource(lookup = "jms/DemoTopic")
    private javax.jms.Topic queue;

    /**
     * Ссылка на объект, предоставляющий сервис доступа к базе данных. В данном
     * приложении этот объект обеспечивает получение списка сохранённых
     * сообщений.
     */
    @EJB
    private DemoJPALocal jpa;

    /**
     * Метод обрабатывает запрос типа <code>HTTP GET</code> на начало нового
     * сеанса с пользователем. Метод получает имя(логин) пользователя и
     * открывает новую сессию с пользователем.
     *
     * @param request ссылка на объект, представляющий запрос, в частности,
     * параметры запроса
     * @param response ссылка на объект, позволяющий сформировать ответ на
     * запрос
     * @throws ServletException выбрасывается в случае какой-либо ошибки,
     * связанной с работой сервлета.
     * @throws IOException выбрасывается в случае какой-либо ошибки, связанной с
     * операциями ввода/вывода (I/O error)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // Получение параметра запроса login
        String login = request.getParameter("login").trim();
        try (PrintWriter out = response.getWriter()) {
            if (login == null || login.isEmpty() || login.length() > 8) {
                out.println(getForm(FormTypes.LOGIN, INVALID_NAME));
                return;
            }
            // Получение ссылки на сессию пользователя, если она открыта.
            HttpSession hs = request.getSession(false);
            if (hs != null) {
                // Если сессия открыта, то она закрывается для предотвращения
                // использования открытой сессии другим пользователем.
                hs.invalidate();
            }
            // Открытие новой сессии.
            hs = request.getSession(true);
            // Сохранение в атрибуте сессии имени (логина) пользователя.
            hs.setAttribute("user", login.trim());
            // Вывод ответа пользователю.
            out.println(getForm(FormTypes.MESSAGE, null));
        }
    }

    /**
     * Метод обработки <code>HTTP POST</code> запроса. Метод определяет форму
     * ответа пользоватею.
     *
     * @param request ссылка на объект, представляющий запрос, в частности,
     * параметры запроса
     * @param response ссылка на объект, позволяющий сформировать ответ на
     * запрос
     * @throws ServletException выбрасывается в случае какой-либо ошибки,
     * связанной с работой сервлета.
     * @throws IOException выбрасывается в случае какой-либо ошибки, связанной с
     * операциями ввода/вывода (I/O error)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // Получение коллекции всех параметров запроса
        Map<String, String[]> map = request.getParameterMap();
        StringBuilder sb = new StringBuilder();
        try (PrintWriter out = response.getWriter()) {
            // Получение ссылки на сессию.
            HttpSession hs = request.getSession(false);
            if (hs == null) {
                // Если сессия пользователя уже закрыта,...
                sb.append(START_MESSAGE)
                        .append(SESSION_CLOSED)
                        .append(END_MESSAGE);
                // то пользователю возвращается форма ввода логина
                out.println(getForm(FormTypes.LOGIN, sb.toString()));
                return;
            }
            String user = hs.getAttribute("user").toString();
            String message = null;
            // Перебор полученной коллекции параметров запроса
            for (Map.Entry<String, String[]> m : map.entrySet()) {
                String key = m.getKey();
                switch (key) {
                    case "message": // Обработка собственно сообщения.
                        // Проверка корректности сообщения
                        if (m.getValue() != null && m.getValue().length > 0) {
                            message = m.getValue()[0].trim();
                        }
                        break;
                    case "saveText": // Команда на отправку сообщения.
                        sb.append(START_MESSAGE);
                        if (message == null || message.isEmpty()) {
                            // Если сообщение пустое
                            sb.append(NULL_MESSAGE);
                        } else {
                            // Если это "нормальное" сообщение, то оно 
                            // отправляется в очередь сообщений
                            String result = sendMessage(user, message, "text");
                            // Оценка результата и формирование сообщения 
                            // пользователю
                            if (result == null) {
                                // Сообщение об успешной отправке
                                sb.append(MESSAGE)
                                        .append(message)
                                        .append(SENT);
                            } else {
                                // Описание ошибки при отправке сообщения
                                sb.append(result);
                            }
                        }
                        sb.append(END_MESSAGE);
                        out.println(getForm(FormTypes.MESSAGE, sb.toString()));
                        return;
                    case "saveNumber": // Команда на отправку сообщения.
                        sb.append(START_MESSAGE);
                        if (message == null || message.isEmpty()) {
                            // Если сообщение пустое
                            sb.append(NULL_MESSAGE);
                        } else if (!message.matches("^-?[1-9][0-9]*$")) {
                            sb.append(BAD_NUMBER_MESSAGE);
                        } else {
                            // Если это "нормальное" сообщение, то оно 
                            // отправляется в очередь сообщений
                            if (message.length() > 10) {
                                message = message.substring(0, 9);
                            }
                            String result = sendMessage(user, message, "number");
                            // Оценка результата и формирование сообщения 
                            // пользователю
                            if (result == null) {
                                // Сообщение об успешной отправке
                                sb.append(MESSAGE)
                                        .append(message)
                                        .append(SENT);
                            } else {
                                // Описание ошибки при отправке сообщения
                                sb.append(result);
                            }
                        }
                        sb.append(END_MESSAGE);
                        out.println(getForm(FormTypes.MESSAGE, sb.toString()));
                        return;
                    case "summ": // Команда перехода на форму отправки сообщения

                        out.println(getForm(FormTypes.SUMM, user));

                        return;
                    case "next": // Команда перехода на форму отправки сообщения
                        out.println(getForm(FormTypes.MESSAGE, null));
                        return;
                    case "list": // Команда на вывод списка сообщения пользователя.
                        out.println(getForm(FormTypes.LIST, user));
                        return;
                    case "return": // Команда на выход из приложения.
                        hs.invalidate();
                        response.sendRedirect("index.html");
                        return;
                    default: // Обработка неизвестной команды.
                        sb.append(START_MESSAGE)
                                .append(UNKNOWN_COMMAND)
                                .append(END_MESSAGE);
                        out.println(getForm(FormTypes.LOGIN, sb.toString()));
                }
            }
        }
    }

    /**
     * Метод получает сообщение пользователя и отправляет его в очередь
     * сообщений для дальнейшего сохранения в базе данных.
     *
     * @param login Логин пользователя.
     * @param message Сообщение пользователя.
     * @return Значение null, если сообщение оправлено успешно, иначе -
     * сообщение с описанием ошибки.
     */
    private String sendMessage(String login, String message, String type) {
        // Вариант 1. Сообщение помещается в очередь сообщений, откуда оно 
        // асинхронно извлекается MDB-компонентом для последующей записи в базу
        // данных.
        try (
                // Поскольку QueueConnection и QueueSession наследуют
                // интерфейс AutoClosable, то их можно использовать в 
                // операторе "try-catch с ресурсами"
                // Создание соединения с очередью сообщений
                TopicConnection con = factory.createTopicConnection();
                // Открытие сессии
                TopicSession ses = con.createTopicSession(true, 0)) {
            // Создание отправителя сообщения
            MessageProducer sender = ses.createProducer(queue);
            // Создание сообщения типа "ключ-значение"
            Message map = null;
            if (type.equals("text")) {
                TextMessage mp = ses.createTextMessage();
                // Создание элемента сообщения с ключом "login"
                mp.setStringProperty("login", login);
                // Создание элемента сообщения с ключом "message"
                mp.setText(message);
                map = mp;
            } else {
                ObjectMessage mp = ses.createObjectMessage();
                // Создание элемента сообщения с ключом "login"
                mp.setStringProperty("login", login);
                // Создание элемента сообщения с ключом "message"
                mp.setObject(Integer.parseInt(message));
                map = mp;
            }
            // Отправка сообщения
            sender.send(map);
        } catch (JMSException e) {
            return CONNECTION_ERROR;
        }
        /*
        // Вариант 2. Альтернативный вариант. В данном случае сообщение 
        // помещается сразу в базу данных с помощью JPA-компонента DemoJPALocal.
        try {
            jpa.addMessage(login, message);
        } catch (Exception e) {
            return e.getMessage();
        }
         */
        return null;
    }

    /**
     * Метод формирует заданную форму ответа с полями ввода и сообщением о
     * результате обработки запроса.
     *
     * @param type Определяет форму ответа пользователю.
     * @param msg Сообщение пользователю.
     * @return HTML-код формы ответа
     */
    private String getForm(FormTypes type, String msg) {
        StringBuilder sb = new StringBuilder(HEAD);
        switch (type) {
            case MESSAGE: // Форма ввода сообщения
                sb.append(MESSAGE_FORM)
                        .append(NUMBER_FORM)
                        .append(BUTTON_FORM);
                if (msg != null) {
                    sb.append(START_MESSAGE)
                            .append(msg)
                            .append(END_MESSAGE);
                }
                break;
            case LIST: // Форма вывода списка сообщений 
                sb.append(getList(msg))
                        .append(MESSAGE_LIST_FORM);
                break;
            case SUMM: // Форма вывода списка сообщений 
                sb.append(getSumm(msg))
                        .append(MESSAGE_LIST_FORM);
                break;
            case LOGIN: // Форма ввода логина пользователя
                sb.append(LOGIN_FORM);
                if (msg != null) {
                    sb.append(START_MESSAGE)
                            .append(msg)
                            .append(END_MESSAGE);
                }
        }
        sb.append(FOOTER);
        return sb.toString();
    }

    /**
     * Метод возвращает HTML-код, содержащий список сообщений пользователя.
     *
     * @param login Логин (идентификатор) пользователя.
     * @return HTML-код, содержащий список сообщений пользователя.
     */
    private String getList(String login) {
        StringBuilder sb = new StringBuilder();
        // Обращение к JPA-компоненту с запросом списка сообщений.
        List<Messages> c = jpa.getList(login);
        if (c == null) {
            sb.append(START_MESSAGE)
                    .append(NULL_LIST)
                    .append(END_MESSAGE);
            return sb.toString();
        }
        sb.append(LIST_HEAD_START)
                .append(login)
                .append(LIST_HEAD_END);
        // Перебор коллекции сообщений и формирование HTML-код с его 
        // представлением.
        for (Messages m : c) {
            sb.append("<li>")
                    .append(m.getId())
                    .append(".")
                    .append(m.getMessage())
                    .append("</li>\n");
        }
        sb.append("</ol>");
        return sb.toString();
    }

    private String getSumm(String login) {
        StringBuilder sb = new StringBuilder();
        // Обращение к JPA-компоненту с запросом списка сообщений.
        Long c = jpa.getSum(login);
        if (c == null) {
            sb.append(START_MESSAGE)
                    .append(NULL_LIST)
                    .append(END_MESSAGE);
            return sb.toString();
        }
        sb.append(SUMM_HEAD_START)
                .append(login)
                .append(SUMM_HEAD_END);
        // Перебор коллекции сообщений и формирование HTML-код с его 
        // представлением.

        sb.append(SUMM_FOOTER_START)
                .append(c)
                .append(SUMM_FOOTER_END);
        return sb.toString();
    }

    /**
     * Метод возвращает краткое описание сервлета.
     *
     * @return объект String, содержащий описание сервлета
     */
    @Override
    public String getServletInfo() {
        return "Сервлет Messager";
    }

}
