package jp.ac.gunmau.andolab.mew.service

import jp.ac.gunmau.andolab.mew.mapper.CommentMapper
import jp.ac.gunmau.andolab.mew.model.Comment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class CommentService @Autowired constructor(private val dao: CommentMapper){

    fun insert(comment: Comment): Boolean{
        return dao.insert(comment)>0
    }

    fun selectById(id: Int): Comment?{
        return dao.selectWithId(id)
    }

    fun selectByBookId(bookId: Int): Comment?{
        return dao.selectWithBookId(bookId)
    }

    fun selectByUserId(userId: Int): Comment?{
        return dao.selectWithUserId(userId)
    }

    fun selectByRate(rate: Int): Comment?{
        return dao.selectWithRate(rate)
    }

    fun selectAll(): List<Comment>?{
        return dao.selectAll()
    }
}