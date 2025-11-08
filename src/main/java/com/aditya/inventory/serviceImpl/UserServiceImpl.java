package com.aditya.inventory.serviceImpl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aditya.inventory.customException.*;
import com.aditya.inventory.dto.*;
import com.aditya.inventory.entity.*;
import com.aditya.inventory.jwt.JwtUtils;
import com.aditya.inventory.repository.*;
import com.aditya.inventory.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.aditya.inventory.mapper.UserMapper;
import com.aditya.inventory.service.UserService;

import io.jsonwebtoken.lang.Arrays;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AdminRepo adminRepo;

	@Autowired
	private DealerRepo dealerRepo;

	@Autowired
	private CustomerRepo customerRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpRepo otpRepo;

	@Value("${adminLoginKey}")
	String adminKey;

    @Autowired
    private AuthenticationManager authenticationManager;


    //-------------------Sign In Authentication-----------//
    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        if(!validateEmail(loginRequest.getEmail())){
            throw new InvalidEmail();
        }
        if(!validatePassword(loginRequest.getPassword())){
            throw new InvalidPassword();
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail().toLowerCase(),
                        loginRequest.getPassword()));  //check email and password

        SecurityContextHolder.getContext().setAuthentication(authentication);          //Store aunthenticated user in spring security context
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();          // Gives the user info
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);             // generate Jwt token for valided user
        LoginResponse response = new LoginResponse(userDetails.getUsername(), jwtToken);// response jwt token with email
        return  response;
    }

	// ---------------------Adding User-----------------//

    @Transactional
	public UserResponseDto addUser(UserRequestDto userRequestDto) {

        if(!validateMobileNumber(userRequestDto.getMobile())){
            throw new InvalidMobileNumber();
        }
        if(!validateEmail(userRequestDto.getEmail())){
            throw new InvalidEmail();
        }

        if(!validatePassword(userRequestDto.getPassword())){
            throw new InvalidPassword();
        }

        if(userRepo.existsByEmail(userRequestDto.getEmail())){
            throw new AlreadyExits("Email " +  userRequestDto.getEmail());
        }
        if(userRepo.existsByMobileNo(userRequestDto.getMobile())){
            throw new AlreadyExits("Mobile number " +  userRequestDto.getMobile());
        }

		
		
		String[] role = userRequestDto.getRole();
		
		if(role.length==0) {
			throw new InvalidRole();
		}
		for (int i = 0; i < role.length; i++) {
			role[i]= initCap(role[i]);
		}
		userRequestDto.setRole(role);

		for (int i = 0; i < userRequestDto.getRole().length; i++) {
			if(!((userRequestDto.getRole()[i]).equals("Admin")|| userRequestDto.getRole()[i].equals("Dealer") || userRequestDto.getRole()[i].equals("Customer"))) {
				throw new InvalidRole();
			}
				
		}
		
		//Checking Admin key for admin Login
		if (Arrays.asList(userRequestDto.getRole()).contains("Admin")) {
			if (!userRequestDto.getFor_admin_login_key_required().equals(adminKey)) {
				throw new InvalidAdminKey();
			}
		}
        //Checking company name and gst no
        if (Arrays.asList(userRequestDto.getRole()).contains("Dealer")) {
            if (userRequestDto.getCompanyName() == null || userRequestDto.getCompanyName().isEmpty() || userRequestDto.getGstNo() == null || userRequestDto.getGstNo().isEmpty()) {
                throw new InvalidInput("Enter a Valid Company Name or GST No");
            }
        }

		User user = userMapper.toUser(userRequestDto);
		user.setCreatedAt(new Date());
        user.setStatus(false);
		User savedUser = userRepo.save(user);

		String[] role2 = savedUser.getRole();

		for (int i = 0; i < role2.length; i++) {
			switch (role2[i]) {
			case "Admin": {
				Admin admin = userMapper.toAdmin(savedUser);
				adminRepo.save(admin);
				break;
			}
			case "Dealer": {
				Dealer dealer = userMapper.toDealer(savedUser);
                if(userRequestDto.getCompanyName()!=null && userRequestDto.getGstNo()!=null) {
                    if(!validateGSTNo(userRequestDto.getGstNo())) {
                            throw new  InvalidGSTNo();
                    }
                    dealer.setGSTNo(userRequestDto.getGstNo());
                    dealer.setCompanyName(userRequestDto.getCompanyName());

                }

				dealerRepo.save(dealer);
				break;
			}

			case "Customer": {
				Customer customer = userMapper.toCustomer(savedUser);
				customerRepo.save(customer);
				break;
			}

			}

		}
        int otp = createOpt();
        Otp otpUser = new Otp();
        otpUser.setOtp(otp);
        otpUser.setEmail(user.getEmail());
        otpRepo.save(otpUser);


        emailService.sendMail(user.getEmail(),"Opt for verifivation",otp+"  this is your opt for registration.");
		return userMapper.toDto(savedUser);
	}

    //Verify otp
    public void verifyOtp(int userOtp, String email){
        if(!validateEmail(email)){
            throw new InvalidEmail();
        }
        User user = userRepo.findByEmail(email);
        if(user==null){
            throw new ResourceNotFound("user not found");
        }

        if(user.isStatus()){
            throw new AlreadyExits("User already exits with this " +  user.getEmail());
        }

        Otp byEmail = otpRepo.findByEmail(email);
        System.out.println(byEmail.getOtp());

        if(userOtp== byEmail.getOtp()){

            user.setStatus(true);
            userRepo.save(user);
            otpRepo.delete(byEmail);
        }
        else{
            throw new InvalidInput("Enter valid OTP");
        }
    }
	// ---------------------------Get------------------------------//

	// all
	public List<UserResponseDto> getUsers() {
		List<User> all = userRepo.findAll();
		List<UserResponseDto> users = new ArrayList<>();
		for (User user : all) {
			users.add(userMapper.toDto(user));
		}
		return users;
	}

	// ByID
	@Override
	public UserResponseDto getUserById(String id) {
		User byId = userRepo.findById(id).get();
		return userMapper.toDto(byId);
	}

	// ByEmail
	@Override
	public UserResponseDto getUserByEmail(String userNameFromJwtToken) {
		User byEmail = userRepo.findByEmail(userNameFromJwtToken);
		return userMapper.toDto(byEmail);
	}

	// Delete User
	@Override
	public boolean deleteUser(String id, HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwt);
        UserResponseDto userByEmail = getUserByEmail(userNameFromJwtToken);

        User userById = userRepo.findById(id).get();
        if(userById==null){
            throw new ResourceNotFound("User Not found");
        }
        if (userByEmail.getMobile() == userById.getMobileNo() || Arrays.asList(userByEmail.getRole()).contains("Admin")) {
            String[] role = userById.getRole();
            for(String role2 : role ) {
                switch (role2){
                    case "Admin": {
                        Admin admin = adminRepo.findByUser_id(userById.getUser_id());
                        adminRepo.delete(admin);
                        break;
                    }case "Dealer": {
                        Dealer dealer = dealerRepo.findByUser_id(userById.getUser_id());
                        dealerRepo.delete(dealer);
                        break;
                    }case "Customer": {
                        Customer customer = customerRepo.findByUser_id(userById.getUser_id());
                        customerRepo.delete(customer);
                        break;
                    }
                }
            }
            userRepo.deleteById(id);
        }


		return true;
	}

	// Update User
	@Override
	public UserResponseDto updateUser(UserRequestDto userRequestDto, HttpServletRequest request) {


       if(userRequestDto.getMobile() != null){
           if(!validateMobileNumber(userRequestDto.getMobile())){
               throw new InvalidMobileNumber();
           }
       }
       if(userRequestDto.getEmail()!=null){
           if(!validateEmail(userRequestDto.getEmail())){
               throw new InvalidEmail();
           }
       }

       if(userRequestDto.getPassword()!=null){
           if(!validatePassword(userRequestDto.getPassword())){
               throw new InvalidPassword();
           }
       }

        if(userRepo.existsByEmail(userRequestDto.getEmail())){
            throw new AlreadyExits("Email " +  userRequestDto.getEmail());
        }
        if(userRepo.existsByMobileNo(userRequestDto.getMobile())){
            throw new AlreadyExits("Mobile number " +  userRequestDto.getMobile());
        }



		if (Arrays.asList(userRequestDto.getRole()).contains("Admin")) {
			if (!userRequestDto.getFor_admin_login_key_required().equals(adminKey)) {
				throw new InvalidAdminKey();
			}
		}

        //Checking company name and gst no
        if (Arrays.asList(userRequestDto.getRole()).contains("Dealer")) {
            if (userRequestDto.getCompanyName() == null || userRequestDto.getCompanyName().isEmpty() || userRequestDto.getGstNo() == null || userRequestDto.getGstNo().isEmpty()) {
                if(!validateGSTNo(userRequestDto.getGstNo())) {
                    throw new InvalidGSTNo();
                }
                throw new InvalidInput("Enter a Valid Company Name or GST No");
            }
        }

        String jwt = jwtUtils.getJwtFromHeader(request);
        String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwt);
		User byEmail = userRepo.findByEmail(userNameFromJwtToken);
		User user = userMapper.toUser(userRequestDto, byEmail);
		user.setUpdatedAt(new Date());
		User savedUser = userRepo.save(user);

		String[] role = savedUser.getRole();
		for (int i = 0; i < role.length; i++) {
			switch (role[i]) {

			case "Admin": {
				Admin byUser_id = adminRepo.findByUser_id(savedUser.getUser_id());
				Admin admin = userMapper.toAdmin(savedUser, byUser_id);
				adminRepo.save(admin);
				break;
			}
			case "Dealer": {
				Dealer byUser_id = dealerRepo.findByUser_id(savedUser.getUser_id());
				Dealer dealer = userMapper.toDealer(savedUser, byUser_id);
                if(userRequestDto.getCompanyName()!=null && userRequestDto.getGstNo()!=null) {
                    if(!validateGSTNo(userRequestDto.getGstNo())) {
                        throw new  InvalidGSTNo();
                    }
                    dealer.setGSTNo(userRequestDto.getGstNo());
                    dealer.setCompanyName(userRequestDto.getCompanyName());

                }

				dealerRepo.save(dealer);
				break;
			}

			case "Customer": {
				Customer byUser_id = customerRepo.findByUser_id(savedUser.getUser_id());
				Customer customer = userMapper.toCustomer(savedUser, byUser_id);
				customerRepo.save(customer);
				break;
			}

			}

		}

		return userMapper.toDto(savedUser);
	}
	
	
	//Initial capital method string
	public String initCap(String value) {
		value = value.substring(0,1).toUpperCase()+value.substring(1).toLowerCase();
		return value;
	}

	//All dealers
	@Override
	public List<UserResponseDto> getDealers() {
		List<User> dealers = userRepo.getDealers();
		List<UserResponseDto> users = new ArrayList<>();
		for (User user : dealers) {
			users.add(userMapper.toDto(user));
		}
		return users;
	}

	//All Admins
	@Override
	public List<UserResponseDto> getAdmins() {
		List<User> admins = userRepo.getAdmins();
		List<UserResponseDto> users = new ArrayList<>();
		for (User user : admins) {
			users.add(userMapper.toDto(user));
		}
		return users;
	}

	//All Customers
	@Override
	public List<UserResponseDto> getCustomers() {
		List<User> customers = userRepo.getCustomers();
		List<UserResponseDto> users = new ArrayList<>();
		for (User user : customers) {
			users.add(userMapper.toDto(user));
		}
		return users;
	}

	
	//All user sort by Roles
	@Override
	public HashMap<String,List<UserResponseDto>> getUsersSortByRoles() {
		HashMap<String,List<UserResponseDto> > users = new HashMap<>(); 
		List<UserResponseDto> admins = getAdmins();
		List<UserResponseDto> dealers = getDealers();
		List<UserResponseDto> customers = getCustomers();
		
		users.put("Admins", admins);
		users.put("Dealers", dealers);
		users.put("Customers", customers);
		
		return users;
	}

    //MobileNumber matching

    private boolean validateMobileNumber(Long mobileNumber) {
       String mobileNumberRegex = "^[6-9][0-9]{9}$";
       Pattern pattern = Pattern.compile(mobileNumberRegex);
        Matcher matcher = pattern.matcher(mobileNumber.toString());
        return matcher.matches();

    }

    //Email pattern
    private boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Password match
    private boolean validatePassword(String password) {
        String passwordRegex ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$" ;
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //Vaildate GSTNO
    private boolean validateGSTNo(String gstNo) {
        String gstNoRegex = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
        Pattern pattern = Pattern.compile(gstNoRegex);
        Matcher matcher = pattern.matcher(gstNo);
        return matcher.matches();
    }

    //Opt generate
    private int createOpt(){
        Random random = new Random();
        int opt = 100000 + random.nextInt(99999);
        return opt;
    }





}
