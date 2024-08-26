package com.backend.orderhere.dto.order;

import com.backend.orderhere.model.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusDTO {

  private Integer orderId;
  private String orderStatus;
}
