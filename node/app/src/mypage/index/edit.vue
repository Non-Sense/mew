<template>
  <div class="hashimoto-main">     
    <form @submit="updateWord" action="#">
      <div class="hashimoto-button">
        <input class="hashimoto-delete" type="submit" value="Delete" v-on:click="deleteWord">
        <input class="hashimoto-save" type="submit" value="Save" v-on:click="updateWord">
      </div>
      <div class="hashimoto-page-name">Edit</div>
      
      <div class="hashimoto-edit-word">
        <p class="hashimoto-p">用語 <input v-model="word" class="hashimoto-word" type="text" name="word" id="word"></p>
        <p class="hashimoto-p">意味 <input v-model="meaning" class="hashimoto-meaning" type="text" name="meaning" id="meanning" ></p>
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
      word: "",
      meaning: ""
    }
  },

  // 読み込み時にinputに値が反映されてるほうが良さそう
  created: function (){
    this.getWord();
  },
  methods: {
    // 単語IDで単語取得
    getWord(){
      let wordId = getParam();
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

        console.log(res.data);  // <- TODO
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
      let wordId = getParam();
      if(wordId == null) {
        console.error("getParam: id == null");
        return;
      }
      axios.put(config.baseUrl+"/api/word/"+wordId, {
            word: this.word,   // <- TODO
            mean: this.meaning    // <- TODO
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
            this.$router.push("/login");
            break;
          case 404:
            // 自分の単語ではない・そもそも無い
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
      let wordId = getParam();
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
          console.log("OK")   // <- TODO
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