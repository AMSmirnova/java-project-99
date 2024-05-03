package hexlet.code.mapper;

import hexlet.code.dto.user.UserCreateDto;
import hexlet.code.dto.user.UserDto;
import hexlet.code.dto.user.UserUpdateDto;
import hexlet.code.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public abstract User map(UserCreateDto dto);
    public abstract UserDto map(User model);
    public abstract void update(UserUpdateDto dto, @MappingTarget User model);

    @BeforeMapping
    public void encryptPassword(UserCreateDto data) {
        var password = data.getPasswordDigest();
        data.setPasswordDigest(passwordEncoder.encode(password));
    }
}
