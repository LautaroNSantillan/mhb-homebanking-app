package com.mindhub.homeBanking.utils;

import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.utilities.CardUtils;
import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
//public class CardUtilsTest {
//    @Test
//    public void cardNumberIsCreated(){
//
//        String cardNumber = CardUtils.getCardNumbers();
//        SoftAssertions soft = new SoftAssertions();
//
//          soft.assertThat(cardNumber)
//                  .isNotEmpty()
//                  .isNotBlank()
//                  .isNotNull();
//                 // .isEqualTo("Some random Number");
//          soft.assertAll();
//
//         assertThat(cardNumber)
//                 .matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}");
//    }
//
//    @Test
//    public void getCvv(){
//        String cvv = CardUtils.getCvv();
//
//        assertThat(cvv)
//                .isNotEmpty()
//                .isNotBlank()
//                .isNotNull()
//                .matches("\\d{3}");
//    }
//}
