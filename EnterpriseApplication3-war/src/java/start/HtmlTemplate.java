/*
 * Приложение "Messager".
 * Интерфейс HtmlTemplate.java.
 * (C) Ю.Д.Заковряшин, 2020
 */
package start;

/**
 * Интерфейс определяет набор строковых констант, представляющих стандартные
 * элементы дизайна страниц приложения.
 *
 * @author Ю.Д.Заковряшин, 2008-2020
 */
public interface HtmlTemplate {

    /**
     * Стандартный заголовок HTML-страницы.
     */
    public static final String HEAD
            = "<!DOCTYPE html>\n"
            + "<!--\n"
            + "Стартовая страница учебного web-приложения \"Messanger\".\n"
            + "(C) Ю.Д.Заковряшин, 2008-2020.\n"
            + "-->\n"
            + "<html>\n"
            + "    <head>\n"
            + "        <title>Messager, v.1.0</title>\n"
            + "        <meta charset=\"UTF-8\">\n"
            + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
            + "    </head>\n"
            + "    <body>\n"
            + "        <header style=\"height: 40px; text-align: center; line-height: 40px;\n"
            + "                color: ghostwhite; background-color: #0099ff\">\n"
            + "            <h2>Приложение Messager, v.1.1</h2>          \n"
            + "        </header>\n"
            + "        <hr width=\"100%\" align=\"center\" />";
    /**
     * Краткое описание приложения.
     */
    public static final String DESCRIPTION = "        <div style=\"text-align:  center\">\n"
            + "           Лабораторная №4."
            + "        </div>";
    /**
     * Html-форма для ввода имени(логина) пользователя.
     */
    public static final String LOGIN_FORM = "        <form action=\"Messager\" method=\"GET\" >\n"
            + "            <p  style=\"text-align: center\">\n"
            + "                <!-- Ввод идентификатора пользователя (логина) -->\n"
            + "                Введите Ваше имя(логин) длиной до 8 символов:\n"
            + "                <input type=\"text\" name=\"login\" size=\"16\" required />\n"
            + "            </p>\n"
            + "            <p style=\"text-align: center\">\n"
            + "                <!-- Кнопка отправки формы на сервер -->           \n"
            + "                <input type=\"submit\" value=\"Начать работу\"/>\n"
            + "            </p>\n"
            + "        </form>";
    /**
     * Форма ввода сообщения пользователя.
     */
    public static final String MESSAGE_FORM = "        <form action=\"Messager\" method=\"POST\" >\n"
            + "                               <style type=\"text/css\">\n"
            + "                                .tab{ margin-left: auto;\n"
            + "                                 margin-right: auto}\n"
            + "                            </style>\n"
            + "                         <table class=\"tab\" bgcolor=\"c0e4ff\" border=\"2\"\n"
            + "                            cellspacing=\"5\" cellpadding=\"5\" width=\"450\">\n"
            + "                        <tr>\n"
            + "                            <td colspan=\"2\">\n"
            + "                        <center>\n"
            + "                            Введите Ваше текстовое сообщение\n"
            + "                        </center>\n"
            + "                        </td>\n"
            + "                        </tr>\n"
            + "                        <tr >\n"
            + "                            <td >\n"
            + "                                Текст до 255 символов:\n"
            + "                            </td> \n"
            + "                            <td >\n"
            + "                                <input type=\"text\" name=\"message\" value=\"\" size=\"30\"required />\n"
            + "                            </td>\n"
            + "                        </tr>\n"
            + "                        <tr>\n"
            + "                            <td colspan=\"2\">\n"
            + "                                <style type=\"text/css\">\n"
            + "                                .bt{width:100%}\n"
            + "                            </style>\n"
            + "                            <input class=\"bt\" type=\"submit\" name=\"saveText\" value=\"Отправить сообщение\"/>\n"
            + "                        </td>\n"
            + "                    </tr>\n"
            + "                </table> "
            + "        </form>"
            + "<BR>";

    /**
     * Форма ввода сообщения пользователя.
     */
    public static final String NUMBER_FORM = "        <form action=\"Messager\" method=\"POST\" >\n"
            + "                                 <style type=\"text/css\">\n"
            + "                                .tab{ margin-left: auto;\n"
            + "                                 margin-right: auto}\n"
            + "                            </style>\n"
            + "                         <table class=\"tab\" bgcolor=\"c0e4ff\" border=\"2\"\n"
            + "                            cellspacing=\"5\" cellpadding=\"5\" width=\"450\">\n"
            + "                        <tr>\n"
            + "                            <td colspan=\"2\">\n"
            + "                        <center>\n"
            + "                            Введите Ваше целое число\n"
            + "                        </center>\n"
            + "                        </td>\n"
            + "                        </tr>\n"
            + "                        <tr >\n"
            + "                            <td >\n"
            + "                                Целое число до 9 символов:\n"
            + "                            </td> \n"
            + "                            <td align=\"center\">\n"
            + "                                <input type=\"text\" name=\"message\" value=\"\" size=\"20\"required />\n"
            + "                            </td>\n"
            + "                        </tr>\n"
            + "                        <tr>\n"
            + "                            <td colspan=\"2\">\n"
            + "                                <style type=\"text/css\">\n"
            + "                                .bt{width:100%}\n"
            + "                            </style>\n"
            + "                            <input class=\"bt\" type=\"submit\" name=\"saveNumber\" value=\"Отправить сообщение\"/>\n"
            + "                        </td>\n"
            + "                    </tr>\n"
            + "                </table> "
            + "        </form>";
    /**
     * Форма ввода сообщения пользователя.
     */
    public static final String BUTTON_FORM = "        <form action=\"Messager\" bgcolor=\"c0e4ff\" method=\"POST\" >\n"
            + "            <p  style=\"text-align: center\">\n"
            + "                <!-- Кнопки пользователя -->\n"
            + "            </p>\n"
            + "            <p style=\"text-align: center\">\n"
            + "                <input type=\"submit\" name=\"summ\" value=\"Вывести сумму всех чисел\"/>&nbsp;\n"
            + "                <input type=\"submit\" name=\"list\" value=\"Вывести список текстовых сообщений\"/>&nbsp;\n"
            + "                <input type=\"submit\" name=\"return\" value=\"Завершить работу\"/>\n"
            + "            </p>\n"
            + "        </form>";

    /**
     * Форма вывода списка ранее введённых сообщений пользователя.
     */
    public static final String MESSAGE_LIST_FORM = "        <form action=\"Messager\" method=\"POST\" >\n"
            + "            <p style=\"text-align: center\">\n"
            + "                <!-- Кнопка отправки команд -->           \n"
            + "                <input type=\"submit\" name=\"next\" value=\"Добавить сообщение\"/>&nbsp;\n"
            + "                <input type=\"submit\" name=\"return\" value=\"Завершить работу\"/>&nbsp;\n"
            + "            </p>\n"
            + "        </form>";
    /**
     * HTML-код, определяющий начало сообщения приложения.
     */
    public static final String START_MESSAGE = "            "
            + "<p  style=\"text-align: center; color:#F00000;\">\n";
    /**
     * HTML-код, определяющий конец сообщения приложения.
     */
    public static final String END_MESSAGE = "            </p>\n";
    /**
     * Дополнительные сообщения.
     */
    public static final String SESSION_CLOSED = "Сессия пользователя закрыта";
    public static final String MESSAGE = "Сообщение \"";
    public static final String SENT = "\" отправлено";
    public static final String UNKNOWN_COMMAND = "Неизвестная команда";
    public static final String NULL_MESSAGE = "Пустое сообщение не отправлено";
    public static final String BAD_NUMBER_MESSAGE = "Это не число. Собщение не отправлено";
    public static final String CONNECTION_ERROR = "Ошибка: отсутствует "
            + "соединение с очередью сообщений";
    public static final String LIST_HEAD_START
            = "            <h3>Список сообщений пользователя : ";
    public static final String LIST_HEAD_END
            = "            </h3>\n            <ol>\n";
    
    public static final String SUMM_HEAD_START
            = "            <h3>Сумма введенных чисел пользователя : ";
    public static final String SUMM_HEAD_END
            = "            </h3>\n";
    public static final String SUMM_FOOTER_START
            = "            <h3>Сумма : ";
    public static final String SUMM_FOOTER_END
            = "            </h3>\n";
    public static final String NULL_LIST = " Список сообщений пуст";
    /**
     * Стандартный подвал страницы приложения.
     */
    public static final String FOOTER = "        <hr width=\"100%\" align=\"center\" />\n"
            + "        <footer style=\"height:20px; text-align: center; line-height: 20px;\n"
            + "                color: ghostwhite; font-size: smaller; font-weight: bolder; \n"
            + "                background-color: #0099ff\">\n"
            + "            "
            + "        </footer>\n"
            + "    </body>\n"
            + "</html>";
    public static final String INVALID_NAME = "Имя пользователя задано с ошибкой";
}
