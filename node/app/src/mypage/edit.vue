<template>
  <div class="hashimoto-main">     
    <form @submit.prevent="updateBook" action="#!">
      <div class="hashimoto-button">
        <input class="hashimoto-delete" type="submit" value="Delete" v-on:click="deleteBook">
        <input class="hashimoto-save" type="submit" value="Save" v-on:click="updateBook">
      </div>
      <div class="hashimoto-page-name">Edit</div>
      
      <div class="hashimoto-edit-form">
        <div class="hashimoto-box">単語帳名 <input v-model="wordBook" class="hashimoto-word-book" type="text" name="word-book" id="word-book" required></div>
        <p>share<input v-model="share" class="hashimoto-share" type="checkbox" name="share" id="share"></p>
      </div>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import config from "@/const.js"

export default {
  name: "edit_book",
  data() {
    return {
      wordBook: "",
      share: false,
      id:0
    }
  },

  // 読み込み時にinputに値が反映されてるほうが良さそう
  created: function (){
    this.id = this.$route.params.id;
    this.getBook();
  },
  methods: {
    // 単語帳IDで単語帳取得
    getBook(){
      let bookId = this.id;
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
        this.wordBook = res.data.title;
        this.share = res.data.public;
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/login");
            break;
          case 404:
            // 閲覧できない単語帳・そもそも無い
            this.$router.push("/mypage");
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
      let bookId = this.id;
      if(bookId == null) {
        console.error("getParam: id == null");
        return;
      }
      axios.put(config.baseUrl+"/api/book/"+bookId, {
            title: this.wordBook,
            public: this.share
          },
          {
            headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
          }
      ).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        if(res.status===200) {
          this.$router.push("/mypage");
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
            // 自分の単語帳ではない・そもそも無い
            this.$router.push("/mypage");
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
      let bookId = this.id;
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
          this.$router.push("/mypage");
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
            // 自分の単語帳ではない・そもそも無い
            this.$router.push("/mypage");
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