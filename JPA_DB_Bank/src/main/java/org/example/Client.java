package org.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity //Анотація, яка вказує Hibernate, що наш клас є спеціальним і його об'єкти потрібно зберігати в базі даних
@Table(name = "Clients") //Встановлюємо назву таблиці
public class Client {
    @Id //Анотація, за допомогою якої задаємо PrimaryKey
    @GeneratedValue //Автогенерація номера ID
    @Column(name = "id_Client") //Встановлюємо ім'я для колонки таблиці
    private Long id;

    @Column(name = "name_Client", nullable = false) //Встановлюємо ім'я для колонки таблиці, яка не може бути пустою
    private String name;

    @Column(name = "phone_Client", unique = true) //Встановлюємо ім'я для колонки таблиці, поле повинно бути унікальним
    private Integer phone;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL) //CascadeType.ALL означає, що всі дії, які ми
    //виконуємо з батьківським об'єктом, потрібно повторити і для його залежних об'єктів.
    private List<Account> accounts = new ArrayList<>();

    public Client() {} //Конструктор за замовчуванням

    public Client(String name, Integer phone) { //Конструктор цього класу з параметрами для майбутніх об'єктів
        this.name = name;
        this.phone = phone;
    }

    //Геттери та Сеттери
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) { //Метод для додавання рахунку в список рахунків
        if(!accounts.contains(account)) {
            accounts.add(account);
            account.setClient(this);
        }
    }

    @Override //Переопреділяємо метод
    public String toString() { //До строкового виду
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                '}';
    }
}
