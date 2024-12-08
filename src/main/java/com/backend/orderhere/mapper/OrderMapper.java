package com.backend.orderhere.mapper;

import com.backend.orderhere.auth.KeyCloakService;
import com.backend.orderhere.dto.order.OrderGetDTO;
import com.backend.orderhere.dto.order.PlaceOrderDTO;
import com.backend.orderhere.dto.order.UpdateOrderStatusDTO;
import com.backend.orderhere.model.Order;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(source = "restaurant.restaurantId", target = "restaurantId")
    @Mapping(target = "username", expression = "java(keyCloakService.getUsernameFromKeycloak(order.getUserId()))")
    OrderGetDTO fromOrderToOrderGetDTO(Order order, @Context KeyCloakService keyCloakService);

    UpdateOrderStatusDTO fromOrdertoUpdateOrderStatusDTO(Order updatedOrder);

    Order dtoToOrder(PlaceOrderDTO dto);
}

