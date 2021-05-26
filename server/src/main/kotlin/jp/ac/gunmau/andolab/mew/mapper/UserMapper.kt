package jp.ac.gunmau.andolab.mew.mapper

import jp.ac.gunmau.andolab.mew.model.User
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Mapper
interface UserMapper {
    @Insert("INSERT INTO user(name_id, name, password) VALUES(#{nameId}, #{name}, #{password})")
    fun insert(model: User): Int

    @Select("SELECT * FROM user WHERE user_id = #{id}")
    fun selectWithId(id: Int): User

    @Select("SELECT * FROM user WHERE name_id = #{nameId}")
    fun selectWithNameId(nameId: String): User

    @Select("SELECT * FROM user")
    fun selectAll(): List<User>
}