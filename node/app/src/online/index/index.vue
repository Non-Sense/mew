<template>
  <div class="tomita_oibody">
    <div class="tomita_review_window" v-if="modalFlag">
      <div style="display: inline-block;  _display: inline;" class="tomita_review_window_content">
        <span class="tomita_btn-close" v-on:click="modal"></span>
          <div class="tomita_Review_word">
            Review
          </div>
          <div class="tomita_evaluation">
            <input type="number" name="point" value="point" class="tomita_point" min="0" max="5">
              <div class="tomita_five_point">
                / 5.0点
              </div>
          <div>
          <input type="text" class="review_comment" placeholder="コメント">
          <input type="submit" name="submit" value="送信" class="tomita_submit review_submit">
        </div>
      </div>
    </div>
  </div>

  <form action="#">
    <div class="tomita_oitop">
      <p>数学単語帳
      <input type="search" name="search" v-model="search" v-on:change="findWord" placeholder="Search" class="tomita_oisearch">
      <input type="button" name="Review" value="Review" class="tomita_oiReview" v-on:click="modal">
      </p>

    </div>
  </form>

  <div v-for="item in items" v-bind:key="item.wordId">
    <div class="tomita_miword_list">
      <table>
        <tr>
          <td class="tomita_miword">{{item.word}}</td>
          <td class="tomita_mimeaning">{{item.mean}}</td>
        </tr>
      </table>
    </div>
  </div>
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
  name: "online_index",
  data() {
    return {
      modalFlag: false,
    }
  },
  created: function () {
    this.getRate();
    this.getWord();
    this.getComment();
  },
  methods: {
    // 単語帳IDで単語一覧取得
    getWord(){
      let bookId = getParam();
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/book/"+bookId+"/word", {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.items=res.data;
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
    // 単語帳IDで単語帳の評価値を取得
    getRate(){
      let bookId = getParam();
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/book/"+bookId+"/rate", {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.items=res.data;
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
    // 単語帳IDでコメント一覧取得
    getComment(){
      let bookId = getParam();
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/book/"+bookId+"/comment", {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.items=res.data;
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
    // 単語帳IDで評価値を送信
    postRate(){
      let bookId = getParam();
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.post(config.baseUrl+"/api/book/"+bookId+"/rate", {
        rate: 1 // <- TODO
      }, {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        if(res.status===200) {
          console.log("OK");  // <- TODO
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
            // 閲覧できない単語帳・そもそも無い
            break;
          case 409:
            // 重複している(評価済み)
            break;
          case 500:
            // サーバ内部エラー
            break;
          default:
            // 多分サーバが死んでいる
        }
      })
    },
    // 単語帳IDでコメントを送信
    postComment(){
      let bookId = getParam();
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.post(config.baseUrl+"/api/book/"+bookId+"/comment", {
        comment: "" // <- TODO
      }, {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        if(res.status===200) {
          console.log("OK");  // <- TODO
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
    // 単語帳IDを指定して単語を検索
    findWord(){
      let searchWord = this.search;  // <- TODO
      let searchMean = this.search;  // <- TODO
      let bookId = getParam();
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/book/"+bookId+"/word/find", {
        params:{
          mean: searchMean===""?null:searchMean,
          word: searchWord===""?null:searchWord
        },
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.items=res.data;
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
            // ヒット数が0・そもそも単語帳が無い・見れない単語帳
            break;
          case 500:
            // サーバ内部エラー
            break;
          default:
            // 多分サーバが死んでいる
        }
      })
    },

    modal(){
      console.log(this.modalFlag);
      this.modalFlag = !this.modalFlag;
    },
  }
}
</script>
<style scoped>
.tomita_oirbody{
    margin: 0px;
}

.tomita_oirheader p{
    font-family: Roboto;
    font-style: normal;
    margin: 0px;
    background-color:  #7B89C9;
    padding-bottom: 35px;
    padding-top: 35px;
    padding-left: 20px;
    font-size: 20px;
    color: white;
}

.tomita_oirtop{
    font-family: Roboto;
    font-style: normal;
    font-size: 24px;
    color: #666666;
    width : 1000px;
    margin-left: 230px;
    margin-right: 230px;
    margin-bottom: 112px;
    margin-top: 110px;
}



.tomita_oirsearch{
    font-family: Roboto;
    font-style: normal;
    background-color: #F6F7FB;
    border: none;
    width: 480px;
    height: 48px;
    margin-left: 109px;
    padding-right: 408px;
    padding-left: 16px;
    padding-top: 14px;
    padding-bottom: 13px;
    border-radius: 16px;
}

.tomita_oirReview{
    font-family: Roboto;
    font-style: normal;
    background-color: #7B89C9;
    color:white;
    border: none;
    font-size: 24px;
    width: 188px;
    height: 64px;
    margin-left: 64px;
    padding-top: 20px;
    padding-bottom: 20px;
    padding-left: 53px;
    padding-right: 53px;
    border-radius: 5px;
}


.tomita_submit{
    font-family: Roboto;
    font-style: normal;
    background-color: #7B89C9;
    color:white;
    border: none;
    width: 188px;
    height: 64px;
    font-size: 24px;
    margin-left: 329px;
    margin-top: 77px;
    padding-top: 20px;
    padding-bottom: 20px;
    padding-left: 66px;
    padding-right: 66px;
    border-radius: 8px;
    position: relative;
    z-index: 1;
}


.tomita_review_window{
    position:absolute;
    width:100%;
    height:120vh;
    background:rgba(63,76,88,.4);
    top: 0px;
    z-index: 10;
}

.tomita_review_window_content{
    color: #7B89C9;
    position: absolute;
    width: 846px;
    height: 508px;
    left:297px;
    right:297px;
    top:116px;
    bottom:196px;
    background-color:#FFFFFF;
}



.tomita_btn-close{
    display: inline-block;
    padding-top: 29px;
    padding-right: 35px;
    float: right;
  }
  
  .tomita_btn-close::before,
  .tomita_btn-close::after {
      display: block;
      content: "";
      width: 29px;
      height: 1px;
      background: #000;

  }
  .tomita_btn-close::before {
      transform: rotate(-45deg);
  }
  .tomita_btn-close::after {
      transform: rotate(45deg);
}

.tomita_Review_word{
    font-size: 36px;
    margin-left: 46px;
    margin-top: 54px;
}

.tomita_point{
    background-color:  #F6F7FB;
    border: none;
    width: 117px;
    height: 40px;
    padding-bottom: 3px;
    margin-left: 307px;
    margin-top: 102px;
    border-radius: 2px;
    left: 30px;
}

.tomita_evaluation{
    color: black;
    
}

.tomita_five_point{
    font-family: Roboto;
    font-style: normal;
    display: inline-block; _display: inline;
    color: #666666;
    width: 99px;
    height: 28px;
    margin-left: 15px;
    font-size: 24px;
}


.tomita_oirword_list table{
    background-color: rgb(246, 248, 255);
    font-size: 18px;
    width: 980px;
    height: 48px;
    margin-left: 230px;
    margin-right: 230px;
    margin-bottom: 49px;
    border-radius: 2px;
}

.tomita_oirword{
    width: 243px;
    padding-left: 20px;
    padding-top: 14px;
    padding-bottom: 13px;
}

.tomita_oirmeaning{
    padding-top: 14px;
    padding-bottom: 13px;
}

.review_submit {
  display: block;
  margin: 0 auto;
}

.review_comment {
  background-color: rgb(246, 248, 255);
  border: none;
  display: block;
  margin: 40px auto;
  width: 320px;
  height: 32px;
}
</style>