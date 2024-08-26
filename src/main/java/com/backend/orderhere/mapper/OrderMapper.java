package com.backend.orderhere.mapper;

import com.backend.orderhere.dto.order.OrderGetDTO;
import com.backend.orderhere.dto.order.PlaceOrderDTO;
import com.backend.orderhere.dto.order.UpdateOrderStatusDTO;
import com.backend.orderhere.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "restaurant.restaurantId", target = "restaurantId")
    OrderGetDTO fromOrderToOrderGetDTO(Order order);

    UpdateOrderStatusDTO fromOrdertoUpdateOrderStatusDTO(Order updatedOrder);

    Order dtoToOrder(PlaceOrderDTO dto);
}

