<template>
  <div class="hashimoto-main">     
      <form action="送信先のURL" method="post">
        <div class="hashimoto-button">
          <input class="hashimoto-save" type="submit" value="Save">
        </div>
        <div class="hashimoto-page-name">New Post</div>
        
        <div class="hashimoto-post-word">
          <p class="hashimoto-p">用語 <input class="hashimoto-word" type="text" name="word"></p>
          <p class="hashimoto-p">意味 <input class="hashimoto-meaning" type="text" name="meaning" ></p>
        </div>
      </form>

      
    </div>
</template>

<script>
import axios from "axios";
import config from "@/const.js"

function getParam(){
  let params = new URLSearchParams(document.location.search.substring(1));
  return params.get("id");
}

export default {
  name: "edit_word",
  data() {
    return {
    }
  },

  methods: {
    // 単語の内容更新
    addWord(){
      let bookId = getParam();
      if(bookId == null) {
        console.error("getParam: id == null");
        return;
      }
      axios.post(config.baseUrl+"/api/book/"+bookId+"/word", {
            word: "",   // <- TODO
            mean: ""    // <- TODO
          },
          {
            headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
          }
      ).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        if(res.status===200) {
          console.log("OK") // <- TODO
        }
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/test/login"); // <- TODO
            break;
          case 404:
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