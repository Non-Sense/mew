
<template>
    <div class="wrapper">
      <Header/>
      <main id="main">
        <router-view/>
      </main>
      <Footer/>
    </div>
</template>

<script>
import Header from '@/components/header.vue';
import Footer from '@/components/footer.vue';

export default {
  name: 'App',
  components: {
    Header,
    Footer,
  },
};
</script>

<style lang="css">

/**
 * ここでimportしているscssは
 * 全てのページに適用されます
 */
@import './assets/css/hashimoto.css';
@import './assets/css/taisei.css';
@import './assets/css/common.css';

</style>
