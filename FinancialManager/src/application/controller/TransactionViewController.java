package application.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.MonthDay;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import application.model.Transaction;
import application.model.TransactionDAO;
import application.model.TransactionsLoad;
import application.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class TransactionViewController {

	private List<String> stringTransactions = new ArrayList<>();
	private ObservableList<Transaction> transactions = FXCollections.observableArrayList();
	private Double total = 0.0;
	private Map<String, Double> months = new HashMap<String, Double>();
	private MonthDay currentDate = MonthDay.now();

	@FXML
	private TableView<Transaction> transactionTable;

	@FXML
	private TableColumn<Transaction, Integer> stmtColumn;

	@FXML
	private TableColumn<Transaction, String> dateColumn;

	@FXML
	private TableColumn<Transaction, Double> amountColumn;

	@FXML
	private LineChart<String, Double> transactionGraph;
	
	@FXML
	private TextField stmtText;

	@FXML
	private Text totalCost;

	@FXML
	private Text monthlyCost;

	@FXML
	private void initialize() throws ClassNotFoundException, SQLException {
		
		stmtColumn.setCellValueFactory(cellData -> cellData.getValue().getStmtNumProperty().asObject());
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().getStringDateProperty());
		amountColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountProperty().asObject());

		transactions = DBUtil.dbGetTransactions();
		transactionTable.setItems(transactions);

		updateCosts();
		
		plotGraph();
		
		
		
	}

	@FXML
	private void isPressed(ActionEvent event) throws ClassNotFoundException, SQLException {
		try {
			stringTransactions = TransactionsLoad.getTransactions();
			if (!(stringTransactions.isEmpty()) && DBUtil.dbCheckTable(TransactionsLoad.statementNumber) == 0) {
				for (int i = 0; i < stringTransactions.size(); i++) {
					String date = stringTransactions.get(i).substring(0, stringTransactions.get(i).lastIndexOf(' '));
					String amount = stringTransactions.get(i).substring(stringTransactions.get(i).lastIndexOf(' ') + 1);
					TransactionDAO.insertTrans(TransactionsLoad.statementNumber, date, amount);
				}
			}
			transactions = DBUtil.dbGetTransactions();
			transactionTable.setItems(transactions);

			updateCosts();
			plotGraph();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void deletePressed(ActionEvent event) throws ClassNotFoundException, SQLException {
		try {
			System.out.println(stmtText.getText() + "   " + stmtText.getText().contains("[0-9]+"));
			if (stmtText.getText().matches("[0-9]+")) {
				System.out.println("Success");
				TransactionDAO.deleteStmt(Integer.parseInt(stmtText.getText()));
				transactions = DBUtil.dbGetTransactions();
				transactionTable.setItems(transactions);
				updateCosts();
				plotGraph();
				stmtText.setText("");
			}
		} catch (SQLException e) {
			System.out.println("Could not delete bank statement: 122");
		}
	}
	
	@FXML 
	private void clearAll(ActionEvent event) throws ClassNotFoundException, SQLException {
		try {
			TransactionDAO.clearTable();
			transactions = DBUtil.dbGetTransactions();
			transactionTable.setItems(transactions);
			updateCosts();
			plotGraph();
		} catch (SQLException e) {
			System.out.println("Could not clear Transaction table");
		}
	}
	
	
	
	private void updateCosts() {
		total = 0.0;
		Double jan = 0.0;
		Double feb = 0.0;
		Double mar = 0.0;
		Double apr = 0.0;
		Double may = 0.0;
		Double jun = 0.0;
		Double jul = 0.0;
		Double aug = 0.0;
		Double sep = 0.0;
		Double oct = 0.0;
		Double nov = 0.0;
		Double dec = 0.0;
		DecimalFormat df = new DecimalFormat(".##");
		
		for (Transaction t : transactions) {
			total += t.getAmount();
		}
		if (total == 0) {
			totalCost.setText("$" + total + "0");
		} else {
			totalCost.setText("$" + df.format(total));
		}
		
		for (int i = 0; i < transactions.size(); i++) {
			switch(transactions.get(i).getDate().getMonth()) {
			case JANUARY:
				jan += transactions.get(i).getAmount();
				break;
			case FEBRUARY:
				feb += transactions.get(i).getAmount();
				break;
			case MARCH:
				mar += transactions.get(i).getAmount();
				break;
			case APRIL:
				apr += transactions.get(i).getAmount();
				break;
			case MAY:
				may += transactions.get(i).getAmount();
				break;
			case JUNE:
				jun += transactions.get(i).getAmount();
				break;
			case JULY:
				jul += transactions.get(i).getAmount();
				break;
			case AUGUST:
				aug += transactions.get(i).getAmount();
				break;
			case SEPTEMBER:
				sep += transactions.get(i).getAmount();
				break;
			case OCTOBER:
				oct += transactions.get(i).getAmount();
				break;
			case NOVEMBER:
				nov += transactions.get(i).getAmount();
				break;
			case DECEMBER:
				dec += transactions.get(i).getAmount();
				break;
			}
		}
		months.put("Jan", jan);
		months.put("Feb", feb);
		months.put("Mar", mar);
		months.put("Apr", apr);
		months.put("May", may);
		months.put("Jun", jun);
		months.put("Jul", jul);
		months.put("Aug", aug);
		months.put("Sep", sep);
		months.put("Oct", oct);
		months.put("Nov", nov);
		months.put("Dec", dec);
		
		monthlyCost.setText("$" + months.get(currentDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)));
		
	}
	
	private void plotGraph() {
		XYChart.Series<String, Double> series = new Series<String, Double>();
		
		transactionGraph.getData().clear();
		
		series.setName("Monthly Expenditure");
		series.getData().add(new Data<String, Double>("Jan", months.get("Jan")));
		series.getData().add(new Data<String, Double>("Feb", months.get("Feb")));
		series.getData().add(new Data<String, Double>("Mar", months.get("Mar")));
		series.getData().add(new Data<String, Double>("Apr", months.get("Apr")));
		series.getData().add(new Data<String, Double>("May", months.get("May")));
		series.getData().add(new Data<String, Double>("Jun", months.get("Jun")));
		series.getData().add(new Data<String, Double>("Jul", months.get("Jul")));
		series.getData().add(new Data<String, Double>("Aug", months.get("Aug")));
		series.getData().add(new Data<String, Double>("Sep", months.get("Sep")));
		series.getData().add(new Data<String, Double>("Oct", months.get("Oct")));
		series.getData().add(new Data<String, Double>("Nov", months.get("Nov")));
		series.getData().add(new Data<String, Double>("Dec", months.get("Dec")));
		
		transactionGraph.getData().add(series);
		
	}

}
