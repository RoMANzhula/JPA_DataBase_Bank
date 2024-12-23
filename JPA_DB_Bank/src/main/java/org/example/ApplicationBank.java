package org.example;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class ApplicationBank {
    static EntityManagerFactory emf; //змінна-посилання для з'єднання
    static EntityManager em; //змінна-посилання для управління сутностями 

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); //потік для введення даних з клавіатури
        try {
            emf = Persistence.createEntityManagerFactory("JPATest4"); //виконуємо з'єднання
            em = emf.createEntityManager(); //ініціалізуємо об'єкт для управління сутностями

            //заповнимо одразу таблицю з курсом валют трьома позиціями та встановимо їм курс щодо гривні
            //далі можна буде через меню додавати валюту в таблицю та встановлювати курс
            em.getTransaction().begin(); //запускаємо транзакцію для роботи з сутностями
            RateOfExchange rateOfExchange1 = new RateOfExchange("USD", 36.56); //долар
            em.persist(rateOfExchange1);
            RateOfExchange rateOfExchange2 = new RateOfExchange("EUR", 39.83); //євро
            em.persist(rateOfExchange2);
            RateOfExchange rateOfExchange3 = new RateOfExchange("UAH", 1.0); //гривня
            em.persist(rateOfExchange3);
            em.getTransaction().commit(); //завершуємо транзакцію для роботи з сутностями

            try {
                while (true) {
                    System.out.println("1: додати клієнта"); //додати клієнта
                    System.out.println("2: видалити клієнта"); //видалити клієнта
                    System.out.println("3: переглянути клієнтів"); //переглянути всіх клієнтів
                    System.out.println("4: переглянути транзакції"); //переглянути транзакції
                    System.out.println("5: додати рахунок"); //створення рахунку з першим поповненням
                    System.out.println("6: поповнити рахунок"); //поповнити рахунок
                    System.out.println("7: видалити рахунок"); //видалити рахунок
                    System.out.println("8: переглянути рахунки"); //переглянути всі рахунки
                    System.out.println("9: додати валюту з її курсом"); //додати валюту з її курсом
                    System.out.println("10: видалити валюту з її курсом"); //видалити валюту з її курсом
                    System.out.println("11: переглянути всі валюти з їх курсом"); //переглянути всі валюти з їх курсом
                    System.out.println("12: перевести кошти"); //перевести кошти
                    System.out.println("13: перевести кошти з конвертацією"); //перевести кошти з конвертацією
                    System.out.println("14: перевести кошти з конвертацією для одного клієнта"); //перевести кошти з конвертацією
                    System.out.println("15: переглянути загальну суму коштів одного клієнта в гривні"); //переглянути сумарно кошти одного клієнта в гривні

                    System.out.print("-> ");

                    String str = sc.nextLine();
                    switch (str) { //блок для взаємодії з користувачем (меню нашого застосунку)
                        case "1":
                            addClient(sc);
                            break;
                        case "2":
                            deleteClient(sc);
                            break;
                        case "3":
                            viewClients();
                            break;
                        case "4":
                            viewTransactions();
                            break;
                        case "5":
                            addAccount(sc);
                            break;
                        case "6":
                            topUpAccount(sc);
                            break;
                        case "7":
                            deleteAccount(sc);
                            break;
                        case "8":
                            viewAccounts();
                            break;
                        case "9":
                            addRateOfExchange(sc);
                            break;
                        case "10":
                            deleteRateOfExchange(sc);
                            break;
                        case "11":
                            viewRateOfExchanges();
                            break;
                        case "12":
                            transferFunds(sc);
                            break;
                        case "13":
                            transferFundsWithConversion(sc);
                            break;
                        case "14":
                            transferFundsWithConversionForSingleClient(sc);
                            break;
                        case "15":
                            totalFundsSingleClientInUAH(sc);
                            break;

                        default:
                            return;
                    }
                }
            } finally { //блок для закриття потоків і з'єднань
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private static void addClient(Scanner sc) { // Метод для додавання клієнта до таблиці
    System.out.print("Введіть ім'я клієнта: "); // Просимо ввести ім'я клієнта
    String name = sc.nextLine(); // Зчитуємо введене ім'я

    System.out.print("Введіть номер телефону клієнта: "); // Просимо ввести номер телефону
    String strPhone = sc.nextLine(); // Зчитуємо введений номер телефону
    int phone = Integer.parseInt(strPhone); // Перетворюємо номер телефону на число

    em.getTransaction().begin(); // Розпочинаємо транзакцію для роботи зі сутностями
    try {
        Client client = new Client(name, phone); // Створюємо нового клієнта з переданими параметрами конструктора
        em.persist(client); // Зберігаємо об'єкт у контексті
        em.getTransaction().commit(); // Завершуємо транзакцію

        System.out.println(client.getId()); // Виводимо ID нового клієнта
    } catch (Exception exception) {
        em.getTransaction().rollback(); // У разі помилки скасовуємо всі зміни
    }
}

private static void deleteClient(Scanner sc) { // Метод для видалення клієнта з таблиці
    System.out.print("Введіть ID клієнта: "); // Просимо ввести ID клієнта
    String strId = sc.nextLine(); // Зчитуємо введений ID
    long id = Long.parseLong(strId); // Перетворюємо ID у число

    Client client = em.getReference(Client.class, id); // Шукаємо клієнта за ID
    if (client == null) { // Якщо клієнт не знайдений
        System.out.println("Клієнта не знайдено!"); // Повідомляємо користувача
        return; // Повертаємось до меню
    }

    em.getTransaction().begin(); // Розпочинаємо транзакцію
    try {
        em.remove(client); // Видаляємо клієнта
        em.getTransaction().commit(); // Завершуємо транзакцію
    } catch (Exception ex) {
        em.getTransaction().rollback(); // У разі помилки скасовуємо всі зміни
    }
}

private static void viewClients() { // Метод для перегляду всіх клієнтів у таблиці
    String queryClients = "SELECT c FROM Client c"; // Запит для отримання всіх клієнтів
    Query query = em.createQuery(queryClients, Client.class); // Виконуємо запит
    List<Client> list = (List<Client>) query.getResultList(); // Формуємо список результатів

    for (Client client : list) { // Для кожного клієнта зі списку
        System.out.println(client); // Виводимо на консоль
    }
}

private static void viewTransactions() { // Метод для перегляду всіх транзакцій
    String queryTransactions = "SELECT t FROM Transaction t"; // Запит для отримання транзакцій
    Query query = em.createQuery(queryTransactions, Transaction.class); // Виконуємо запит
    List<Transaction> list = (List<Transaction>) query.getResultList(); // Формуємо список результатів

    for (Transaction transaction : list) { // Для кожної транзакції зі списку
        System.out.println(transaction); // Виводимо на консоль
    }
}

private static void addAccount(Scanner sc) { // Метод для створення рахунку за іменем клієнта (з першим поповненням)
    System.out.print("Введіть ім'я клієнта: "); // Просимо ввести ім'я клієнта
    String clientName = sc.nextLine(); // Зчитуємо введене ім'я

    Client client = null; // Створюємо посилальну змінну для клієнта
    try {
        String queryNameClient = "SELECT c FROM Client c WHERE c.name = :name"; // Запит для пошуку клієнта
        Query query = em.createQuery(queryNameClient); // Формуємо запит
        query.setParameter("name", clientName); // Підставляємо ім'я клієнта
        client = (Client) query.getSingleResult(); // Отримуємо клієнта за результатами запиту

        System.out.print("Введіть суму: "); // Просимо ввести суму для поповнення
        String strSum = sc.nextLine(); // Зчитуємо введену суму
        double sum = Double.parseDouble(strSum); // Перетворюємо суму на число

        em.getTransaction().begin(); // Розпочинаємо транзакцію
        Account account = new Account(selectCurrency(), sum, client); // Створюємо новий рахунок
        em.persist(account); // Зберігаємо рахунок
        em.getTransaction().commit(); // Завершуємо транзакцію
        System.out.println("Готово.");

    } catch (NoResultException exception) { // Якщо клієнта не знайдено
        System.out.println("Клієнта не знайдено!");
        return;
    } catch (NonUniqueResultException exception) { // Якщо знайдено декілька клієнтів
        System.out.println("Знайдено декілька клієнтів з однаковим ім'ям!");
        return;
    }
}

    private static void topUpAccount(Scanner sc) { //метод для поповнення рахунку
        double thisBalance = 0.0; //створюємо змінну для суми поповнення
        System.out.print("Enter account id: "); //просимо ввести id рахунку
        String strAccountId = sc.nextLine(); //читаємо з консолі id рахунку
        long accountId = Long.parseLong(strAccountId); //парсимо id рахунку

        Account account = em.find(Account.class, accountId); //виконуємо пошук по id рахунку
        if (account == null) { //якщо рахунок не знайдено
            System.out.println("Account not found!"); //рахунок не знайдено
            return; //повертаємося в меню
        }

        System.out.print("Enter sum: "); //просимо ввести суму поповнення
        String strSum = sc.nextLine(); //читаємо з консолі суму поповнення
        double balance = Double.parseDouble(strSum); //парсимо суму поповнення

        em.getTransaction().begin(); //запускаємо транзакцію для роботи з сутностями
        try {
            Query query; //посилання на запит
            String queryCurrency = "SELECT r FROM RateOfExchange r WHERE r.currency = :currency"; //строка-запит для курсу валют по назві стовпця "валюта"

            if (!selectCurrency().equals(account.getCurrency())) { //якщо валюта з рахунку не співпадає з валютою з методу по поверненню валюти
                query = em.createQuery(queryCurrency); //створюємо запит
                query.setParameter("currency", account.getCurrency()); //підставляємо параметр
                RateOfExchange rateOfExchange = (RateOfExchange) query.getSingleResult(); //отримуємо одиночний результат
                double balanceOfAccount = rateOfExchange.getRateToUAH();

                if (selectCurrency().equals("UAH")) {
                    thisBalance = (balance / balanceOfAccount);
                } else {
                    thisBalance = balance;
                }
                account.replenishBalance(thisBalance);
                em.persist(account); //зберігаємо об'єкт
            } else {
                thisBalance = balance;
                account.replenishBalance(thisBalance);
                em.persist(account); //зберігаємо об'єкт
            }

            Transaction transaction = new Transaction(account.getClient(), account, null, thisBalance); //для поповнення рахунку з терміналів
            em.persist(transaction); //зберігаємо об'єкт
            em.getTransaction().commit(); //завершуємо транзакцію для роботи з сутностями
            System.out.println("Ok!");
        } catch (Exception exception) {
            exception.printStackTrace();
            em.getTransaction().rollback(); //якщо виникла помилка - скасовуємо всі зміни в сутностях
            return; //повертаємось в меню
        }
    }

    private static void deleteAccount(Scanner sc) { //метод для видалення рахунку з таблиці
        System.out.print("Enter account id: "); //просимо ввести id рахунку
        String strId = sc.nextLine(); //читаємо з консолі id рахунку
        long id = Long.parseLong(strId); //парсимо id рахунку

        Account account = em.getReference(Account.class, id); //виконуємо пошук по id рахунку
        if (account == null) { //якщо рахунок не знайдений
            System.out.println("Account not found!"); //рахунок не знайдено
            return; //повертаємось в меню
        }

        em.getTransaction().begin(); //запускаємо транзакцію для роботи з сутностями
        try {
            em.remove(account); //видаляємо об'єкт
            em.getTransaction().commit(); //завершуємо транзакцію для роботи з сутностями
        } catch (Exception ex) {
            em.getTransaction().rollback(); //якщо виникла помилка - скасовуємо всі зміни в сутностях
        }
    }

    private static void viewAccounts() { //метод для демонстрації всіх рахунків з таблиці
        String queryAccounts = "SELECT a FROM Account a"; //строка-запит для пошуку в таблиці рахунків
        Query query = em.createQuery(queryAccounts, Account.class); //запит
        List<Account> list = (List<Account>) query.getResultList(); //створюємо список для результатів пошуку

        for (Account account : list) //для кожного рахунку зі списку
            System.out.println(account); //вивести на екран рахунок
    }

    private static void addRateOfExchange(Scanner sc) { //метод для додавання курсу валют в таблицю
        System.out.print("Enter currency: "); //просимо ввести валюту
        String currency = sc.nextLine(); //читаємо з консолі валюту

        System.out.print("Enter rate to UAH: "); //просимо ввести курс до гривні
        String strRateToUAH = sc.nextLine(); //читаємо з консолі введений курс до гривні
        double rateToUAH = Double.parseDouble(strRateToUAH); //парсимо курс до гривні

        em.getTransaction().begin(); //запускаємо транзакцію для роботи з сутностями
        try {
            RateOfExchange rateOfExchange = new RateOfExchange(currency, rateToUAH); //створюємо новий курс валют з параметрами
            em.persist(rateOfExchange); //зберігаємо об'єкт
            em.getTransaction().commit(); //завершуємо транзакцію для роботи з сутностями

            System.out.println(rateOfExchange.getId()); //друкуємо id курсу валют
        } catch (Exception exception) {
            em.getTransaction().rollback(); //якщо виникла помилка - скасовуємо всі зміни в сутностях
        }
    }

    private static void deleteRateOfExchange(Scanner sc) { //метод для видалення курсу валют з таблиці
        System.out.print("Enter rate of exchange id: "); //просимо ввести id курсу валют
        String strId = sc.nextLine(); //читаємо id курсу валют введений користувачем
        long id = Long.parseLong(strId); //парсимо id курсу валют

        RateOfExchange rateOfExchange = em.getReference(RateOfExchange.class, id); //виконуємо пошук по id курсу валют
        if (rateOfExchange == null) { //якщо курс валют не знайдений
            System.out.println("Rate of exchange not found!"); //курс валют не знайдено
            return; //повертаємось в меню
        }

        em.getTransaction().begin(); //запускаємо транзакцію для роботи з сутностями
        try {
            em.remove(rateOfExchange); //видаляємо об'єкт
            em.getTransaction().commit(); //завершуємо транзакцію для роботи з сутностями
        } catch (Exception ex) {
            em.getTransaction().rollback(); //якщо виникла помилка - скасовуємо всі зміни в сутностях
        }
    }

    private static void viewRateOfExchanges() { //метод для демонстрації всіх курсів валют з таблиці
        String queryRate = "SELECT r FROM RateOfExchange r"; //строка-запит для пошуку в таблиці курсів валют
        Query query = em.createQuery(queryRate, RateOfExchange.class); //запит
        List<RateOfExchange> list = (List<RateOfExchange>) query.getResultList(); //створюємо список для результатів запиту

        for (RateOfExchange rateOfExchange : list) //для кожного курсу валют зі списку
            System.out.println(rateOfExchange); //вивести на екран
    }

    private static void transferFunds(Scanner sc) { //метод для переведення грошей між рахунками з однаковою валютою
    System.out.print("Enter your sender account id: "); //просимо ввести id рахунку-відправника
    String strYourSenderAccountId = sc.nextLine(); //зчитуємо id рахунку-відправника
    Long yourSenderAccountId = Long.parseLong(strYourSenderAccountId); //парсимо id рахунку-відправника

    Account senderAccount = em.find(Account.class, yourSenderAccountId); //шукаємо рахунок за id
    if (senderAccount == null) { //якщо не знайдено
        System.out.println("Account not found!"); //виводимо - рахунок не знайдено
        return; //повертаємось у меню
    }

    System.out.print("Enter beneficiary account id: "); //просимо ввести id рахунку-одержувача
    String strBeneficiaryAccountId = sc.nextLine(); //зчитуємо id рахунку-одержувача
    Long beneficiaryAccountId = Long.parseLong(strBeneficiaryAccountId); //парсимо id рахунку-одержувача у число

    Account beneficiaryAccount = em.find(Account.class, beneficiaryAccountId); //шукаємо рахунок-одержувача за id
    if (beneficiaryAccount == null) { //якщо рахунок-одержувач відсутній
        System.out.println("Account not found!"); //виводимо - рахунок не знайдено
        return; //повертаємось у меню
    }

    if (senderAccount.getCurrency().equals(beneficiaryAccount.getCurrency())) { //якщо валюта рахунку-відправника дорівнює
        //валюті рахунку-одержувача
        System.out.print("Enter sum for transfer: "); //просимо ввести суму для переведення
        String strSum = sc.nextLine(); //зчитуємо суму, введену користувачем
        double sum = Double.parseDouble(strSum); //парсимо суму у число
        if (sum > senderAccount.getBalance()) { //якщо введена сума більша за баланс рахунку
            System.out.println("Error! Insufficiently money!"); //виводимо - Помилка! Недостатньо коштів!
            return; //повертаємось у меню
        }

        em.getTransaction().begin(); //починаємо транзакцію для роботи з об'єктами
        try {
            Transaction transaction = new Transaction(senderAccount.getClient(), beneficiaryAccount, senderAccount, sum);
            em.persist(transaction); //зберігаємо об'єкт

            beneficiaryAccount.replenishBalance(sum); //додаємо суму до рахунку-одержувача
            senderAccount.withdrawFromBalance(sum); //знімаємо суму з рахунку-відправника

            em.getTransaction().commit(); //завершуємо транзакцію
            System.out.println("Ok!");
        } catch (Exception exception) {
            exception.printStackTrace();
            em.getTransaction().rollback(); //якщо виникла помилка - скасовуємо всі зміни
            return;
        }
    } else {
        System.out.println("Enter the account with the currency that matches!"); //виводимо - введіть рахунок з валютою, яка
        //відповідає
        return; //повертаємось у меню
    }
}

private static void transferFundsWithConversion(Scanner sc) { //метод для переведення коштів з адаптивною конвертацією валюти
    System.out.print("Enter sender account id: "); //просимо ввести id рахунку-відправника
    String strSenderAccountId = sc.nextLine(); //зчитуємо id рахунку-відправника
    Long senderAccountId = Long.parseLong(strSenderAccountId); //парсимо id рахунку-відправника у число

    Account senderAccount = em.find(Account.class, senderAccountId); //шукаємо рахунок-відправника за його id
    if (senderAccount == null) { //якщо рахунок-відправник відсутній
        System.out.println("Account not found!"); //виводимо - рахунок не знайдено
        return; //повертаємось у меню
    }

    System.out.print("Enter beneficiary account id: "); //просимо ввести id рахунку-одержувача
    String strBeneficiaryAccountId = sc.nextLine(); //зчитуємо id рахунку-одержувача
    Long beneficiaryAccountId = Long.parseLong(strBeneficiaryAccountId); //парсимо id рахунку-одержувача у число

    Account beneficiaryAccount = em.find(Account.class, beneficiaryAccountId); //шукаємо рахунок-одержувача за id
    if (beneficiaryAccount == null) { //якщо рахунок-одержувач відсутній
        System.out.println("Account not found!"); //виводимо - рахунок не знайдено
        return; //повертаємось у меню
    }

    System.out.print("Enter sum for transfer: "); //просимо ввести суму для переведення між рахунками
    String strSum = sc.nextLine(); //зчитуємо відповідь користувача
    double sum = Double.parseDouble(strSum); //парсимо суму коштів для переведення
    if (sum > senderAccount.getBalance()) { //якщо сума переведення більша за суму на рахунку-відправнику
        System.out.println("Error! Insufficiently money!"); //Помилка! Недостатньо коштів!
        return; //повертаємось у меню
    }

    em.getTransaction().begin(); //починаємо транзакцію для роботи з об'єктами
    try {
        Transaction transaction = new Transaction(senderAccount.getClient(), senderAccount, beneficiaryAccount, sum); //створюємо транзакцію
        em.persist(transaction); //зберігаємо об'єкт

        if (!senderAccount.getCurrency().equals(beneficiaryAccount.getCurrency())) { //якщо валюти між рахунками не збігаються
            String queryRateOfExchange = "SELECT r from RateOfExchange r WHERE currency = :currency"; //запит на курс валюти

            Query query = em.createQuery(queryRateOfExchange); //створюємо запит
            query.setParameter("currency", senderAccount.getCurrency()); //для рахунку-відправника
            RateOfExchange rateOfExchange = (RateOfExchange) query.getSingleResult(); //отримуємо одиничний результат
            double senderAccountRate = rateOfExchange.getRateToUAH(); //отримуємо курс рахунку-відправника до гривні

            query.setParameter("currency", beneficiaryAccount.getCurrency()); //для рахунку-одержувача
            rateOfExchange = (RateOfExchange) query.getSingleResult(); //отримуємо одиничний результат
            double beneficiaryAccountRate = rateOfExchange.getRateToUAH(); //отримуємо курс рахунку-одержувача до гривні

            double thisBalance; //буфер для математичних операцій з коштами
            if (senderAccount.getCurrency().equals("UAH")) { //якщо валюта рахунку-відправника є гривнею
                thisBalance = sum / beneficiaryAccountRate; //ініціалізуємо буфер = суму переведення ділимо на курс рахунку-одержувача
                //відносно гривні
            } else { //в іншому випадку
                thisBalance = sum * senderAccountRate / beneficiaryAccountRate; //ініціалізуємо буфер = суму переведення
                //множимо на курс до гривні рахунку-відправника та ділимо на курс до гривні рахунку-одержувача
            }
            senderAccount.withdrawFromBalance(sum); //з рахунку-відправника знімаємо переведені кошти (без конвертації)
            beneficiaryAccount.replenishBalance(thisBalance); //на рахунок-одержувача додаємо кошти з буфера (з конвертацією)
        } else { //якщо валюти збігаються
            senderAccount.withdrawFromBalance(sum); //з рахунку-відправника знімаємо переведені кошти (без конвертації)
            beneficiaryAccount.replenishBalance(sum); //на рахунок-одержувача додаємо переведені кошти (без конвертації)
        }
        em.getTransaction().commit(); //завершуємо транзакцію
        System.out.println("Ok!");
    } catch (Exception ex) {
        ex.printStackTrace();
        em.getTransaction().rollback(); //якщо виникла помилка - скасовуємо всі зміни
        return;
    }
}

    private static void transferFundsWithConversionForSingleClient(Scanner sc) {
    System.out.print("Введіть ID рахунку відправника: "); // просимо ввести id рахунку-відправника
    String strSenderAccountId = sc.nextLine(); // зчитуємо id рахунку-відправника
    Long senderAccountId = Long.parseLong(strSenderAccountId); // парсимо id рахунку-відправника в число

    Account senderAccount = em.find(Account.class, senderAccountId); // знаходимо рахунок-відправник за його id
    if (senderAccount == null) { // якщо рахунок-відправник відсутній, то
        System.out.println("Рахунок не знайдений!"); // пишемо - рахунок не знайдений
        return; // повертаємось в меню
    }

    System.out.print("Введіть ID рахунку отримувача: "); // просимо ввести id рахунку-отримувача
    String strBeneficiaryAccountId = sc.nextLine(); // зчитуємо id рахунку-отримувача
    Long beneficiaryAccountId = Long.parseLong(strBeneficiaryAccountId); // парсимо id рахунку-отримувача в число

    Account beneficiaryAccount = em.find(Account.class, beneficiaryAccountId); // знаходимо рахунок-отримувач за його id
    if (beneficiaryAccount == null) { // якщо рахунок-отримувач відсутній, то
        System.out.println("Рахунок не знайдений!"); // пишемо - рахунок не знайдений
        return; // повертаємось в меню
    }

    if (senderAccount.getClient().equals(beneficiaryAccount.getClient())) { // якщо рахунки належать одному клієнту
        System.out.print("Введіть суму для переказу: "); // просимо ввести суму для переказу між рахунками
        String strSum = sc.nextLine(); // читаємо відповідь користувача
        double sum = Double.parseDouble(strSum); // парсимо суму переказу
        if (sum > senderAccount.getBalance()) { // якщо сума переказу більша за баланс на рахунку-відправнику, то
            System.out.println("Помилка! Недостатньо коштів!"); // помилка! Недостатньо коштів!
            return; // повертаємось в меню
        }

        em.getTransaction().begin(); // запускаємо транзакцію для роботи з об'єктами
        try {
            Transaction transaction = new Transaction(senderAccount.getClient(), senderAccount, beneficiaryAccount, sum); // створюємо транзакцію
            em.persist(transaction); // зберігаємо об'єкт транзакції

            if (!senderAccount.getCurrency().equals(beneficiaryAccount.getCurrency())) { // якщо валюти між рахунками не співпадають, то
                String queryRateOfExchange = "SELECT r from RateOfExchange r WHERE currency = :currency"; // запит на курс валюти

                Query query = em.createQuery(queryRateOfExchange); // запит
                query.setParameter("currency", senderAccount.getCurrency()); // для рахунку-відправника
                RateOfExchange rateOfExchange = (RateOfExchange) query.getSingleResult(); // отримуємо курс для відправника
                double senderAccountRate = rateOfExchange.getRateToUAH(); // курс рахунку-відправника до гривні

                query.setParameter("currency", beneficiaryAccount.getCurrency()); // для рахунку-отримувача
                rateOfExchange = (RateOfExchange) query.getSingleResult(); // отримуємо курс для отримувача
                double beneficiaryAccountRate = rateOfExchange.getRateToUAH(); // курс рахунку-отримувача до гривні

                double thisBalance; // змінна для математичних операцій з коштами
                if (senderAccount.getCurrency().equals("UAH")) { // якщо валюта рахунку-відправника - гривня
                    thisBalance = sum / beneficiaryAccountRate; // конвертуємо суму з UAH на валюту отримувача
                } else { // якщо валюти не співпадають
                    thisBalance = sum * senderAccountRate / beneficiaryAccountRate; // конвертуємо валюту з відправника на отримувача
                }
                senderAccount.withdrawFromBalance(sum); // знімаємо кошти з рахунку-відправника (без конвертації)
                beneficiaryAccount.replenishBalance(thisBalance); // поповнюємо рахунок-отримувач (з конвертацією)
            } else { // якщо валюти співпадають між рахунками
                senderAccount.withdrawFromBalance(sum); // знімаємо кошти з рахунку-відправника (без конвертації)
                beneficiaryAccount.replenishBalance(sum); // поповнюємо рахунок-отримувач (без конвертації)
            }
            em.getTransaction().commit(); // завершення транзакції
            System.out.println("Ок!");
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback(); // якщо сталася помилка - скасовуємо транзакцію
            return;
        }
    } else { // якщо рахунки належать різним клієнтам
        System.out.println("Помилка! Клієнти не співпадають!"); // пишемо - помилка! Клієнти не співпадають!
    }
}

private static void totalFundsSingleClientInUAH(Scanner sc) { // метод для відображення сумарних коштів на рахунку одного клієнта в UAH
    System.out.print("Введіть ім'я клієнта: "); // просимо ввести ім'я клієнта
    String strClientName = sc.nextLine(); // зчитуємо ім'я клієнта

    String queryAccounts = "SELECT a FROM Account a"; // запит на вибір всіх рахунків
    Query query = em.createQuery(queryAccounts, Account.class); // запит
    List<Account> list = (List<Account>) query.getResultList(); // створюємо список для результатів

    double totalFunds = 0.0; // змінна для підсумку коштів

    for (Account account : list) { // для кожного рахунку
        if (account.getClient().getName().equals(strClientName)) { // якщо ім'я рахунку співпадає з введеним
            account = em.getReference(Account.class, account.getId()); // шукаємо рахунок по id
            String queryRateOfExchange = "SELECT r from RateOfExchange r WHERE currency = :currency"; // запит на курс валюти
            query = em.createQuery(queryRateOfExchange); // запит
            query.setParameter("currency", account.getCurrency()); // для рахунку
            RateOfExchange rateOfExchange = (RateOfExchange) query.getSingleResult(); // отримуємо курс
            double thisAccountRate = rateOfExchange.getRateToUAH(); // курс рахунку до гривні
            double balanceInUAH = thisAccountRate * account.getBalance(); // перерахунок балансу в UAH
            totalFunds += balanceInUAH; // додаємо до загальної суми
            System.out.println(account); // виводимо рахунок для перевірки
        } else { // якщо рахунок не знайдений
            System.out.println("Рахунок не знайдений!"); // повідомляємо про помилку
            return; // повертаємось в меню
        }
    }
    System.out.println("Загальні кошти одного клієнта в UAH: " + String.format("%.2f", totalFunds)); // виводимо підсумкову суму
}

    private static String selectCurrency() { //метод для вибору валюти (повертає валюту у вигляді рядка)
    String queryRate = "SELECT r FROM RateOfExchange r"; //стрічка-запит для таблиці курсів валют
    Query query = em.createQuery(queryRate, RateOfExchange.class); //виконуємо запит
    List<RateOfExchange> list = (List<RateOfExchange>) query.getResultList(); //створюємо список з курсів валют

    Scanner sc = new Scanner(System.in); //відкриваємо потік для введення даних з клавіатури
    String strCurrencyNames = ""; //створюємо порожній рядок для зберігання назв валют
    for (RateOfExchange rateOfExchange : list) { //проходимо через кожен елемент зі списку курсів валют
        strCurrencyNames += rateOfExchange.getCurrency() + " "; //додаємо назву валюти до рядка strCurrencyNames
    }

    System.out.print("Select currency: " + strCurrencyNames); //пропонуємо вибрати валюту з доступних валют
    String strCurrency = sc.nextLine(); //читаємо відповідь користувача
    String currency = null; //створюємо змінну для валюти = null
    for (RateOfExchange rateOfExchange : list) { //проходимо через кожен елемент списку курсів валют
        if (strCurrency.equals(rateOfExchange.getCurrency())) { //якщо введена валюта збігається з однією з валют списку
            currency = strCurrency; //присвоюємо її змінній currency
        }
    }
    return currency; //повертаємо вибрану користувачем валюту
  }
}
// Створити базу даних «Банк» з таблицями «Користувачі», «Транзакції», «Рахунки» та «Курси валют».
//Рахунок буває 3-х видів: USD, EUR, UAH.
//Написати запити:
// для поповнення рахунку у потрібній валюті,
// Переказ коштів з одного рахунку на інший,
// Конвертація валюти за курсом в рамках рахунків одного користувача.
//Написати запит для отримання сумарних коштів на рахунку одного користувача в UAH (розрахунок за курсом)

