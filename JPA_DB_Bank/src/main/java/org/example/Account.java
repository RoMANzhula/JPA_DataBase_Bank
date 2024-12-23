package org.example;

import javax.persistence.*;

@Entity //анотація, яка вказує Hibernate, що наш клас є спеціальним і його об'єкти потрібно зберігати в базі даних
@Table(name = "Accounts") //встановлюємо назву таблиці
public class Account {
    @Id //анотація, за допомогою якої задаємо PrimaryKey
    @GeneratedValue //автогенерація номера ID
    @Column(name = "id_Account") //встановлюємо ім'я для стовпця таблиці
    private Long id;

    @Column(name = "currency_account") //встановлюємо ім'я для стовпця таблиці
    private String currency;

    @Column(name = "balance_account") //встановлюємо ім'я для стовпця таблиці
    private Double balance;

    @ManyToOne //анотація @ManyToOne вказує Hibernate, що багато сутностей з інших таблиць можуть посилатися на одну сутність Client
    @JoinColumn(name = "id_Client", nullable = false) //анотація @JoinColumn вказує ім'я стовпця, з якого буде братися id,
    //не може бути null
    private Client client;

    public Account() {} //конструктор за замовчуванням

    public Account(String currency, Double balance, Client client) { //конструктор цього класу з параметрами для майбутніх об'єктів
        this.currency = currency;
        this.balance = balance;
        this.client = client;
    }

    //Гетери та сетери
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double total) {
        this.balance = total;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void replenishBalance(Double balance) { //метод для поповнення балансу
        this.balance += balance;
    }

    public void withdrawFromBalance(Double balance) { //метод для зняття з балансу
        this.balance -= balance;
    }

    @Override //перевизначаємо метод
    public String toString() { //перетворення до рядкового вигляду
        return "Account{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", client=" + client +
                '}';
    }
}
