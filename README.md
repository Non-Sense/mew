
## 起動方法
- 前提  
    - Docker, DockerComposeをインストール済み (頑張って調べて)  

1. コマンド叩けるウィンドウを出します  
1. カレントディレクトリを`docker-compose.yml`があるディレクトリにします  
1. `docker-compose up -d`を叩く  

サーバはポート8080に繋がっているので、URLは`http://localhost:8080`です  

起動後は`resources/static`以下のファイルは書き換えて保存すれば表示に反映されると思います  
javaコードはビルドかければ自動でリロードしてくれます。コマンド:`./gradlew classes`

***
### Tips
- jarを吐く  
  `./gradlew build`でリリース用のjarファイルを吐き出してくれます  
  この時はビルド時にテストが走るので時間がかかります  
  jarは`/srv/build/libs`にあります  
  実行は`java -jar [jarファイル]`  


- デバッグ  
  ポート5005にリモートデバッガ繋げてください
***