<template>
  <div>
    <div class="detail-content" v-html="product.detail"></div>
  </div>
</template>
<script>
import product from "@/views/user/Product.vue";

export default {
  name: 'Comment',

  data() {
    return {
    product:null
    };
  },
  created() {
    this.getParam();
  },
  methods: {
    /**
     * 从路径上取得商品ID
     */
    getParam() {
      const param = this.$route.query;
      this.productId = Number(param.productId);
      this.fetchProduct(this.productId);
      console.log(this.productId);
    },
    fetchProduct(productId) {
      this.$axios
          .post("/product/query", { id: productId })
          .then((res) => {
            const { data } = res; // 解构
            if (data.code === 200) {
              this.product = data.data[0];
              this.coverListParse(this.product);
              this.querySaveStatus();
            }
          })
          .catch((error) => {
            console.log("商品查询异常：", error);
          });
    },

  }
};
</script>
<style scoped lang="scss">
</style>