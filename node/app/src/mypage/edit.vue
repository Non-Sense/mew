<template>
  <div class="hashimoto-main">     
    <form action="送信先のURL" method="post">
      <div class="hashimoto-button">
        <input class="hashimoto-delete" type="submit" value="Delete">
        <input class="hashimoto-save" type="submit" value="Save">
      </div>
      <div class="hashimoto-page-name">Edit</div>
      
      <div class="hashimoto-edit-form">
        <div class="hashimoto-box">単語帳名 <input v-model="word-book" class="hashimoto-word-book" type="text" name="word-book" id="word-book"></div>
        <p>share<input v-model="share" class="hashimoto-share" type="checkbox" name="share" id="share"></p>
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
  name: "edit_book",
  data() {
    return {
    }
  },

  // 読み込み時にinputに値が反映されてるほうが良さそう
  created: function (){
    this.getBook();
  },
  methods: {
    // 単語帳IDで単語帳取得
    getBook(){
      let bookId = getParam();
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/book/"+bookId, {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)
        }
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);

        console.log(res.data);  // <- TODO
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/test/login"); // <- TODO
            break;
          case 404:
            // 閲覧できない単語帳・そもそも無い
            break;
          case 500:
            // サーバ内部エラー
            break;
          default:
            // 多分サーバが死んでいる
        }
      })
    },
    // 単語帳の内容更新
    updateBook(){
      let bookId = getParam();
      if(bookId == null) {
        console.error("getParam: id == null");
        return;
      }
      axios.put(config.baseUrl+"/api/book/"+bookId, {
            title: "",      // <- TODO
            public: false   // <- TODO
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
    // 単語帳の削除
    deleteBook(){
      let bookId = getParam();
      if(bookId == null) {
        console.error("getParam: id == null");
        return;
      }
      axios.delete(config.baseUrl+"/api/book/"+bookId, {
            headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
          }
      ).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        if(res.status===200) {
          console.log("OK")   // <- TODO
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