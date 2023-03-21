package com.mindhub.homeBanking.utilities;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.mindhub.homeBanking.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Service
public class PDFExporterService {
    private List<Transaction> transactionList;

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.green);
        cell.setPadding(5);

       Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
       font.setColor(Color.MAGENTA);

        cell.setPhrase(new Phrase("Date",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Type",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Sender",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Remaining Balance",font));
        table.addCell(cell);
    }
    private void writeTableData(PdfPTable table ){
        for (Transaction transaction : transactionList){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = transaction.getDate().format(formatter);
            table.addCell(formattedDate);
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(String.valueOf(transaction.getDescription()));
            table.addCell(String.valueOf(transaction.getType()));
            table.addCell(String.valueOf(transaction.getSender() == null ? "N/A" : String.valueOf(transaction.getSender())));
            table.addCell(String.valueOf(transaction.getRemainingBalance()));
        }
    }
    public void export(HttpServletResponse response,  LocalDateTime fromDate, LocalDateTime toDate) throws IOException {
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        document.add(new Paragraph("Transactions "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();
    }

    public byte[] export2(LocalDateTime fromDate, LocalDateTime toDate) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, baos);

        document.open();

        document.add(new Paragraph("Transactions "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

        return baos.toByteArray();
    }

}
