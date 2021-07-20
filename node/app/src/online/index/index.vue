<template>
  <div class="tomita_oibody">
    <div class="tomita_review_window" v-if="modalFlag">
      <div style="display: inline-block;  _display: inline;" class="tomita_review_window_content">
        <span class="tomita_btn-close" v-on:click="modal"></span>
          <div class="tomita_Review_word">
            Review
          </div>
          <div class="tomita_evaluation">
            <input type="number" name="point" value="point" class="tomita_point" min="0" max="5" v-model="point">
              <div class="tomita_five_point">
                / 5.0点
              </div>
          <div>
          <input type="text" class="review_comment" placeholder="コメント" v-model="comment">
          <input type="submit" name="submit" value="送信" class="tomita_submit review_submit" v-on:click="postReview">
        </div>
      </div>
    </div>
  </div>

  <!-- <form action="#" @submit="findWord"> -->
    <div>
    <div class="tomita_oitop">
      <p>{{bookname}}
      <input type="search" name="search" v-model="search" v-on:change="findWord" placeholder="Search" class="tomita_oisearch">
      <input type="button" name="Review" value="Review" class="tomita_oiReview" v-on:click="modal">
      </p>

    </div>
  <!-- </form> -->
  </div>

  <div v-for="item in items" v-bind:key="item.wordId" v-show="showFlag">
    <div class="tomita_miword_list">
        <table>
          <tr>
            <td class="tomita_oiword">{{item.word}}</td>
            <td class="tomita_oimeaning">{{item.mean}}</td>
          </tr>
        </table>
    </div>
  </div>
  <div class="comment">
    <table>
      <tr>
        <th>UserName</th>
        <th>Comment</th>
      </tr>
      <tr v-for="item in comments" v-bind:key="item.commentId">
        <td>{{item.userName}}</td>
        <td>{{item.comment}}</td>
      </tr>
    </table>
  </div>
</div>



</template>
<script>
import axios from "axios";
import config from "@/const.js"


export default {
  name: "online_index",
  data() {
    return {
      id:0,
      modalFlag: false,
      search:"",
      bookname:"",
      comment:"",
      point:null,
      showFlag:true,
      items:[{wordId:"",bookId:"",word:"",mean:"",userId:"",createdAt:"",updatedAt:""}],
      comments:[{commentId:"",bookId:"",userId:"",userName:"",comment:"",createdAt:""}]
    }
  },
  created: function () {
    this.id = this.$route.params.id;
    this.getMyRate();
    //this.getRate();
    this.getWord();
    this.getComment();
    this.getBook();
  },
  methods: {
    // 単語帳IDで単語一覧取得
    getWord(){
      let bookId = this.id;
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
        this.showFlag=true;
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/login");
            break;
          case 404:
            this.showFlag=false;
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
    // getRate(){
    //   let bookId = this.id;
    //   if(bookId == null) {
    //     // パラメータが設定されていない
    //     console.error("getParam: id == null");
    //     return;
    //   }
    //   axios.get(config.baseUrl+"/api/book/"+bookId+"/rate", {
    //     headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
    //   }).then((res)=>{
    //     this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
    //     this.items=res.data;
    //   }).catch((error)=>{
    //     switch (error.response.status){
    //       case 400:
    //         // リクエストが不正
    //         break;
    //       case 403:
    //         this.$router.push("/login");
    //         break;
    //       case 404:
    //         // 閲覧できない単語帳・そもそも無い
    //         break;
    //       case 500:
    //         // サーバ内部エラー
    //         break;
    //       default:
    //         // 多分サーバが死んでいる
    //     }
    //   })
    // },
    getMyRate(){
      let bookId = this.id;
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/book/"+bookId+"/myrate", {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.point=res.data.rate;
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/login");
            break;
          case 404:
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
      let bookId = this.id;
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/book/"+bookId+"/comment", {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.comments=res.data;
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
            break;
          case 500:
            // サーバ内部エラー
            break;
          default:
            // 多分サーバが死んでいる
        }
      })
    },
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
        this.bookname=res.data.title;
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/login");
            break;
          case 404:
            this.showFlag=true;
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
    // 単語帳IDで評価値を送信
    postRate(){
      let t = this;
      return new Promise(function (resolve, reject) {
        let bookId = t.id;
        if (bookId == null) {
          // パラメータが設定されていない
          console.error("getParam: id == null");
          return;
        }
        axios.post(config.baseUrl + "/api/book/" + bookId + "/rate", {
          rate: t.point
        }, {
          headers: {"X-AUTH-TOKEN": t.$cookies.get(config.cookieName)}
        }).then((res) => {
          t.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
          if (res.status === 200) {
            resolve();
          }
        }).catch((error) => {
          switch (error.response.status) {
            case 400:
              // リクエストが不正
              break;
            case 403:
              t.$router.push("/login");
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
          reject();
        })
      })
    },
    // 単語帳IDでコメントを送信
    postComment(){
      let t = this;
      return new Promise(function (resolve, reject) {
        let bookId = t.id;
        if (bookId == null) {
          // パラメータが設定されていない
          console.error("getParam: id == null");
          return;
        }
        axios.post(config.baseUrl + "/api/book/" + bookId + "/comment", {
          comment: t.comment
        }, {
          headers: {"X-AUTH-TOKEN": t.$cookies.get(config.cookieName)}
        }).then((res) => {
          t.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
          if (res.status === 200) {
            resolve();
          }
        }).catch((error) => {
          switch (error.response.status) {
            case 400:
              // リクエストが不正
              break;
            case 403:
              t.$router.push("/login");
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
          reject();
        })
      });
    },

    // 2件の送信をして両方成功でモーダルを閉じる
    postReview(){
      Promise.all([this.postRate(),this.postComment()])
      .then(()=>{
        this.modalFlag = false;
      }).catch(()=>{});
    },

    // 単語帳IDを指定して単語を検索
    findWord(){
      let searchWord = this.search;
      let searchMean = this.search;
      let bookId = this.id;
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
        this.showFlag=true;
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/login");
            break;
          case 404:
            this.showFlag=false;
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
    background-color:  rgb(144, 143, 228);
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
    background-color: rgb(144, 143, 228);
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
    background-color: rgb(144, 143, 228);
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
    color: rgb(144, 143, 228);
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

.comment {
  padding: 80px 150px;
  background-color: (255,255,255,0.8);
}
</style>