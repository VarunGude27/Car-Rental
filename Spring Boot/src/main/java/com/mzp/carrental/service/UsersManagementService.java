package com.mzp.carrental.service;

import com.mzp.carrental.dto.ReqRes;
import com.mzp.carrental.entity.Cars.Car;
import com.mzp.carrental.entity.Cars.Ratings;
import com.mzp.carrental.entity.OurUsers;
import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.entity.Users.Customer;
import com.mzp.carrental.repository.Car.CarRepo;
import com.mzp.carrental.repository.Car.RatingsRepo;
import com.mzp.carrental.repository.Customer.CustomerRepo;
import com.mzp.carrental.repository.UsersRepo;
import com.mzp.carrental.repository.agency.AgencyRepo;
import com.mzp.carrental.repository.rent.RentRepo;
import com.mzp.carrental.repository.rent.RentalOrderRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsersManagementService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AgencyRepo agencyRepo;
    @Autowired
    private CustomerRepo customerRepo;



    @Autowired
    private RentRepo rentRepo;

    @Autowired
    private RentalOrderRepo rentalOrderRepo;

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private RatingsRepo ratingRepo;

    private static final Logger log = LoggerFactory.getLogger(UsersManagementService.class);



    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        // Regular expression for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        if(usersRepo.existsByEmail(registrationRequest.getEmail())){
            resp.setStatusCode(400);
            resp.setMessage("Email already exists");
            return resp;
        }

        try {
            // Check if email format is valid
            if (!pattern.matcher(registrationRequest.getEmail()).matches()) {
                resp.setStatusCode(400); // Bad Request
                resp.setMessage("Invalid email format. Please enter a valid email.");
                return resp;
            }

            // Check if Admin role already exists
            if ("Admin".equalsIgnoreCase(registrationRequest.getRole())) {
                boolean adminExists = usersRepo.existsByRole("Admin");
                if (adminExists) {
                    resp.setStatusCode(400); // Bad Request
                    resp.setMessage("An Admin user already exists. Only one Admin is allowed.");
                    return resp;
                }
            }

            // Proceed with registration
            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUser.setAccountStatus("Active");
            OurUsers ourUsersResult = usersRepo.save(ourUser);

            if (ourUsersResult.getId() > 0) {
                if ("Agency".equalsIgnoreCase(ourUsersResult.getRole())) {
                    Agency agency = new Agency();
                    agency.setOurUsers(ourUsersResult);

                    agencyRepo.save(agency);
                } else if ("Customer".equalsIgnoreCase(ourUsersResult.getRole())) {
                    Customer customer = new Customer();
                    customer.setOurUsers(ourUsersResult);
                    customerRepo.save(customer);
                }

                resp.setOurUsers(ourUsersResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }


    public ReqRes login(ReqRes loginRequest){
        ReqRes response = new ReqRes();
        // Regular expression for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        try {
            // Check if email format is valid
            if (!pattern.matcher(loginRequest.getEmail()).matches()) {
                System.out.println("Invalid email: " + loginRequest.getEmail()); // Debugging log
                response.setStatusCode(400);
                response.setMessage("Invalid email format. Please enter a valid email.");
                return response;
            }

            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            if(!user.getAccountStatus().equalsIgnoreCase("Active")){
                response.setStatusCode(400);
                response.setMessage("Your Account has been "+user.getAccountStatus()+". Please contact related Authority or Admin for further instructions");
                System.out.println("Account is banned and " + response);
                return response;
            }
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setId(user.getId());
            response.setEmail(user.getEmail());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

            System.out.println(response.toString());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }





    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        try{
            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }


    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<OurUsers> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }


    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            OurUsers usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setOurUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }


//    @Transactional
//    public ReqRes deleteUser(Integer userId) {
//        ReqRes reqRes = new ReqRes();
//        try {
//            Optional<OurUsers> userOptional = usersRepo.findById(userId);
//            if (userOptional.isPresent()) {
//                OurUsers user = userOptional.get();
//
//                // Check if the user is a Customer
//                Optional<Customer> customerOptional = customerRepo.findByOurUsersId(userId);
//                if (customerOptional.isPresent()) {
//                    Customer customer = customerOptional.get();
//                    log.info("Deleting customer with ID: {}", userId);
//
//                    // Step 1: Delete Rating records for the customer
//                    ratingRepo.deleteByCustomer(customer.getId());
//
//                    // Step 2: Delete Rent records
//                    rentRepo.deleteByCustomer(customer.getId());
//
//                    // Step 3: Delete RentalOrders
//                    rentalOrderRepo.deleteByCustomer(customer.getId());
//
//                    // Step 4: Delete Customer
//                    customerRepo.delete(customer);
//                }
//
//                // Check if the user is an Agency
//                Optional<Agency> agencyOptional = agencyRepo.findByOurUsersId(userId);
//                if (agencyOptional.isPresent()) {
//                    Agency agency = agencyOptional.get();
//                    log.info("Deleting agency with ID: {}", userId);
//
//                    List<Car> cars = carRepo.findByAgency(agency);
//                    for (Car car : cars) {
//                        // Step 1: Delete Rating records for the car
//                        ratingRepo.deleteByCar(car.getId());
//
//                        // Step 2: Delete Rent records for the car
//                        rentRepo.deleteByCar(car.getId());
//
//                        // Step 3: Delete RentalOrders for the car
//                        rentalOrderRepo.deleteByCar(car.getId());
//
//                        // Step 4: Delete the car itself
//                        carRepo.delete(car);
//                    }
//
//                    // Step 5: Delete the Agency
//                    agencyRepo.delete(agency);
//                }
//
//                // Finally, delete the OurUsers record
//                log.info("Deleting user from OurUsers table with ID: {}", userId);
//                usersRepo.delete(user);
//
//                reqRes.setStatusCode(200);
//                reqRes.setMessage("User deleted successfully");
//            } else {
//                log.warn("User not found with ID: {}", userId);
//                reqRes.setStatusCode(404);
//                reqRes.setMessage("User not found for deletion");
//            }
//        } catch (Exception e) {
//            log.error("Error deleting user ID {}: {}", userId, e.getMessage(), e);
//            reqRes.setStatusCode(500);
//            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
//        }
//        return reqRes;
//    }

    public ReqRes updateUser(Integer userId, OurUsers updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                if(updatedUser.getAccountStatus().equalsIgnoreCase("Active") || updatedUser.getAccountStatus().equalsIgnoreCase("Suspended") ||
                        updatedUser.getAccountStatus().equalsIgnoreCase("Banned"))
                {
                    existingUser.setAccountStatus(updatedUser.getAccountStatus());
                    if ((updatedUser.getAccountStatus().equalsIgnoreCase("Suspended") ||
                            updatedUser.getAccountStatus().equalsIgnoreCase("Banned")) &&
                            existingUser.getRole().equalsIgnoreCase("Agency")) {

                        Agency agency = agencyRepo.findByOurUsers_Email(existingUser.getEmail()).orElseThrow();
                        List<Car> cars = carRepo.findByAgency(agency);

                        // Loop through each car and update its availability
                        for (Car car : cars) {
                            car.setAvailable(false);  // Assuming there's a setter method
                        }

                        // Save the updated cars back to the database
                        carRepo.saveAll(cars);
                    }


                }

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsers savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }



    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }

    public ReqRes defaultRegister(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();

        try {
            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setRole("admin");
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            OurUsers ourUsersResult = usersRepo.save(ourUser);
            if (ourUsersResult.getId()>0) {
                resp.setOurUsers((ourUsersResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes updateName(Integer userId, String name) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                OurUsers savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }
}
