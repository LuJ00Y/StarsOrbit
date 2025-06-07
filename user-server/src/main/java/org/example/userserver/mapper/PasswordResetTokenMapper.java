package org.example.userserver.mapper;

import org.apache.ibatis.annotations.*;
import org.example.userserver.entity.PasswordResetToken;
import org.example.userserver.entity.User;

@Mapper
public interface PasswordResetTokenMapper {
    @Insert("INSERT INTO password_reset_token (token, user_id, expiry_date, used) " +
            "VALUES (#{token}, #{user.id}, #{expiryDate}, #{used})")
    void saveToken(PasswordResetToken token);

    @Select("SELECT * FROM password_reset_token WHERE token = #{token}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "token", column = "token"),
            @Result(property = "expiryDate", column = "expiry_date"),
            @Result(property = "used", column = "used"),
            @Result(property = "user", column = "user_id",
                    javaType = User.class,
                    one = @One(select = "org.example.userserver.mapper.UserMapper.findById"))
    })
    PasswordResetToken findByToken(String token);

    @Update("UPDATE password_reset_token SET " +
            "token = #{token}, " +
            "expiry_date = #{expiryDate}, " +
            "used = #{used} " +
            "WHERE id = #{id}")
    void updateToken(PasswordResetToken token);

    PasswordResetToken findByUserId(Long id);
}