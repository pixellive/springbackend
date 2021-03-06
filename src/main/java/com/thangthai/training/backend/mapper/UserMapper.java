package com.thangthai.training.backend.mapper;

import com.thangthai.training.backend.entity.User;
import com.thangthai.training.backend.model.MRegisterResponse;
import com.thangthai.training.backend.model.MUserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    MRegisterResponse toRegisterResponse(User user);

    MUserProfile toUserProfile(User user);

}
