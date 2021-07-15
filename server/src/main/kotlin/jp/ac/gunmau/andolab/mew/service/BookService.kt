package jp.ac.gunmau.andolab.mew.service

import jp.ac.gunmau.andolab.mew.mapper.BookMapper
import jp.ac.gunmau.andolab.mew.model.Book
import jp.ac.gunmau.andolab.mew.model.BookWithRate
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

    fun selectByUserId(userId: Int):List<Book>{
        return dao.selectWithUserId(userId)
    }

    fun selectPublicByUserId(userId: Int):List<Book>{
        return dao.selectPublicWithUserId(userId)
    }

    fun selectBooksWithRate():List<BookWithRate>{
        return dao.selectBooksWithRate()
    }

    fun findByTitle(pattern: String, userId: Int):List<Book>{
        return dao.findByTitle(pattern, userId)
    }

    fun findPublicByTitle(pattern: String):List<Book>{
        return dao.findPublicByTitle(pattern)
    }

    fun updateTitle(bookId: Int, title: String): Boolean{
        return dao.updateTitle(bookId, title)
    }

    fun updatePublic(bookId: Int, public: Boolean): Boolean{
        return dao.updatePublic(bookId,public)
    }

    fun update(bookId: Int, title: String, public: Boolean): Boolean{
        return dao.update(bookId, title, public)
    }

    fun delete(bookId: Int): Boolean{
        return dao.delete(bookId)
    }

    fun selectAll():List<Book>{
        return dao.selectAll()
    }

}