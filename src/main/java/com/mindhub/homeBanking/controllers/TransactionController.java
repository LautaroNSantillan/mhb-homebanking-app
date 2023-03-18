package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.PDFExporter.PDFExporter;
import com.mindhub.homeBanking.dtos.TransactionFilterDTO;
import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.repositories.ClientRepository;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import com.mindhub.homeBanking.utilities.ErrorResponse;
import com.mindhub.homeBanking.utilities.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accRepo;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private TransactionRepository transactionRepo;

    @Transactional
    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/transaction")
    public ResponseEntity<Object> makeTransaction(@RequestParam String originAccNumber, @RequestParam String destinationAccNumber, @RequestParam double amount, @RequestParam String description, Authentication auth) {

        if (originAccNumber.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Missing origin account", null), HttpStatus.FORBIDDEN);
        }
        if (destinationAccNumber.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Missing destination account", null), HttpStatus.FORBIDDEN);
        }
        if (amount < 1) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Invalid amount", null), HttpStatus.FORBIDDEN);
        }
        if (description.length() > 100) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Description exceeds maximum length of 100 characters", null), HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty()) {
            description = "No description provided";
        }
        if (originAccNumber.equals(destinationAccNumber)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "It is not possible to send money to the same account", null), HttpStatus.FORBIDDEN);
        }
        if (!accRepo.existsByNumber(originAccNumber)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Origin Account doesn't exist", null), HttpStatus.FORBIDDEN);
        }
        if (!accRepo.existsByNumber(destinationAccNumber)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Destination Account doesn't exist", null), HttpStatus.FORBIDDEN);
        }

        Client sender = clientRepo.findByEmail(auth.getName());
        Client destination = accRepo.findByNumber(destinationAccNumber).getClient();

        Account originAcc = accRepo.findByNumber(originAccNumber);
        Account destinationAcc = accRepo.findByNumber(destinationAccNumber);

        if (!sender.getAccounts().contains(originAcc)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "You don't posses this account", null), HttpStatus.FORBIDDEN);
        }

        if (!accRepo.existsByNumber(destinationAccNumber)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Destination account doesn't exist", null), HttpStatus.FORBIDDEN);
        }
        try {
            Transaction withdrawTransaction = originAcc.withdraw(amount, description, originAcc, transactionRepo, accRepo, originAcc.getBalance());
            Transaction depositTransaction = destinationAcc.deposit(amount, description, originAcc.getClient(), destinationAcc, transactionRepo, accRepo);
            return new ResponseEntity<>("Transaction successful", HttpStatus.CREATED);
        } catch (InsufficientFundsException e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Insufficient funds", null), HttpStatus.FORBIDDEN);
        }

    }
    @PostMapping("/transaction/export-to-pdf")
    public void exportToPDF( @RequestBody TransactionFilterDTO filter, Authentication auth, HttpServletResponse response) throws ParseException, IOException {

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

        response.setContentType("application/type");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=transactionsFor" + currentAcc.getId() +"from"+ fromDateStr+"to"+toDateStr+".pdf";

        response.setHeader(headerKey, headerValue);

        PDFExporter exporter = new PDFExporter(transactions);
        exporter.export(response, start, end);

    }

    @PostMapping("/getrans")
    public List<Transaction> getrans (@RequestBody TransactionFilterDTO filter, Authentication auth){
        Client currentClient = clientRepo.findByEmail(auth.getName());
        Account currentAcc = accRepo.findByNumber(filter.getAccountNumber());

        LocalDateTime start = filter.getFromDate().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = filter.getToDate().withHour(23).withMinute(59).withSecond(59);
      return transactionRepo.findByIdAndDateBetween(
                currentAcc.getId(), start, end);

    }

    @PostMapping("/getrans2")
    public ResponseEntity<?> getTrans(@RequestBody TransactionFilterDTO filter, Authentication auth) throws IOException {
        Client currentClient = clientRepo.findByEmail(auth.getName());
        Account currentAcc = accRepo.findByNumber(filter.getAccountNumber());

        LocalDateTime fromDate = filter.getFromDate().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime toDate = filter.getToDate().withHour(23).withMinute(59).withSecond(59);

        LocalDateTime start = filter.getFromDate().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = filter.getToDate().withHour(23).withMinute(59).withSecond(59);

        List<Transaction> transactions = transactionRepo.findByIdAndDateBetween(
                currentAcc.getId(), start, end);

        PDFExporter pdfExporter = new PDFExporter(transactions);
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
