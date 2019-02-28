package application.model;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javafx.stage.FileChooser;

public class TransactionsLoad {
	
	public static int statementNumber = 0;

	/*
	 * Allows the user to go and a PDF file in order to load in. Currently will only
	 * work with a KiwiBank Statement.
	 * 
	 */
	public static File chooseFile() {
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(filter);
		fileChooser.setTitle("Select a PDF version of your bank statement");
		return fileChooser.showOpenDialog(null);
	}

	/*
	 * Goes through each line in the Bank Statements and figures out which lines are
	 * outgoing transactions. It then creates an ArrayList containing both the date
	 * and amount of each transaction.
	 * 
	 * @return returns a ArrayList of Strings that contains each outgoing
	 * transaction
	 */
	public static List<String> getTransactions() throws IOException, ClassNotFoundException, SQLException {

		String[] lines;
		List<String> transactions = new ArrayList<>();

		// FileChooser to pick PDF file
		File file = chooseFile();

		// Load the file picked with the FileChooser
		PDDocument document = PDDocument.load(file);

		// Instantiate PDFTextStripper class

		PDFTextStripper pdfStripper = new PDFTextStripper();

		// Retrieving text from PDF document
		String text = pdfStripper.getText(document);

		if (text.contains("Kiwibank Limited")) {

			// Closing the document
			document.close();

			// Find the Statement Number
			int stmntIndex = text.indexOf("Statement number") + 17;
			statementNumber = Integer.parseInt(text.substring(stmntIndex, stmntIndex + 3));

			// Only want the debit account transactions, ignore the rest.
			int i0 = text.indexOf("Opening Account Balance");
			int i1 = text.indexOf("Closing Account Balance");

			// Separates into an array
			lines = text.substring(i0, i1).split(System.getProperty("line.separator"));

			// Join transactions that are to long to fit into one line on the bank
			// statement.
			for (int i = lines.length - 1; i > 1; i--) {
				if (!(Character.isDigit(lines[i].charAt(0)))) {
					lines[i - 1] = lines[i - 1] + " " + lines[i];
				}
			}

			// Remove all transfers and interest into debit card, only looking for
			// expenditure.
			for (int i = 0; i < lines.length; i++) {
				if (lines[i].contains("TRANSFER FROM A J COCHRANE") || lines[i].contains("INTEREST") || lines[i].contains("Insuff")) {
					lines[i] = "";
				}
			}

			// Remove last line.
			lines[lines.length - 1] = "";

			// Process each line and find the outgoing transactions
			int index = 0;
			String secondary = "";
			// Go through each line and manipulate the String to find the date and
			// transaction amount
			
			for (int i = 0; i < lines.length - 1; i++) {
				System.out.println(lines[i]);
				if (!(lines[i].isEmpty()) && Character.isDigit(lines[i].charAt(0))) {
					index = lines[i].indexOf("$");
					secondary = lines[i].substring(index + 1, lines[i].length());
					secondary = secondary.substring(0, secondary.indexOf(" "));

					// Add the finalised transactions into an ArrayList we can then return
					transactions.add(lines[i].substring(0, 6) + " " + secondary);
				}
			}
			return transactions;
		}
		System.out.println("Error: Please select a Kiwibank Statement");
		return transactions;
	}
	

}
