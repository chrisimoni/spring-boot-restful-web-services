package com.chrisreal.ws.service;

import java.util.List;

import com.chrisreal.ws.shared.dto.AddressDto;

public interface AddressService {
	List<AddressDto> getAddresses(String userId);

	AddressDto getAddress(String addressId);
}
