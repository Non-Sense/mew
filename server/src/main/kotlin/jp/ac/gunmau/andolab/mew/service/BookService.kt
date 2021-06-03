package jp.ac.gunmau.andolab.mew.service

import jp.ac.gunmau.andolab.mew.mapper.BookMapper
import jp.ac.gunmau.andolab.mew.model.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class BookService @Autowired constructor(private val dao: BookMapper) {

    fun insert(book: Book):Boolean{
        return dao.insert(book)>0
    }

    fun selectById(id: Int):Book?{
        return dao.selectWithId(id)
    }

    fun selectByUserId(userId: Int):Book?{
        return dao.selectWithUserId(userId)
    }

    fun findByTitle(pattern: String):List<Book>?{
        return dao.findByTitle(pattern)
    }

    fun selectAll():List<Book>?{
        return dao.selectAll()
    }

}