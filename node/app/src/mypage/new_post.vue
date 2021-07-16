<template>
  <div class="hashimoto-main">     
    <form action="送信先のURL" method="post">
      <div class="hashimoto-button">
        <input class="hashimoto-save" type="submit" value="Save">
      </div>
      <div class="hashimoto-page-name">New Post</div>
      
      <div class="hashimoto-post-form">
        <div class="hashimoto-box">単語帳名 <input class="hashimoto-word-book" type="text" name="word-book"></div>
        <p>share<input class="hashimoto-share" type="checkbox" name="share" ></p>
      </div>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import config from "@/const.js"

export default {
  name: "new_post",
  data() {
    return {
    }
  },

  methods: {
    // 単語帳の作成
    addBook(){
      axios.post(config.baseUrl+"/api/book", {
        title: "",      // <- TODO
        public: false   // <- TODO
      },
      {headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName) }
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        if(res.status===200) {
          console.log("OK") // <- TODO: 成功したのでリダイレクトするなりなんなり
        }
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            // アクセス拒否: ログインし直してもらう
            this.$router.push("/test/login"); // <- TODO
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