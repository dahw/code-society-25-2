/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.codedifferently.lesson14.ecommerce;

public class OrderNotFoundException extends Exception {
  public OrderNotFoundException(String orderId) {
    super("Order with ID " + orderId + " not found");
  }

  public OrderNotFoundException(String orderId, String operation) {
    super("Operation '" + operation + "' is not supported for order with ID " + orderId);
  }
}
