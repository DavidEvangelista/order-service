package br.com.project.orderservice.adapter.input;

import br.com.project.orderservice.core.domain.dto.OrderDto;
import br.com.project.orderservice.core.domain.enums.StatusOrder;
import br.com.project.orderservice.core.port.input.ManagerOrderServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ManagerOrderServicePort orderService;

    @PostMapping
    public ResponseEntity<String> sendOrder(@Valid @RequestBody OrderDto order) {
        orderService.sendOrder(order);
        return new ResponseEntity<>("Order sent for processing", HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}/updateStatus/{status}")
    public ResponseEntity<String> updateStatusOrder(@PathVariable String id, @PathVariable StatusOrder status) {
        orderService.updateStatusOrder(id, status);
        return new ResponseEntity<>("Order updated", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String id) {
        return new ResponseEntity<>(orderService.getOrderById(UUID.fromString(id)), HttpStatus.OK);
    }

    @GetMapping("/customer/{document}")
    public Page<OrderDto> getOrdersByCustomerId(@PathVariable String document,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrdersByCustomerDocument(document, page, size);
    }

}
