package com.itdr.mapper;

import com.itdr.pojo.Users;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    Users selectByUsernameAndPassword(String username, String password);

    Users selectByUsername(String username);

    int selectByUsernameOrEmail(@Param("str") String str, @Param("type") String type);

    int selectByUsernameAndQuestionAndAnswer(@Param("username")String username,@Param("question") String question, @Param("answer")String answer);

    int updateByUsernameAndPasswordNew(@Param("username")String username,@Param("passwordNew") String passwordNew);

    int updateByUsernameAndPasswordOldAndPasswordNew(@Param("username")String username,@Param("passwordOld") String passwordOld,@Param("passwordNew") String passwordNew);
}