package org.example;

import javax.persistence.*;

@Entity // Аннотація, яка вказує Hibernate, що наш клас є спеціальним, і його об'єкти потрібно зберігати в базі даних
@Table(name = "Transactions") // Встановлюємо назву таблиці
public class Transaction {
    @Id // Аннотація, з допомогою якої задаємо PrimaryKey
    @GeneratedValue // Автогенерація номера ID
    @Column(name = "id_Transaction") // Встановлюємо ім'я для колонки таблиці
    private Long id;

    @ManyToOne() // Аннотація @ManyToOne вказує Hibernate, що багато сутностей з інших таблиць можуть посилатись на одну сутність Client
    @JoinColumn(name = "client_Id") // Аннотація @JoinColumn вказує ім'я колонки, з якої буде братись id
    private Client client;

    @ManyToOne // Аннотація @ManyToOne вказує Hibernate, що багато сутностей з інших таблиць можуть посилатись на одну сутність Account
    @JoinColumn(name = "beneficiary_Id_Account") // Аннотація @JoinColumn вказує ім'я колонки, з якої буде братись id
    private Account beneficiaryIdAccount;

    @ManyToOne // Аннотація @ManyToOne вказує Hibernate, що багато сутностей з інших таблиць можуть посилатись на одну сутність Account
    @JoinColumn(name = "sender_Id_Account") // Аннотація @JoinColumn вказує ім'я колонки, з якої буде братись id
    private Account senderIdAccount;

    @Column(name = "balancePlus_Transaction") // Встановлюємо ім'я для колонки таблиці
    private Double balancePlus;

    public Transaction() {} // Конструктор за замовчуванням

    // Конструктор цього класу з параметрами для майбутніх об'єктів
    public Transaction(Client client, Account beneficiaryIdAccount, Account senderIdAccount, Double balancePlus) {
        this.client = client;
        this.beneficiaryIdAccount = beneficiaryIdAccount;
        this.senderIdAccount = senderIdAccount;
        this.balancePlus = balancePlus;
    }

    // Геттери та Сеттери
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Account getBeneficiaryIdAccount() {
        return beneficiaryIdAccount;
    }

    public void setBeneficiaryIdAccount(Account beneficiaryIdAccount) {
        this.beneficiaryIdAccount = beneficiaryIdAccount;
    }

    public Account getSenderIdAccount() {
        return senderIdAccount;
    }

    public void setSenderIdAccount(Account senderIdAccount) {
        this.senderIdAccount = senderIdAccount;
    }

    public Double getBalancePlus() {
        return balancePlus;
    }

    public void setBalancePlus(Double balancePlus) {
        this.balancePlus = balancePlus;
    }

    @Override // Перевизначаємо метод
    public String toString() { // До строкового виду
        return "Transaction{" +
                "id=" + id +
                ", client=" + client +
                ", beneficiaryIdAccount=" + beneficiaryIdAccount +
                ", senderIdAccount=" + senderIdAccount +
                ", balancePlus=" + balancePlus +
                '}';
    }
}
