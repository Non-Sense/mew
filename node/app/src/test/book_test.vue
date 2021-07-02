<template>
  <div>
    <div>
      <form id="form" @submit="addBook" action="#">
        <label for="title">タイトル</label>
        <input v-model="title" type="text" id="title" required>
        <br>

        <label for="public">公開する</label>
        <input v-model="isPublic" type="checkbox" id="public">
        <br>

        <input type="submit" value="submit">
      </form>
      <span v-cloak>{{ msg }}</span>
    </div>
    <div>
      <button v-on:click="getBooks">取得</button>
      <table v-show="showFlag">
        <tr>
          <th>bID</th>
          <th>uID</th>
          <th>title</th>
          <th>public</th>
          <th>create</th>
          <th>update</th>
        </tr>
        <tr v-for="item in items" v-bind:key="item.bookId">
          <td>{{item.bookId}}</td>
          <td>{{item.userId}}</td>
          <td>{{item.title}}</td>
          <td>{{item.public}}</td>
          <td>{{item.createdAt}}</td>
          <td>{{item.updatedAt}}</td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import config from "@/const.js"

export default {
  name: "book_test",
  data() {
    return {
      title: "",
      isPublic: false,
      msg: "",
      showFlag: false,
      items:[{bookId:"",userId:"",title:"",public:"",createdAt:"",updatedAt:""}]
    }
  },

  methods: {
    // 単語帳の作成
    addBook(){
      axios.post(config.baseUrl+"/api/book", {
        title: this.title,
        public: this.isPublic
      },
        // アクセストークンを設定する
        {headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName) }
      }).then((res)=>{
        // 新しいトークンがもらえるので反映する (どっかでグローバルな関数作ったほうがいいかも)
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        // 追加したら自動で表を更新する
        this.getBooks()
        this.msg = "OK";
      }).catch((error)=>{
        this.msg = "error: "+error.response.status;
        if(error.response.status === 403){
          // TODO: トークンが切れたのでもう一度ログインしてもらう
          this.$router.push("/test/login");
        }
      })
    },
    
    // 自分の単語帳の取得
    getBooks(){
      axios.get(config.baseUrl+"/api/book",
        {headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName) }
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.items = res.data;
        this.showFlag = true;
        this.msg = "OK";
      }).catch((error)=>{
        this.msg = "error: "+error.response.status;
        if(error.response.status === 403){
          // TODO: トークンが切れたのでもう一度ログインしてもらう
          this.$router.push("/test/login");
        }
      })
    }
  }
}
</script>

<style scoped>
table {
  border-collapse: collapse;
}
td {
  border: solid 1px;
  padding: 0.5em;
}
</style>