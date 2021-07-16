<template>
  <div class="taisei_Obody">
    <div class="taisei_list">
      <ul>
        <li>
          <p>Online List</p>
        </li>
        <li>
          <input v-model="search" type="text" placeholder="    Search" />
        </li>
      </ul>
    </div>
    
    <div class="taisei_WB">
      <p>並び替え:評価の高い順</p>
      <ul>
        <li>
          <input type="button" value="  数学単語帳" />
          <button>評価 3.5</button>
        </li>
        <li>
          <input type="button" value="  物理単語帳" />
          <button>評価 3.4</button>
        </li>
        <li>
          <input type="button" value="  xx単語帳" />
          <button>評価 3.3</button>
        </li>
        <li>
          <input type="button" value="  yy単語帳" />
          <button>評価 3.2</button>
        </li>
      </ul>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import config from "@/const.js"

export default {
  name: "online_books",
  data() {
    return {
    }
  },
  // ロード終了時に取得してくる?
  created: function (){
    this.getWords();
  },
  methods: {
    // 公開状態の単語帳を取得する
    // 検索文字列を空かnullにしておけば全件取得されるはず
    // たぶん評価が高い順に並んでいると思う(たぶん)
    findBooks(){
      axios.get(config.baseUrl+"/api/book/public", {
        params: {
          title: "" // <- TODO 検索する単語帳名を入れる
        }, headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
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
            // 件数が0
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