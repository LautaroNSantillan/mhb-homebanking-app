package com.mindhub.homeBanking;

import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.models.loans.DynamicLoan;
import com.mindhub.homeBanking.models.loans.PredefinedLoan;
import com.mindhub.homeBanking.queries.Query;
import com.mindhub.homeBanking.repositories.*;

import com.mindhub.homeBanking.services.impl.AccountServiceImpl;
import com.mindhub.homeBanking.utilities.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class HomeBankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomeBankingApplication.class, args);
	}
	@Autowired
	PasswordEncoder pwdEncoder;
	@Autowired
	AccountServiceImpl accService;

//	@Autowired
//	Query query;

	@Bean public CommandLineRunner initData(ClientRepository clientRepo, AccountRepository accRepo, TransactionRepository transactionRepo, LoanRepository loanRepo, ClientLoanRepository clientLoanRepo, CardsRepository cardRepo) {
		return (args) -> {
//			query.connectToDatabase();
//			query.insertRow();
			//query.addClient("Luka", "Morel", "lm@gmail.com", "123");
//			query.addClient("Mickey", "Morel", "1m@gmail.com", "123");
//			query.addClient("Mickey", "Morel", "2m@gmail.com", "123");
//			query.addClient("Mickey", "Morel", "3m@gmail.com", "123");
//			query.addClient("Mickey", "Morel", "4m@gmail.com", "123");
//			query.fetchClients();
//			query.fetchMickeys();
//			query.fetchAdmins();
//			query.closeConnection();
			Client admin = new Client("admin", "admin", "admin@admin.mindhub",pwdEncoder.encode("123") );
			 Client melba = new Client("Melba", "Morel", "melba@mindhub.com",pwdEncoder.encode("melba") );
			 Client jack = new Client("Jack", "Bauer", "jackb@email.com",pwdEncoder.encode("123") );
			clientRepo.save(melba);
			clientRepo.save(jack);
			clientRepo.save(admin);
			Account VIN001 = new Account (LocalDateTime.now(), 5000, accService,melba,AccountType.SAVINGS);
			accRepo.save(VIN001);
			Account VIN002 = new Account (LocalDateTime.now().plusDays(1), 7500, accService,melba,AccountType.CHECKING);
			accRepo.save(VIN002);
			Account VIN003 = new Account (LocalDateTime.now(), 5000, accService,jack, AccountType.SAVINGS);
			accRepo.save(VIN003);
			Account VIN004 = new Account (LocalDateTime.now().plusDays(1), 7500, accService,jack,AccountType.CHECKING);
			accRepo.save(VIN004);

			Transaction VINtrans01= new Transaction(TransactionType.CREDIT, 2000.00,"1",LocalDateTime.now().plusDays(1),null, VIN001);
			Transaction VINtrans08= new Transaction(TransactionType.CREDIT, 2000.00,"1",LocalDateTime.now().plusDays(1),null, VIN001);
			Transaction VINtrans09= new Transaction(TransactionType.CREDIT, 2000.00,"1",LocalDateTime.now().plusDays(1),null, VIN001);
			Transaction VINtrans010= new Transaction(TransactionType.CREDIT, 2000.00,"1",LocalDateTime.now().plusDays(1),null, VIN001);
			Transaction VINtrans011= new Transaction(TransactionType.CREDIT, 2000.00,"1",LocalDateTime.now().plusDays(1),null, VIN001);
			Transaction VINtrans013= new Transaction(TransactionType.CREDIT, 2000.00,"1",LocalDateTime.now().plusDays(1),null, VIN001);
			Transaction VINtrans014= new Transaction(TransactionType.CREDIT, 2000.00,"1",LocalDateTime.now().plusDays(1),null, VIN001);

			Transaction VINtrans02= new Transaction(TransactionType.DEBIT, 3000.00, "bbb",LocalDateTime.now(),null, VIN001);
			Transaction VINtrans03= new Transaction(TransactionType.DEBIT, 3000.00, "bbb",LocalDateTime.now(),null, VIN002);
			Transaction VINtrans04= new Transaction(TransactionType.DEBIT, 3000.00, "bbb",LocalDateTime.now().plusDays(1),null, VIN003);
			Transaction VINtrans05= new Transaction(TransactionType.DEBIT, 3000.00, "bbb",LocalDateTime.now(),null, VIN004);


//			Loan housingLoan = new Loan(LoanType.MORTGAGE,"housing",500000,new ArrayList<>(Arrays.asList(12,24,36,48,60)));
//			Loan personalLoan = new Loan(LoanType.PERSONAL,"personal",100000,new ArrayList<>(Arrays.asList(6,12,24)));
//		Loan autoLoan = new Loan(LoanType.AUTO,"automobile",300000,new ArrayList<>(Arrays.asList(6,12,24,36)));

			Loan housingLoan = new Loan(PredefinedLoan.MORTGAGE);
			Loan personalLoan = new Loan(PredefinedLoan.PERSONAL);
			Loan autoLoan = new Loan(PredefinedLoan.AUTO);

			DynamicLoan dynamicLoan = new DynamicLoan("DOLLARS", "Dollars",69000, 69, new ArrayList<>(Arrays.asList((byte)69,(byte)70)),false );
			Loan dollarsLoan = new Loan(dynamicLoan);

			ClientLoan melbaHousing = new ClientLoan(400000.00, 60,melba,housingLoan);
			ClientLoan melbaPersonal = new ClientLoan(50000.00, 12,melba,personalLoan);
			ClientLoan jackPersonal = new ClientLoan(100000.00, 24,jack,personalLoan);
			ClientLoan jackAuto = new ClientLoan(200000.00, 36,jack,autoLoan);

			loanRepo.save(housingLoan);
			loanRepo.save(personalLoan);
			loanRepo.save(autoLoan);
			loanRepo.save(dollarsLoan);

//			VIN003.addTransaction(VINtrans01);
//			VIN003.addTransaction(VINtrans03);
//			VIN003.addTransaction(VINtrans04);
//			VIN003.addTransaction(VINtrans05);
//			VIN001.addTransaction(VINtrans02);

//			melba.addAccount(VIN002);
//			melba.addAccount(VIN001);
//			jack.addAccount(VIN003);
//			jack.addAccount(VIN004);

//			melba.addCard(melbaGoldCard);
//			jack.addCard(jackGoldCard);
			Card melbaGoldCard = new Card(melba,CardType.CREDIT, CardColor.GOLD, LocalDate.now(),LocalDate.now().plusDays(5), CardUtils.getCardNumbers());
//			Card melbaTitaniumCard = new Card(melba,CardType.CREDIT, CardColor.TITANIUM, LocalDate.now(),LocalDate.now().plusYears(5));
//			Card jackGoldCard = new Card(jack,CardType.CREDIT, CardColor.SILVER, LocalDate.now(),LocalDate.now().plusYears(6));

			clientRepo.save(melba);
			clientRepo.save(jack);
			clientRepo.save(admin);

			accRepo.save(VIN001);
			accRepo.save(VIN002);
			accRepo.save(VIN003);
			accRepo.save(VIN004);

			transactionRepo.save(VINtrans01);
			transactionRepo.save(VINtrans02);
			transactionRepo.save(VINtrans03);
			transactionRepo.save(VINtrans04);
			transactionRepo.save(VINtrans05);
			transactionRepo.save(VINtrans09);
			transactionRepo.save(VINtrans010);
			transactionRepo.save(VINtrans011);
			transactionRepo.save(VINtrans013);
			transactionRepo.save(VINtrans014);
			transactionRepo.save(VINtrans08);

			clientLoanRepo.save(melbaHousing);
			clientLoanRepo.save(melbaPersonal);
			clientLoanRepo.save(jackAuto);
			clientLoanRepo.save(jackPersonal);

		cardRepo.save(melbaGoldCard);
//		cardRepo.save(melbaTitaniumCard);
//		cardRepo.save(jackGoldCard);


		};
	}
}


