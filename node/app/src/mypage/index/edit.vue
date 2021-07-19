<template>
  <div class="hashimoto-main">     
    <form @submit.prevent="updateWord" action="#!">
      <div class="hashimoto-button">
        <input class="hashimoto-delete" type="button" value="Delete" v-on:click="deleteWord">
        <input class="hashimoto-save" type="submit" value="Save">
      </div>
      <div class="hashimoto-page-name">Edit</div>
      
      <div class="hashimoto-edit-word">
        <p class="hashimoto-p">用語 <input v-model="word" class="hashimoto-word" type="text" name="word" id="word" required></p>
        <p class="hashimoto-p">意味 <input v-model="meaning" class="hashimoto-meaning" type="text" name="meaning" id="meanning" required></p>
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

  // 読み込み時にinputに値が反映されてるほうが良さそう
  created: function (){
    this.id = this.$route.params.id;
    this.getWord();
  },
  methods: {
    // 単語IDで単語取得
    getWord(){
      let wordId = this.id;
      if(wordId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/word/"+wordId, {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)
        }
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.word = res.data.word;
        this.meaning = res.data.mean;
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/login");
            break;
          case 404:
            // 閲覧できない単語・そもそも無い
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
    // 単語の内容更新
    updateWord(){
      let wordId = this.id;
      if(wordId == null) {
        console.error("getParam: id == null");
        return;
      }
      axios.put(config.baseUrl+"/api/word/"+wordId, {
            word: this.word,
            mean: this.meaning
          },
          {
            headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
          }
      ).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        if(res.status===200) {
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
            // 自分の単語ではない・そもそも無い
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
    deleteWord(){
      let wordId = this.id;
      if(wordId == null) {
        console.error("getParam: id == null");
        return;
      }
      axios.delete(config.baseUrl+"/api/word/"+wordId, {
            headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
          }
      ).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        if(res.status===200) {
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
            // 自分の単語ではない・そもそも無い
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