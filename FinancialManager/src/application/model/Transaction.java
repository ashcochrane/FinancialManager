package application.model;

import java.time.MonthDay;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.*;

public class Transaction {
	private IntegerProperty stmtNum;
	private ObjectProperty<MonthDay> date;
	private DoubleProperty amount;
	private StringProperty stringDate;
	
	/*
	 * Initialise the constructor.
	 * Each line of the bank statement is a transaction.
	 * 
	 */
	public Transaction(int stmtNum, MonthDay date, Double amount) {
		this.stmtNum = new SimpleIntegerProperty(stmtNum);
		this.amount = new SimpleDoubleProperty(amount);
		this.date = new SimpleObjectProperty<>(date);
		this.stringDate = new SimpleStringProperty(date.format(DateTimeFormatter.ofPattern("MMMM dd")));
	}

	public IntegerProperty getStmtNumProperty() {
		return this.stmtNum;
	}
	
	public int getStmtNum() {
		return stmtNum.get();
	}

	public void setStmtNum(Integer stmtNum) {
		this.stmtNum.set(stmtNum);
	}

	public ObjectProperty<MonthDay> getDateProperty() {
		return this.date;
	}
	
	public MonthDay getDate() {
		return date.get();
	}
	
	public DoubleProperty getAmountProperty() {
		return this.amount;
	}

	public Double getAmount() {
		return amount.get();
	}

	public void setAmount(Double amount) {
		this.amount.set(amount);
	}
	
	public StringProperty getStringDateProperty() {
		return this.stringDate;
	}
	
	public String getStringDate() {
		return stringDate.get();
	}
	
}
