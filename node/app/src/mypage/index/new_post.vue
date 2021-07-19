<template>
  <div class="hashimoto-main">     
      <form @submit.prevent="addWord" action="#!">
        <div class="hashimoto-button">
          <input class="hashimoto-save" type="submit" value="Save">
        </div>
        <div class="hashimoto-page-name">New Post</div>
        
        <div class="hashimoto-post-word">
          <p class="hashimoto-p">用語 <input v-model="word" class="hashimoto-word" type="text" name="word" id="word" required></p>
          <p class="hashimoto-p">意味 <input v-model="meaning" class="hashimoto-meaning" type="text" name="meaning" id="meaning" required></p>
        </div>
      </form>

      
    </div>
</template>

<script>
import axios from "axios";
import config from "@/const.js"

export default {
  name: "edit_word",
  data() {
    return {
      id: 0,
      word: "",
      meaning: ""
    }
  },
  created() {
    this.id = this.$route.params.id;
  },
  methods: {
    // 単語の内容更新
    addWord(){
      // 空文字列を登録しない
      if(this.word==="" && this.meaning==="")
        return;
      let bookId = this.id;
      if(bookId == null) {
        console.error("getParam: id == null");
        return;
      }
      axios.post(config.baseUrl+"/api/book/"+bookId+"/word", {
            word: this.word,
            mean: this.meaning
          },
          {
            headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
          }
      ).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        if(res.status===200) {
          // 単語帳に戻る
          this.$router.back();
        }
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/login");
            break;
          case 404:
            this.$router.push("/mypage");
            // 自分の単語帳ではない・そもそも無い
            break;
          case 500:
            // サーバ内部エラー
            break;
          default:
            // 多分サーバが死んでいる
        }
      })
    },
  }
}
</script>