package jp.ac.gunmau.andolab.mew

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print


@AutoConfigureMockMvc
@SpringBootTest
class MewApplicationTests {

	@Autowired
	private lateinit var mock: MockMvc

	@Test
	fun contextLoads() {
	}

	@Test
	fun testIndex(){
		println("test start")
		mock.perform(get("/api")).andDo(print()).andExpect(status().isOk)
	}

}
