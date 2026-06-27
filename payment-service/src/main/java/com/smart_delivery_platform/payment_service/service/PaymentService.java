package com.smart_delivery_platform.payment_service.service;

import com.smart_delivery_platform.payment_service.dto.PaymentFailedEvent;
import com.smart_delivery_platform.payment_service.dto.PaymentRefundedEvent;
import com.smart_delivery_platform.payment_service.dto.PaymentSuccessEvent;
import com.smart_delivery_platform.payment_service.entity.PaymentEntity;
import com.smart_delivery_platform.payment_service.repository.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public  class PaymentService  implements IPaymentService{
    private  final PaymentRepo paymentRepo;
    private  final PaymentProducer paymentProducer;

  public void  save(PaymentSuccessEvent event){
      PaymentEntity paymentEntity = findExistingPayment(event.getOrderId(), event.getPaymentId());
      boolean isNewPayment = paymentEntity == null;
      if (isNewPayment) {
          paymentEntity = new PaymentEntity();
          paymentEntity.setCreatedAt(LocalDateTime.now());
      } else {
          paymentEntity.setUpdatedAt(LocalDateTime.now());
      }

      paymentEntity.setPaymentId(event.getPaymentId());
      paymentEntity.setAmount(event.getAmount());
      paymentEntity.setPaymentStatus(event.getStatus());
      paymentEntity.setFailureReason(null);
      paymentEntity.setOrderId(event.getOrderId());
      paymentRepo.save(paymentEntity);
  }
  public void  save(PaymentFailedEvent event){
      PaymentEntity paymentEntity = findExistingPayment(event.getOrderId(), event.getPaymentId());
      boolean isNewPayment = paymentEntity == null;
      if (isNewPayment) {
          paymentEntity = new PaymentEntity();
          paymentEntity.setCreatedAt(LocalDateTime.now());
      } else {
          paymentEntity.setUpdatedAt(LocalDateTime.now());
      }

      paymentEntity.setPaymentId(event.getPaymentId());
      paymentEntity.setAmount(event.getAmount());
      paymentEntity.setPaymentStatus(event.getStatus());
      paymentEntity.setFailureReason(event.getReason());
      paymentEntity.setOrderId(event.getOrderId());
      paymentRepo.save(paymentEntity);
  }

  public PaymentEntity isOrderExist(String orderId){
     return paymentRepo.findByOrderId(orderId).orElse(null);
    }

    public boolean updatePaymentStatus(String orderId, String status) {
        PaymentEntity payment = paymentRepo.findByOrderId(orderId).orElse(null);
        if (payment == null) {
            return false;
        }
        payment.setPaymentStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepo.save(payment);
        return true;
    }

   public String refundInitiation(String orderId){
     PaymentEntity paymentEntity = paymentRepo.findByOrderId(orderId).orElse(null);
     if(!paymentEntity.getPaymentStatus().equals("REFUND_FAILED")){
         return "Not Eligible For Refund";
     }
       PaymentRefundedEvent paymentRefundedEvent = PaymentRefundedEvent.builder()
               .paymentId(paymentEntity.getPaymentId())
               .orderId(orderId)
               .refundAmount(paymentEntity.getAmount())
               .status("PAYMENT-REFUNDED")
               .build();
       paymentProducer.refundInitiationEvent(paymentRefundedEvent);
       return "Payment Refund sucessfully";
   }

   private PaymentEntity findExistingPayment(String orderId, String paymentId) {
      PaymentEntity paymentEntity = paymentRepo.findByOrderId(orderId).orElse(null);
      if (paymentEntity != null || paymentId == null) {
          return paymentEntity;
      }
      return paymentRepo.findByPaymentId(paymentId).orElse(null);
   }


}
