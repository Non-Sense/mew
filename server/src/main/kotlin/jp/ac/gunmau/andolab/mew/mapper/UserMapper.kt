package jp.ac.gunmau.andolab.mew.mapper

import jp.ac.gunmau.andolab.mew.model.User
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
    @Insert("INSERT INTO user(name_id, name, password) VALUES(#{nameId}, #{name}, #{password})")
    fun insert(model: User): Int

    @Select("SELECT * FROM user WHERE user_id = #{id}")
    fun selectWithId(id: Int): User?

    @Select("SELECT * FROM user WHERE name_id = #{nameId}")
    fun selectWithNameId(nameId: String): User?

    @Select("SELECT * FROM user WHERE name LIKE #{pattern} LIMIT 300")
    fun findByName(pattern: String): List<User>

    @Select("SELECT * FROM user LIMIT 300")
    fun selectAll(): List<User>
}