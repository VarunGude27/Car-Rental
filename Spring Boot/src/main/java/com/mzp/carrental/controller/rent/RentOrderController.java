package com.mzp.carrental.controller.rent;

import com.mzp.carrental.dto.RentalOrderDTO;
import com.mzp.carrental.entity.Cars.Car;
import com.mzp.carrental.entity.Rent.RentalOrder;
import com.mzp.carrental.entity.Users.Customer;
import com.mzp.carrental.repository.Car.CarRepo;
import com.mzp.carrental.service.Customer.CustomerService;
import com.mzp.carrental.service.NotificationService;
import com.mzp.carrental.service.OurUserDetailsService;
import com.mzp.carrental.service.car.CarService;
import com.mzp.carrental.service.rent.RentService;
import com.mzp.carrental.service.rent.RentalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rent/orders")
public class RentOrderController {

    @Autowired
    private RentalOrderService rentalOrderService;

    //---------------------------------
    @Autowired
    private CarService carService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RentService rentService;
    //------------------------------------------------------

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OurUserDetailsService ourUserDetailsService;


    @Autowired
    private CarRepo carRepo;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody RentalOrderDTO orderDto) {
        System.out.println("Received Order DTO: " + orderDto.toString());
        try {
            rentalOrderService.createRentalOrder(orderDto);
            Optional<Car> car = carRepo.findById(orderDto.getCarId());

            if (car.isPresent()) {
                if(car.get().getAvailable()){
                    //System.out.println("Order is made because car is available");
                    Integer agencyId = car.get().getAgency().getId(); // ✅ Get Integer agencyId
                    notificationService.sendMessageToAgency(agencyId,
                            "A customer has ordered " + car.get().getBrand() + " " + car.get().getModel());
                    return ResponseEntity.ok("Order successfully created!");
                }else{
                    //System.out.println("Car is not available at the moment");
                    return ResponseEntity.ok("The car is not available at the moment!");
                }

            }else{
                return ResponseEntity.ok("Car not found!");
            }


        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }



    // Update rental order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestParam("status") RentalOrder.OrderStatus status) {
        try {
            rentalOrderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok("Order status updated!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/{carId}/orders")
    public ResponseEntity<List<RentalOrderDTO>> getOrdersByCar(@PathVariable Long carId) {
        List<RentalOrderDTO> orders = rentalOrderService.getFilteredOrdersByCar(carId);
        return ResponseEntity.ok(orders);
    }




    // Cancel a rental order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        try {
            rentalOrderService.cancelRentalOrder(orderId);
            return ResponseEntity.ok("Order canceled successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    //-------------------------------

    @GetMapping("/customer")
    public ResponseEntity<List<RentalOrderDTO>> getOrdersByCustomer() {
        List<RentalOrderDTO> orders = rentalOrderService.getFilteredOrdersByCustomer();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/agency")
    public ResponseEntity<List<RentalOrderDTO>> getOrdersByAgency() {
        System.out.println("Getting list of orders for agency");
        Integer agencyId = carService.getCurrentAgency().getId();
        List<RentalOrder> orders = rentalOrderService.getOrdersByAgency(agencyId);

        // Map RentalOrder entities to RentalOrderDTO
        List<RentalOrderDTO> dtos = orders.stream()
                .map(order -> {
                    RentalOrderDTO dto = new RentalOrderDTO();
                    dto.setId(order.getId());
                    dto.setCarId(order.getCar().getId()); // Ensure Car is fetched eagerly or use a join fetch query
                    dto.setCustomerId(order.getCustomer().getId());
                    dto.setCustomerName(order.getCustomer().getUsername());
                    dto.setCarBrand(order.getCar().getBrand());
                    dto.setCarModel(order.getCar().getModel());
                    dto.setStartDate(order.getStartDate());
                    dto.setEndDate(order.getEndDate());
                    dto.setPickUpLocation(order.getPickUpLocation());
                    dto.setDropOffLocation(order.getDropOffLocation());
                    dto.setIncludeDriver(order.isIncludeDriver());
                    dto.setTotalPrice(order.getTotalPrice());
                    dto.setStatus(order.getStatus());
                    dto.setCustomerPhoneNumber(order.getCustomerPhoneNumber());
                    dto.setCustomerVerificationStatus(order.getCustomer().getVerificationStatus().toString());
                    return dto;
                })
                .collect(Collectors.toList());

        return dtos.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/agency")
    public ResponseEntity<?> getOrderDetailsForAgency(@PathVariable Long id) {
        try {
            RentalOrder order = rentalOrderService.getRentalOrderById(id);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }
            // Fetch car details and customer details here
            Car car = carService.getCarById(order.getCar().getId());
            Customer customer = customerService.getCustomerById(order.getCustomer().getId());

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.getId());
            response.put("carId", car.getId());
            response.put("carImage", car.getImageData()); // Assuming you have a way to encode image to Base64 or URL
            response.put("carBrandModel", car.getBrand() + " " + car.getModel());
            response.put("customerId", customer.getId());
            response.put("customerName", customer.getUsername());
            response.put("startDate", order.getStartDate());
            response.put("endDate", order.getEndDate());
            response.put("pickUpLocation", order.getPickUpLocation());
            response.put("dropOffLocation", order.getDropOffLocation());
            response.put("includeDriver", order.isIncludeDriver());
            response.put("driverFeePerDay", car.getDriverFeePerDay());
            response.put("carPricePerDay", car.getPricePerDay());
            long totalRentDays = ChronoUnit.DAYS.between(order.getStartDate(), order.getEndDate()) + 1;
            response.put("totalRentDays", totalRentDays);
            double totalPrice = totalRentDays * (car.getPricePerDay() + (order.isIncludeDriver() ? car.getDriverFeePerDay() : 0));
            response.put("totalAmount", totalPrice);
            response.put("orderStatus", order.getStatus());
            response.put("customerPhoneNumber", order.getCustomerPhoneNumber());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching order details");
        }
    }






}

