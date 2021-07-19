package jp.ac.gunmau.andolab.mew.controller

import jp.ac.gunmau.andolab.mew.model.*
import jp.ac.gunmau.andolab.mew.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger

@RestController
@RequestMapping("/api")
class ApiController @Autowired constructor(
    private val userService: UserService,
    private val bookService: BookService,
    private val wordService: WordService,
    private val commentService: CommentService,
    private val rateService: RateService){

    companion object{
        private val bcryptRegex = Regex("""^\$2[ayb]\$.{56}$""")
    }

    private fun getUserId(auth: Authentication): Int{
        return (auth.principal as User).userId!!
    }

    @RequestMapping
    fun index():String{
        return "Hello"
    }

    @RequestMapping("/test")
    fun test():String{
        return "valid"
    }

    /**
     * ユーザ追加 (POST: /signup)
     *
     * e.g. {"nameId":"testuser","name":"The User","password":"testpassword"}
     * パスワードハッシュはbcryptを使うけどハッシュ化するのはサーバ側にします(ログインするときに平文でパスワード送るので)
     * すでにBCryptだったらそのまま採用します
     */
    @PostMapping("/signup")
    @ResponseBody
    fun addUser(@RequestBody user: User): ResponseEntity<String>{
        if(!user.validateOnSignup())
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        if(!user.password.matches(bcryptRegex)) {
            val encoder = BCryptPasswordEncoder()
            user.password = encoder.encode(user.password)
        }
        try {
            userService.insert(user)
        } catch (e: DuplicateKeyException){
            return ResponseEntity(null, HttpStatus.CONFLICT)
        }
        return ResponseEntity(null, HttpStatus.OK)
    }


    private fun <T> responseEntityUtil(obj: T?):ResponseEntity<T>{
        return if(obj == null)
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        else
            ResponseEntity(obj, HttpStatus.OK)
    }
    private fun <T> responseEntityUtil(obj: List<T>):ResponseEntity<List<T>>{
        if(obj.isEmpty())
            return ResponseEntity(listOf(),HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(obj)
    }
    private fun patternUtil(pattern: String):String{
        if(!pattern.startsWith('%') && !pattern.endsWith('%'))
            return "%$pattern%"
        return pattern
    }

    /**
     * ユーザ取得 (GET: /user?[nemeid|id]=)
     *
     * nameidは一意な名前, idは内部ID
     * 完全一致したユーザを返す
     */
    @GetMapping("/user")
    fun findUserByNameId(@RequestParam(name="nameid",required = false) nameId:String?,
                         @RequestParam(name="id",required = false) id:Int?) :ResponseEntity<UserView>{

        nameId?:id?:return ResponseEntity(null,HttpStatus.BAD_REQUEST)

        if(nameId!=null){
            userService.select(nameId).let {
                return responseEntityUtil(if(it!=null)UserView(it) else null)
            }
        }
        userService.select(id!!).let {
            return responseEntityUtil(if(it!=null)UserView(it) else null)
        }
    }

    /**
     * ユーザ取得 (GET: /user/{nameid})
     *
     * ログイン用IDでユーザを取得する
     */
    @GetMapping("/user/{nameid}")
    fun getUser(@PathVariable("nameid") nameId: String): ResponseEntity<UserView>{
        userService.select(nameId).let {
            return responseEntityUtil(if(it!=null)UserView(it) else null)
        }
    }


    /**
     * ユーザ検索 (GET: /finduser?name=)
     *
     * 表示名で検索して返す
     * あいまい検索になっているはず
     */
    @GetMapping("/user/find")
    fun findUserById(@RequestParam(name="name",required = true) name:String):ResponseEntity<List<UserView>>{
        userService.findByName(patternUtil(name)).let {
            if(it.isEmpty())
                return ResponseEntity(listOf(),HttpStatus.NOT_FOUND)
            return ResponseEntity(it.map{ elm->UserView(elm)},HttpStatus.OK)
        }
    }

    /**
     * ユーザ全取得(500件まで)
     *
     * テスト用:そのうち消す
     */
    @GetMapping("/users")
    fun getAllUser(): ResponseEntity<List<UserView>>{
        return ResponseEntity(userService.selectAll().map{ UserView(it) },HttpStatus.OK)
    }

    @PostMapping("/book")
    fun postBook(@RequestBody book:Book): ResponseEntity<String>{
        book.userId = getUserId(SecurityContextHolder.getContext().authentication)
        if(book.public==null)
            book.public = false
        book.title?:return ResponseEntity(HttpStatus.BAD_REQUEST)
        try {
            bookService.insert(book)
        } catch (e: DuplicateKeyException){
            return ResponseEntity(null, HttpStatus.CONFLICT)
        } catch (e: DataIntegrityViolationException){
            return ResponseEntity(null,HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(null)
    }

    @GetMapping("/book")
    fun getBook(@RequestParam(name="userid",required = false) userId:Int?): ResponseEntity<List<Book>>{
        val currentUserId = getUserId(SecurityContextHolder.getContext().authentication)
        if(userId==null || currentUserId == userId){
            return responseEntityUtil(bookService.selectByUserId(currentUserId))
        }
        return responseEntityUtil(bookService.selectPublicByUserId(userId))
    }

    @GetMapping("/book/{id}")
    fun getBookById(@PathVariable("id") id:Int): ResponseEntity<Book>{
        val currentUserId = getUserId(SecurityContextHolder.getContext().authentication)
        return responseEntityUtil(bookService.selectById(id)?.let {
            when{
                // 自分の単語帳か公開状態の単語帳を取得できる
                (it.public==true) -> it
                (it.userId==currentUserId) -> it
                else -> null
            }
        })
    }

    @GetMapping("/book/find")
    fun findBook(@RequestParam(name="title",required = true) title:String,
                 @RequestParam(name="own", required = false) own: Int?): ResponseEntity<List<Book>>{
        own?:return responseEntityUtil(bookService.findPublicByTitle(patternUtil(title)))
        val userId = getUserId(SecurityContextHolder.getContext().authentication)
        return responseEntityUtil(bookService.findByTitle(patternUtil(title),userId))
    }

    @GetMapping("/books")
    fun getAllBook(): ResponseEntity<List<Book>>{
        return ResponseEntity.ok(bookService.selectAll())
    }

    @PutMapping("/book/{id}")
    fun updateBook(@PathVariable("id") id: Int,
                   @RequestBody book: Book): ResponseEntity<String>{
        val b = bookService.selectById(id)?:return ResponseEntity(HttpStatus.NOT_FOUND)
        // 作成者以外編集できないことにする
        if(b.userId != getUserId(SecurityContextHolder.getContext().authentication))
            return ResponseEntity(HttpStatus.NOT_FOUND)
        if(book.public!=null && book.title!=null){
            if(bookService.update(id, book.title!!, book.public!!))
                return ResponseEntity(HttpStatus.OK)
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        if(book.public!=null){
            if(bookService.updatePublic(id, book.public!!))
                return ResponseEntity(HttpStatus.OK)
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        if(book.title!=null){
            if(bookService.updateTitle(id, book.title!!))
                return ResponseEntity(HttpStatus.OK)
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @DeleteMapping("/book/{id}")
    fun deleteBook(@PathVariable("id") id:Int):ResponseEntity<String>{
        val b = bookService.selectById(id)?:return ResponseEntity(HttpStatus.NOT_FOUND)
        if(b.userId != getUserId(SecurityContextHolder.getContext().authentication))
            return ResponseEntity(HttpStatus.NOT_FOUND)
        if(bookService.delete(id))
            return ResponseEntity(HttpStatus.OK)
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @GetMapping("/book/public")
    fun getPublicBooks(@RequestParam(name="title",required = false)title:String?):ResponseEntity<List<BookWithRate>>{
        if(title==null || title=="")
            return responseEntityUtil(bookService.selectBooksWithRate())
        return responseEntityUtil(bookService.findBooksWithRate(patternUtil(title)))
    }


    @PostMapping("/word")
    fun postWord(@RequestBody word:Word):ResponseEntity<String>{
        if(word.word == null || word.mean == null || word.bookId == null)
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        val userId = getUserId(SecurityContextHolder.getContext().authentication)
        val book = bookService.selectById(word.bookId!!)?: return ResponseEntity(HttpStatus.NOT_FOUND)
        // 自分が作成した単語帳じゃないと追加できない
        if(userId!=book.userId)
            return ResponseEntity(HttpStatus.NOT_FOUND)
        word.userId = userId
        try {
            wordService.insert(word)
        } catch (e: DuplicateKeyException){
            return ResponseEntity(null, HttpStatus.CONFLICT)
        } catch (e: DataIntegrityViolationException){
            return ResponseEntity(null,HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(null)
    }

    @PostMapping("/book/{id}/word")
    fun postWordOnBook(@PathVariable(name = "id")bookId: Int,
                       @RequestBody word: Word): ResponseEntity<String>{
        word.mean?:word.word?:return ResponseEntity(HttpStatus.BAD_REQUEST)
        val userId = getUserId(SecurityContextHolder.getContext().authentication)
        val book = bookService.selectById(bookId)?: return ResponseEntity(HttpStatus.NOT_FOUND)
        if(userId!=book.userId)
            return ResponseEntity(HttpStatus.NOT_FOUND)
        word.bookId = bookId
        word.userId = userId
        try {
            wordService.insert(word)
        } catch (e: DuplicateKeyException){
            return ResponseEntity(null, HttpStatus.CONFLICT)
        } catch (e: DataIntegrityViolationException){
            return ResponseEntity(null,HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(null)
    }

    @GetMapping("/word/{id}")
    fun getWordById(@PathVariable(name="id") id:Int): ResponseEntity<Word>{
        return responseEntityUtil(wordService.selectById(id))
    }

    @GetMapping("/word")
    fun getWord(@RequestParam(name="bookid", required = true) bookId:Int): ResponseEntity<List<Word>>{
        return responseEntityUtil(wordService.selectByBookId(bookId))
    }
    @GetMapping("/book/{id}/word")
    fun getBookWord(@PathVariable(name="id") bookId: Int): ResponseEntity<List<Word>>{
        val userId = getUserId(SecurityContextHolder.getContext().authentication)
        val b = bookService.selectById(bookId)?:return ResponseEntity(HttpStatus.NOT_FOUND)
        if(b.userId==userId || b.public==true)
            return responseEntityUtil(wordService.selectByBookId(bookId))
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }


    @GetMapping("/word/find")
    fun findWord(@RequestParam(name="word",required = false) word: String?,
                 @RequestParam(name="mean",required = false) mean: String?): ResponseEntity<List<Word>>{
        word?:mean?:return ResponseEntity(listOf(),HttpStatus.BAD_REQUEST)
        if(word!=null){
            return responseEntityUtil(wordService.findByWord(patternUtil(word)))
        }
        return responseEntityUtil(wordService.findByMean(patternUtil(mean!!)))
    }

    @GetMapping("/book/{id}/word/find")
    fun findWordOnBook(@PathVariable(name="id")bookId: Int,
                       @RequestParam(name="word",required = false) word: String?,
                       @RequestParam(name="mean",required = false) mean: String?): ResponseEntity<List<Word>>{
        val userId = getUserId(SecurityContextHolder.getContext().authentication)
        val b = bookService.selectById(bookId)?:return ResponseEntity(HttpStatus.NOT_FOUND)
        if(b.public!=true || b.userId != userId)
            return ResponseEntity(HttpStatus.NOT_FOUND)
        word?:mean?:return responseEntityUtil(wordService.selectByBookId(bookId))
        if(word!=null&&mean!=null){
            return responseEntityUtil(wordService.find(bookId,patternUtil(word),patternUtil(mean)))
        }
        if(word!=null){
            return responseEntityUtil(wordService.findByWord(patternUtil(word)))
        }
        return responseEntityUtil(wordService.findByMean(patternUtil(mean!!)))
    }

    @GetMapping("/words")
    fun getAllWord(): ResponseEntity<List<Word>>{
        return ResponseEntity.ok(wordService.selectAll())
    }

    @PutMapping("/word/{id}")
    fun updateWord(@PathVariable(name="id") id:Int,
                    @RequestBody word: Word): ResponseEntity<String>{
        val w = wordService.selectById(id)?:return ResponseEntity(HttpStatus.NOT_FOUND)
        // 自分が追加した単語じゃないと編集できない
        if(w.userId != getUserId(SecurityContextHolder.getContext().authentication))
            return ResponseEntity(HttpStatus.NOT_FOUND)
        if(word.word!=null&&word.mean!=null){
            if(wordService.update(id, word.word!!, word.mean!!))
                return ResponseEntity(HttpStatus.OK)
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        if(word.word!=null){
            if(wordService.updateWord(id, word.word!!))
                return ResponseEntity(HttpStatus.OK)
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        if(word.mean!=null){
            if(wordService.updateMean(id, word.mean!!))
                return ResponseEntity(HttpStatus.OK)
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @DeleteMapping("/word/{id}")
    fun deleteWord(@PathVariable(name="id") id:Int): ResponseEntity<String>{
        val w = wordService.selectById(id)?:return ResponseEntity(HttpStatus.NOT_FOUND)
        if(w.userId != getUserId(SecurityContextHolder.getContext().authentication))
            return ResponseEntity(HttpStatus.NOT_FOUND)
        if(wordService.delete(id))
            return ResponseEntity(HttpStatus.OK)
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }


    @PostMapping("/comment")
    fun postComment(@RequestBody comment: Comment): ResponseEntity<String>{
        comment.userId = getUserId(SecurityContextHolder.getContext().authentication)
        try {
            commentService.insert(comment)
        } catch (e: DataIntegrityViolationException){
            return ResponseEntity(null,HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(null)
    }

    @GetMapping("/comment/{id}")
    fun getCommentById(@PathVariable(name="id") id:Int): ResponseEntity<Comment>{
        return responseEntityUtil(commentService.selectById(id))
    }

    @GetMapping("/comment")
    fun getComment(@RequestParam(name="bookid", required = false) bookId:Int?,
                   @RequestParam(name="userId", required = false) userId:Int?):ResponseEntity<List<Comment>>{
        bookId?:userId?:return ResponseEntity(listOf(),HttpStatus.BAD_REQUEST)
        if(bookId!=null)
            return responseEntityUtil(commentService.selectByBookId(bookId))
        return responseEntityUtil(commentService.selectByUserId(userId!!))
    }
    @GetMapping("/book/{id}/comment")
    fun getBookComment(@PathVariable(name="id") bookId: Int): ResponseEntity<List<Comment>>{
        val userId = getUserId(SecurityContextHolder.getContext().authentication)
        val b = bookService.selectById(bookId)?:return ResponseEntity(HttpStatus.NOT_FOUND)
        if(b.public!=true || b.userId!=userId)
            return ResponseEntity(HttpStatus.NOT_FOUND)
        return responseEntityUtil(commentService.selectByBookId(bookId))
    }

    @PostMapping("/book/{id}/comment")
    fun postComment(@PathVariable(name="id") bookId: Int,
                    @RequestBody comment: Comment):ResponseEntity<String>{
        val userId = getUserId(SecurityContextHolder.getContext().authentication)
        val b = bookService.selectById(bookId)?:return ResponseEntity(HttpStatus.NOT_FOUND)
        if(b.public!=true || b.userId!=userId)
            return ResponseEntity(HttpStatus.NOT_FOUND)
        comment.bookId = bookId
        comment.userId = userId
        if(commentService.insert(comment))
            return ResponseEntity(HttpStatus.OK)
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @GetMapping("/comments")
    fun getAllComment(): ResponseEntity<List<Comment>>{
        return ResponseEntity.ok(commentService.selectAll())
    }


    @PostMapping("/rate")
    fun postRate(@RequestBody rate: Rate):ResponseEntity<String>{
        rate.bookId?:return ResponseEntity(HttpStatus.BAD_REQUEST)
        val userId = getUserId(SecurityContextHolder.getContext().authentication)
        val b = bookService.selectById(rate.bookId!!)?:return ResponseEntity(HttpStatus.NOT_FOUND)
        if(b.public!=true||b.userId!=userId)
            return ResponseEntity(HttpStatus.NOT_FOUND)
        rate.userId = userId
        try{
            rateService.insert(rate)
        } catch (e: DataIntegrityViolationException){
            return ResponseEntity(null,HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(null)
    }

    @GetMapping("/rate/{id}")
    fun getRateById(@PathVariable(name = "id")id: Int): ResponseEntity<Rate>{
        return responseEntityUtil(rateService.selectByRateId(id))
    }

    @GetMapping("/rate")
    fun getRate(@RequestParam(name="bookid", required = false) bookId:Int?,
                @RequestParam(name="userId", required = false) userId:Int?): ResponseEntity<List<Rate>>{
        bookId?:userId?:return ResponseEntity(listOf(), HttpStatus.BAD_REQUEST)
        if(bookId!=null)
            return responseEntityUtil(rateService.selectByBookId(bookId))
        return responseEntityUtil(rateService.selectByUserId(userId!!))
    }

    @GetMapping("/book/{id}/rate")
    fun getBookRate(@PathVariable(name = "id")bookId: Int): ResponseEntity<HashMap<String,Double?>>{
        val m = HashMap<String,Double?>()
        rateService.getAverage(bookId).let {
            it?:return ResponseEntity(null,HttpStatus.NOT_FOUND)
            m["avg"] = it
        }
        return ResponseEntity.ok(m)
    }

    @GetMapping("/rates")
    fun getAllRate(): ResponseEntity<List<Rate>>{
        return ResponseEntity.ok(rateService.selectAll())
    }

    @PostMapping("/book/{id}/rate")
    fun postRate(@PathVariable(name = "id") bookId: Int,
                   @RequestBody rate: Rate): ResponseEntity<String>{
        val userId = getUserId(SecurityContextHolder.getContext().authentication)
        rate.bookId = bookId
        rate.userId = userId
        try{
            rateService.insert(rate)
        } catch (e: DuplicateKeyException){
            return ResponseEntity(null, HttpStatus.CONFLICT)
        } catch (e: DataIntegrityViolationException){
            return ResponseEntity(null,HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(null)
    }

    @PutMapping("/book/{id}/rate")
    fun updateRate(@PathVariable(name = "id") bookId: Int,
                   @RequestBody rate: Rate): ResponseEntity<String>{
        val userId = getUserId(SecurityContextHolder.getContext().authentication)
        val r = rateService.selectWithBookIdAndUserId(bookId, userId)?: return ResponseEntity(HttpStatus.NOT_FOUND)
        if(rateService.updateRate(r.rateId!!, rate.rate))
            return ResponseEntity(HttpStatus.OK)
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }


    /**
     * passwordは平文で送ってもらう
     */
    @PutMapping("/user/{nameId}")
    fun updateUser(@RequestBody user:User):ResponseEntity<String>{
        val nameId = SecurityContextHolder.getContext().authentication.name

        if(user.name!=null){
            userService.updateDisplayName(nameId, user.name!!)
        }
        try {
            if (!user.password.matches(bcryptRegex)) {
                val encoder = BCryptPasswordEncoder()
                user.password = encoder.encode(user.password)
            }
            userService.updatePassword(nameId, user.password)
        }catch (e:NullPointerException){
        }

        return ResponseEntity(HttpStatus.OK)
    }

}