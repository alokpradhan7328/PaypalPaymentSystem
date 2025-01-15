package com.cpt.payments.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cpt.payments.constant.ErrorCodeEnum;
import com.cpt.payments.exception.PaypalProviderException;
import com.cpt.payments.http.HttpClientUtil;
import com.cpt.payments.service.helper.GetOrderRequestHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

	@Mock
	private GetOrderRequestHelper getOrderRequestHelper;

	@Mock
	private HttpClientUtil httpClientUtil;

	@InjectMocks
	private PaymentServiceImpl paymentServiceImpl;

	/**
	 * If makeHttpRequest returns null, 
	 * then getOrder method shoudl throw exception of PaypalProviderException
	 */
	@Test
	public void testNullResponseFromHttpClientUtil() {
		// Call method of PaymentServiceImpl
		// check if response is as expected
		log.info("Test method executed");

		//Arrange
		String orderId = "1234";

		//Act


		PaypalProviderException validationExpResponse = assertThrows(
				PaypalProviderException.class, 
				() -> paymentServiceImpl.getOrder(orderId)
				);

		//Assert / Verify

		assertEquals(ErrorCodeEnum.INVALID_RESPONSE_FROM_PAYPAL.getErrorCode(), 
				validationExpResponse.getErrorCode());
		
		assertEquals(ErrorCodeEnum.INVALID_RESPONSE_FROM_PAYPAL.getErrorMessage(), 
				validationExpResponse.getErrorMessage());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, 
				validationExpResponse.getHttpStatus());
	}
	
	
	/**
	 * If makeHttpRequest returns object of ResponseEntity with null body, 
	 * then getOrder method shoudl throw exception of PaypalProviderException
	 */
	@Test
	public void testNullResponseBodyFromHttpClientUtil() {
		// Call method of PaymentServiceImpl
		// check if response is as expected
		log.info("Test method executed");

		//Arrange
		String orderId = "1234";

		// when httpClientUtil.makeHttpRequest(httpRequest); is called, 
		// then ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
		
		ResponseEntity nullBodyResponse = ResponseEntity.status(
				HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
		when(httpClientUtil.makeHttpRequest(any())).thenReturn(nullBodyResponse);
		
		//Act
		PaypalProviderException validationExpResponse = assertThrows(
				PaypalProviderException.class, 
				() -> paymentServiceImpl.getOrder(orderId)
				);

		//Assert / Verify

		assertEquals(ErrorCodeEnum.INVALID_RESPONSE_FROM_PAYPAL.getErrorCode(), 
				validationExpResponse.getErrorCode());
		
		assertEquals(ErrorCodeEnum.INVALID_RESPONSE_FROM_PAYPAL.getErrorMessage(), 
				validationExpResponse.getErrorMessage());
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, 
				validationExpResponse.getHttpStatus());
	}
}
