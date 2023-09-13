package com.example.propertymanagement.controller;

import com.example.propertymanagement.model.Property;
import com.example.propertymanagement.model.dto.UserDto;
import com.example.propertymanagement.service.PropertyService;
import com.example.propertymanagement.service.implementation.PropertyServiceImpl;
import com.example.propertymanagement.service.implementation.UserServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final UserServiceImpl userService;

    @PostMapping("add_property")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROPERTY_ADMIN')")
    public void addProperty(@RequestBody Property property) {
        propertyService.addProperty(property);
    }

    @PostMapping("add_property/{email}/{address}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROPERTY_ADMIN')")
    public void addPropertyToUser(@PathVariable("email") String email,
                                  @PathVariable("address") String address) {
        userService.addPropertyToUser(email, address);
    }

    @DeleteMapping("deletePropertyFromUser/{email}/{address}")
    @PreAuthorize("hasAnyAuthority('ADMIN','PROPERTY_ADMIN')")
    public void removePropertyFromUser(@PathVariable("email") String email,
                                       @PathVariable("address") String address) {
        userService.removePropertyFromUser(email, address);
    }

    @GetMapping("{address}/get_owners")
    @PreAuthorize("hasAnyAuthority('ADMIN','PROPERTY_ADMIN', 'SUPPORT')")
    public List<UserDto> getPropertyOwners(@PathVariable("address") String address) {
        return propertyService.getPropertyOwnerDtos(address);
    }

    @DeleteMapping("delete-property/{address}")
    public void deleteProperty(@NonNull HttpServletRequest request,
                               @PathVariable("address") String address) {
        propertyService.deleteProperty(request, address);
    }

    @PostMapping("approveDeletion/{email}/{address}")
    public void approveDeletion(@PathVariable("email") String email,
                                @PathVariable("address") String address) {
        propertyService.approveDeletion(email, address);
    }

    @GetMapping("get-user-properties/{email}")
    public List<Property> getUserProperties(@PathVariable("email") String email) {
        return userService.getUserProperties(email);
    }

    @PostMapping("request-to-purchase-property/{address}/{offer}")
    public void requestToPurchaseProperty(@NonNull HttpServletRequest request,
                                          @PathVariable("address") String address,
                                          @PathVariable("offer") Long offer) {
        propertyService.requestToPurchaseProperty(request,address,offer);
    }
    @PostMapping("approve-purchase/{address}/{email}")
    public void approvePurchase(@NonNull HttpServletRequest request,
                                @PathVariable("address") String address,
                                @PathVariable("email") String email) {
        propertyService.approvePurchase(request,address,email);
    }
    @PutMapping("change-property-price/{address}/{price}")
    public void changePropertyPrice(HttpServletRequest request,
                                    @PathVariable("address") String address,
                                    @PathVariable("price") Long price) {
        propertyService.changePropertyPrice(request,address,price);
    }
}
