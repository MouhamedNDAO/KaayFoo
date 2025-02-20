package com.inn.cafe.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.Authentication ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.inn.cafe.JWT.CustomerUserDetailService;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.JWT.JwtUtil;
import com.inn.cafe.POJO.User;
import com.inn.cafe.constants.CafeConstant;
import com.inn.cafe.dao.UserDao;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.CafeUtil;
import com.inn.cafe.utils.EmailUtil;
import com.inn.cafe.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailService customerUserDetailService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtil emailUtil;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{
        if (validateSignUpMap(requestMap)) {
            log.info("Inside signUp",requestMap);
            User user = userDao.findByEmailId(requestMap.get("email"));
            if (Objects.isNull(user)) {
                userDao.save(getUserFromMap(requestMap));
                return CafeUtil.getResponseEntity("Registered successfully", HttpStatus.OK);
            }
            else{
                return CafeUtil.getResponseEntity("Email already exist", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return CafeUtil.getResponseEntity(CafeConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
    } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR) ;
    }

    private Boolean validateSignUpMap(Map<String,String> requestMap){
       if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password")){
        return true ;
       }
       else{
        return false ;
       }
    }

    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();

        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setName(requestMap.get("name"));
        user.setPassword(requestMap.get("password"));
        user.setRole("user");
        user.setStatus("false");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");

        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (customerUserDetailService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    
                    return new ResponseEntity<String>("{\"Token\":\"\""+
                    jwtUtil.generateToken(customerUserDetailService.getUserDetail().getEmail(), customerUserDetailService.getUserDetail().getRole()) +"\"}",
                    HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<String>("{\"message \": \"" + "Wait for admin approval."+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
                return new ResponseEntity<String>("{\"message \": \"" + "Bad credentials."+"\"}",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
               Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));

               if (!optional.isEmpty()) {
                userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
                                return CafeUtil.getResponseEntity("User status updated successfully", HttpStatus.OK);
                               }else{
                                return CafeUtil.getResponseEntity("User id doesn't exist", HttpStatus.OK);
                               }
                
                            }else{
                                return CafeUtil.getResponseEntity(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                
    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
            allAdmin.remove(jwtFilter.getCurrentUser());
            if (status != null && status.equalsIgnoreCase("true")) {
                        emailUtil.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Approved","User:-"+user+"\n is approved by\nADMIN:-"+ jwtFilter.getCurrentUser(),allAdmin);
            }else{
                        emailUtil.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Disallowed","User:-"+user+"\n is disallowed by\nADMIN:-"+ jwtFilter.getCurrentUser(),allAdmin);
            }
       }
     @Override
     public ResponseEntity<String> checkToken() {
         return CafeUtil.getResponseEntity("true", HttpStatus.OK);      
      }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());

            if (userObj.equals(null)) {
                if (userObj.getPassword().equals(requestMap.get("oldPaswword"))) {
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);
                    return CafeUtil.getResponseEntity("Password changed secessfully", HttpStatus.OK);
                }
                return CafeUtil.getResponseEntity("incorrect old password", HttpStatus.BAD_REQUEST);
            }
            return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User userObj = userDao.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(userObj) && !Strings.isNullOrEmpty(userObj.getEmail())) 
                emailUtil.forgotMail(userObj.getEmail(),"Credential by Cafe Management",userObj.getPassword());
            return CafeUtil.getResponseEntity("Check your mail for credential", HttpStatus.OK);
            } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
                    
}
