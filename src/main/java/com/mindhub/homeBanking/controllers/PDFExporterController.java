package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.TransactionFilterDTO;
import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.Transaction;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.repositories.ClientRepository;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import com.mindhub.homeBanking.utilities.PDFExporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequestMapping("/api")
@Controller
public class PDFExporterController {

    private final PDFExporterService pdfExporterService;
    public PDFExporterController(PDFExporterService pdfExporterService) {
        this.pdfExporterService = pdfExporterService;
    }

    @Autowired
    private AccountRepository accRepo;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private TransactionRepository transactionRepo;

    @PostMapping("/export-to-PDF-stream-output")
    public void exportToPDF(@RequestBody TransactionFilterDTO filter, Authentication auth, HttpServletResponse response) throws ParseException, IOException {

        Client currentClient = clientRepo.findByEmail(auth.getName());
        Account currentAcc = accRepo.findByNumber(filter.getAccountNumber());

        LocalDate fromDate = filter.getFromDate().toLocalDate();
        LocalDate toDate = filter.getToDate().toLocalDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDateStr = fromDate.format(formatter);
        String toDateStr = toDate.format(formatter);

        LocalDateTime start = filter.getFromDate().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = filter.getToDate().withHour(23).withMinute(59).withSecond(59);

        List<Transaction> transactions = transactionRepo.findByIdAndDateBetween(
                currentAcc.getId(), start, end);

        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=transactionsFor" + currentAcc.getId() +"from"+ fromDateStr+"to"+toDateStr+".pdf";

        response.setHeader(headerKey, headerValue);

        this.pdfExporterService.setTransactionList(transactions);
        this.pdfExporterService.export(response, start, end);
    }

    @PostMapping("/get-filtered-transactions")
    public List<Transaction> getFilteredTransactions (@RequestBody TransactionFilterDTO filter, Authentication auth){
        Client currentClient = clientRepo.findByEmail(auth.getName());
        Account currentAcc = accRepo.findByNumber(filter.getAccountNumber());

        LocalDateTime start = filter.getFromDate().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = filter.getToDate().withHour(23).withMinute(59).withSecond(59);
        return transactionRepo.findByIdAndDateBetween(
                currentAcc.getId(), start, end);

    }

    @PostMapping("/export-to-PDF-bytes")
    public ResponseEntity<?> exportToPDFBytes(@RequestBody TransactionFilterDTO filter, Authentication auth) throws IOException {
        Client currentClient = clientRepo.findByEmail(auth.getName());
        Account currentAcc = accRepo.findByNumber(filter.getAccountNumber());

        LocalDateTime fromDate = filter.getFromDate().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime toDate = filter.getToDate().withHour(23).withMinute(59).withSecond(59);

        LocalDateTime start = filter.getFromDate().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = filter.getToDate().withHour(23).withMinute(59).withSecond(59);

        List<Transaction> transactions = transactionRepo.findByIdAndDateBetween(
                currentAcc.getId(), start, end);

        com.mindhub.homeBanking.utilities.PDFExporterService pdfExporter = new PDFExporterService(transactions);
        byte[] pdfBytes = pdfExporter.export2(start, end);

        String fileName = String.format("transactionsFor%sFrom%sTo%s.pdf",
                currentAcc.getNumber(), fromDate.toString(), toDate.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "attachment; filename=" + fileName);
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}
