package com.dzz.stock.mapper;

import com.dzz.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
* @author dzz
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-09-16 19:13:55
* @Entity com.dzz.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findUserInfoByUserName(@Param("username") String username);
}
