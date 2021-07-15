package jp.ac.gunmau.andolab.mew.service

import jp.ac.gunmau.andolab.mew.mapper.WordMapper
import jp.ac.gunmau.andolab.mew.model.Word
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class WordService @Autowired constructor(private val dao: WordMapper){

    fun insert(word: Word): Boolean{
        return dao.insert(word)>0
    }

    fun selectById(id: Int): Word?{
        return dao.selectWithId(id)
    }

    fun selectByBookId(bookId: Int): List<Word>{
        return dao.selectWithBookId(bookId)
    }

    fun findByWord(pattern: String): List<Word>{
        return dao.findByWord(pattern)
    }

    fun findByMean(pattern: String): List<Word>{
        return dao.findByMean(pattern)
    }

    fun updateWord(wordId: Int, word:String): Boolean{
        return dao.updateWord(wordId, word)
    }

    fun updateMean(wordId: Int, mean: String): Boolean{
        return dao.updateMean(wordId, mean)
    }

    fun update(wordId: Int, word: String, mean: String): Boolean{
        return dao.update(wordId, word, mean)
    }

    fun selectAll(): List<Word>{
        return dao.selectAll()
    }
}