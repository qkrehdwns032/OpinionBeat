package com.example.opinionbeat.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Value("${iamport.key}")
    private String impKey;

    @Value("${iamport.secret}")
    private String impSecret;

    private IamportClient iamportClient;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(impKey, impSecret);
    }

    @GetMapping("/test")
    public String testPage(Model model) {
        model.addAttribute("impKey", impKey);
        return "payment/test";
    }

    @PostMapping("/cancel/{impUid}")
    @ResponseBody
    public IamportResponse<Payment> cancelPayment(@PathVariable String impUid) throws IamportResponseException, IOException {
        CancelData cancelData = new CancelData(impUid, true); // 취소할 impUid와 전액 취소 여부
        return iamportClient.cancelPaymentByImpUid(cancelData);
    }

    // 결제 검증을 위한 API
    @PostMapping("/verify/{impUid}")
    @ResponseBody
    public IamportResponse<Payment> verifyPayment(@PathVariable String impUid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(impUid);
    }

    @GetMapping("/success")
    public String success(@RequestParam String impUid,
                          @RequestParam String merchantUid,
                          Model model) {
        try {
            // 결제 정보 조회
            IamportResponse<Payment> payment = iamportClient.paymentByImpUid(impUid);
            model.addAttribute("payment", payment.getResponse());
            return "payment/success";
        } catch (IamportResponseException | IOException e) {
            model.addAttribute("message", e.getMessage());
            return "payment/fail";
        }
    }

    @GetMapping("/fail")
    public String fail(@RequestParam String message, Model model) {
        model.addAttribute("message", message);
        return "payment/fail";
    }
}
