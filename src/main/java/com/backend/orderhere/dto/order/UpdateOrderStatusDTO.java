package com.backend.orderhere.dto.order;

import lombok.Data;

@Data
public class UpdateOrderStatusDTO {

  private Integer orderId;
  private String orderStatus;
}
