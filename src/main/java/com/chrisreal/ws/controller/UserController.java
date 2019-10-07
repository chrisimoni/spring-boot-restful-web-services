package com.chrisreal.ws.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chrisreal.ws.exceptions.UserServiceException;
import com.chrisreal.ws.model.request.UserDetailsRequestModel;
import com.chrisreal.ws.model.response.AddressesRest;
import com.chrisreal.ws.model.response.ErrorMessages;
import com.chrisreal.ws.model.response.OperationStatusModel;
import com.chrisreal.ws.model.response.RequestOperationName;
import com.chrisreal.ws.model.response.RequestOperationStatus;
import com.chrisreal.ws.model.response.UserRest;
import com.chrisreal.ws.service.UserService;
import com.chrisreal.ws.shared.dto.AddressDto;
import com.chrisreal.ws.shared.dto.UserDto;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {

		//UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		
		ModelMapper modelMapper = new ModelMapper();
		
		UserRest returnValue = modelMapper.map(userDto, UserRest.class);

		//BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();

		// User data transfer object
		/*UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);*/
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		//BeanUtils.copyProperties(createdUser, returnValue);
		returnValue = modelMapper.map(createdUser, UserRest.class);

		return returnValue;
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();

		// if(userDetails.getFirstName().isEmpty()) throw new
		// UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		// User data transfer object
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{id}",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
				)
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		return returnValue;
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value="page", defaultValue="0") int page, 
			@RequestParam(value="limit", defaultValue="25") int limit) {
		
		List<UserRest> returnValue = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		
		for(UserDto userDto: users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			
			returnValue.add(userModel);
			
		}
		
		return returnValue;
	}
	
	//http://localhost:8080/mobile-app-ws/users/ghghdshd/addresses	
	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<AddressesRest> getUserAddresses(@PathVariable String id) {

		List<AddressesRest> returnValue = new ArrayList<>();
		
		List<AddressDto> addressesDto = addressService.getAddresses(id);
		
		if(addressesDto != null && !addressesDto.isEmpty()) {
			java.lang.reflect.Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			returnValue = new ModelMapper().map(addressesDto, listType);
		}

		//BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}
}
