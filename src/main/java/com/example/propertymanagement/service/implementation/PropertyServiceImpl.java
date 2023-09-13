package com.example.propertymanagement.service.implementation;
import com.example.propertymanagement.model.Property;
import com.example.propertymanagement.model.Purchase;
import com.example.propertymanagement.model.User;
import com.example.propertymanagement.model.dto.UserDto;
import com.example.propertymanagement.repository.ImageDataRepository;
import com.example.propertymanagement.repository.PropertyRepository;
import com.example.propertymanagement.repository.PurchaseRepository;
import com.example.propertymanagement.repository.UserRepository;
import com.example.propertymanagement.service.PropertyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final  Logger logger;
    private final PropertyRepository propertyRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ImageDataRepository imageDataRepository;
    private final JwtServiceImpl jwtService;
    private final UserServiceImpl userService;

    public void addProperty(Property property) {
        boolean exists = propertyRepository.findPropertyByAddress(property.getAddress())
                .isPresent();
        if (exists)
            throw new IllegalStateException("properties already in the list");

        propertyRepository.save(property);
    }

    public void checkProperty(String address) {
        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        boolean exists = property.isPresent();
        if (!exists)
            throw new IllegalStateException("properties does not exist");
    }

    public void changePropertyPrice(HttpServletRequest request ,String address, Long price) {
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractEmail(jwt);

        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        checkProperty(address);

        if (property.get().getUsers().contains(userRepository.findUserByEmail(userEmail).get())) {
            logger.trace("property existence checked");
            property.get().setPrice(price);
            propertyRepository.save(property.get());
            logger.trace("property price updated");
        }
        else
            throw new  IllegalStateException("User is not an owner of this property");
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public List<UserDto> getPropertyOwnerDtos(String address) {
        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        checkProperty(address);
        return userRepository.findUsersByPropertiesId(property.get().getId())
                .stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public List<User> getPropertyOwners(String address) {
        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        checkProperty(address);
        return userRepository.findUsersByPropertiesId(property.get().getId());

    }

    public void deleteProperty(@NonNull HttpServletRequest request, String address) {
        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        checkProperty(address);

        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractEmail(jwt);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        List<User> users = getPropertyOwners(address);

        if (users.contains(user.get())) {
            Set<User> deleteRequests;
            deleteRequests = property.get().getDeleteRequests();
            deleteRequests.add(user.get());
            property.get().setDeleteRequests(deleteRequests);
            propertyRepository.save(property.get());


            if (property.get().getDeleteRequests().size() == users.size()) {
                removeDeletionFromAllUsers(users, address);

                userService.removePropertyFromAllUsers(users, address);

                imageDataRepository.deleteImageDataByPropertyAddress(address);

                propertyRepository.deleteById(property.get().getId());

                purchaseRepository.deletePurchaseByPropertyAddress(address);
            }
        } else
            throw new IllegalStateException("User is not an owner of this properties");

    }

    public void approveDeletion(String email, String address) {
        Optional<User> user = userRepository.findUserByEmail(email);
        checkUser(email);

        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
       checkProperty(address);

        Set<User> users = null;
        users = property.get().getDeleteRequests();
        users.add(user.get());
        property.get().setDeleteRequests(users);
        propertyRepository.save(property.get());

        logger.info("deletion of " + address + " from database approved by: " + email);
    }
    public void removeDeletion(String email, String address) {
        Optional<User> user = userRepository.findUserByEmail(email);
        checkUser(email);

        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        checkProperty(address);
        Set<Property> properties;
        properties = user.get().getDeleteApprovals();
        properties.remove(property.get());
        user.get().setDeleteApprovals(properties);
        userRepository.save(user.get());
    }
    public void removeApproval(String email, String address) {
        Optional<User> user = userRepository.findUserByEmail(email);
        checkUser(email);

        Optional<Purchase> purchase = purchaseRepository
                                .findPurchaseByPropertyAddress(address);
        Set<Purchase> approvals;
        approvals = user.get().getPurchaseApprovals();
        approvals.remove(purchase.get());
        user.get().setPurchaseApprovals(approvals);
        userRepository.save(user.get());
        logger.info("Purchase approval removed from " + email);
    }
    public void removeApprovalFromAllUsers(List<User> users, String address) {
        for (User user : users) {
            removeApproval(user.getEmail(), address);
        }
    }
    public void removeDeletionFromAllUsers(List<User> users, String address) {
        for (User user : users) {
            removeDeletion(user.getEmail(), address);
        }
    }
    public void checkUser(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        boolean exists = user.isPresent();
        if (!exists)
            throw new IllegalStateException("user doesn't exist");
    }

    @Override
    public void requestToPurchaseProperty(@NonNull HttpServletRequest request,
                                          String address,
                                          Long offer) {
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractEmail(jwt);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        checkProperty(address);
        if (offer < property.get().getPrice()) {
            throw new IllegalStateException("offer is less than the minimal price");
        }
        if (user.get().getBudget() >= property.get().getPrice()) {
            Purchase purchase = new Purchase();
            purchase.setPropertyAddress(address);
            purchase.setBuyerEmail(userEmail);
            purchase.setOffer(offer);
            purchaseRepository.save(purchase);
        }
        else
            throw new IllegalStateException
                    ("User budget is not enough to purchase this property");
    }

    @Override
    public void approvePurchase(@NonNull HttpServletRequest request,
                                String address, String email) {

        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractEmail(jwt);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        Optional<Purchase> purchase = purchaseRepository
                .findPurchaseByBuyerEmailAndPropertyAddress(email, address);

        if (!purchase.isPresent())
            throw new IllegalStateException("Purchase offer does not exist");

        checkProperty(address);

        if (getPropertyOwners(address).contains(user.get())) {
          Set<User> approvers = purchase.get().getPurchaseApprovers();
          approvers.add(user.get());
          purchase.get().setPurchaseApprovers(approvers);
          purchaseRepository.save(purchase.get());

          List<User> users = getPropertyOwners(address);
            if (purchase.get().getPurchaseApprovers().size() == users.size()) {

                removeApprovalFromAllUsers(users, address);

                changeOwners(purchase.get().getBuyerEmail(), address);

                purchaseRepository.deletePurchaseById(purchase.get().getId());

            }
        } else
            throw new IllegalStateException("user is not an owner of this property and" +
                    " therefore can not approve its purchase offers");

    }
    public void changeOwners(String email ,String address) {
        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        Optional<User> user = userRepository.findUserByEmail(email);
        checkUser(email);
        checkProperty(address);
        logger.info("Past owners of " + address + ": "  + getPropertyOwners(address).toString());
        userService.removePropertyFromAllUsers(getPropertyOwners(address), address);

        Set<Property> properties = user.get().getProperties();
        properties.add(property.get());
        user.get().setProperties(properties);
        logger.info("New owner of " + address + ": " + email);
        userRepository.save(user.get());

    }
}