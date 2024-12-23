package org.example;

import javax.persistence.*;

@Entity // Анотація, яка вказує Hibernate, що наш клас є спеціальним і його об'єкти потрібно зберігати в базі даних
@Table(name = "Rates_of_exchange") // Встановлюємо назву таблиці
public class RateOfExchange {

    @Id // Анотація, за допомогою якої задаємо PrimaryKey
    @GeneratedValue // Автогенерація номера ID
    @Column(name = "id_rate") // Встановлюємо ім'я для колонки таблиці
    private Long id;

    @Column // Колонка в таблиці має назву аналогічно цьому полю
    private String currency;

    @Column // Колонка в таблиці має назву аналогічно цьому полю
    private Double rateToUAH;

    public RateOfExchange() {} // Конструктор за замовчуванням

    public RateOfExchange(String currency, Double rateToUAH) { // Конструктор цього класу з параметрами
        this.currency = currency;
        this.rateToUAH = rateToUAH;
    }

    // Геттери та Сеттери
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

    public Double getRateToUAH() {
        return rateToUAH;
    }

    public void setRateToUAH(Double rateToUAH) {
        this.rateToUAH = rateToUAH;
    }

    @Override // Переозначаємо метод
    public String toString() { // У строковий вигляд
        return "RateOfExchange{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                ", rateToUAH=" + rateToUAH +
                '}';
    }
}
