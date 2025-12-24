<template>
  <div class="detail-container">
    <!-- 左侧区域：卖家信息 + 图集 -->
    <div class="left-section">
      <!-- 卖家信息放在左侧图集上方 -->
      <div class="seller-info-top">
        <img :src="product.userAvatar" class="seller-avatar" alt="卖家头像" />
        <div class="seller-details">
          <span class="seller-name">{{ product.userName }}的小店</span>
        </div>
      </div>

      <!-- 左侧图集 -->
      <div class="cover-section">
        <div class="cover-thumbnails">
          <div
            :style="{
              border: coverIndex === index ? '1px solid rgb(51,51,51)' : '',
            }"
            @click="coverSelected(coverItem, index)"
            class="cover-item"
            v-for="(coverItem, index) in coverList"
            :key="'cover-' + index"
          >
            <img :src="coverItem" alt="" />
          </div>
        </div>

        <div class="large-cover">
          <div @click="coverToLeft">
            <i class="el-icon-arrow-left"></i>
          </div>
          <img :src="coverItem" alt="" />
          <div @click="coverToRight">
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>
      </div>
    </div>
    <!-- 右侧信息 -->
    <div class="info-section">
      <!-- 书本名 -->
      <div class="book-name">
        {{ product.name }}
      </div>

      <!-- 作者 -->
      <div class="info-row">
        <span class="label">作者：</span>
        <span class="value">{{ product.author }}</span>
      </div>

      <!-- 关键词 -->
      <div class="info-row">
        <span class="label">关键词：</span>
        <span class="value">{{ product.subject }}</span>
      </div>

      <div class="bargain-row">
        <span class="bargain-tag">
          {{ product.isBargain ? "支持砍价" : "不支持砍价" }}
        </span>
      </div>
      <!-- 新旧程度 -->
      <div class="info-row old-level">
        <span class="value">新旧程度:{{ product.oldLevel }}成新</span>
      </div>

      <!-- 库存和分类 -->
      <div class="inventory-row">
        <span class="inventory">库存:{{ product.inventory }}本</span>
        <span class="separator"></span>
      </div>
      <span class="category">类别:{{ product.categoryName }}</span>
      <!-- 价格 -->
      <div class="price-section">
        <span class="price">
          <span class="symbol">￥</span>
          {{ product.price }}
        </span>
      </div>

      <!-- 操作按钮 -->
      <div class="operation">
        <div class="left">
          <span class="want-btn" @click="likeProduct">
            <i class="el-icon-sell"></i>
            我想要
          </span>
          <span class="buy-btn" @click="buyProduct">立即购买</span>
        </div>
        <div class="right">
          <span class="collect-btn" @click="saveOperation">
            <i style="margin-right: 5px;" class="el-icon-star-off"></i>
            {{ saveFlag ? "取消收藏" : "收藏" }}
          </span>
        </div>
      </div>
    </div>

    <!-- 商品详细信息（放在左右两侧下方） -->
    <div class="detail-section">

      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="详细信息" name="first">
          <div class="detail-content" v-html="product.detail"></div>
        </el-tab-pane>

        <el-tab-pane label="相关评论" name="second">
          <div v-if="userInfo !== null">
            <Evaluations contentType="PRODUCT" :contentId="product.id"/>
          </div>
        </el-tab-pane>

      </el-tabs>

    </div>
    <el-dialog
      :show-close="false"
      :visible.sync="dialogProductOperaion"
      width="35%"
    >
      <div style="padding:16px 20px;">
        <h3 style="
  font-size: 22px;
  font-weight: 600;
  color: #333;
  margin: 0 0 20px 0;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
">
  商品下单
</h3>
        <div class="seller-info-top">
          <span class="seller-name">卖家</span>
          <img :src="product.userAvatar" class="seller-avatar" alt="卖家头像" />
          <div class="seller-details">
            <span class="seller-name">{{ product.userName }}</span>
          </div>
        </div>
        <div class="info">
          <div class="decimal">
            <span class="price"
              ><span class="symbol">￥</span>{{ product.price }}</span
            >

            <span
              style="border: 2px solid rgb(214, 214, 214);border-radius: 50%;"
            ></span>
          </div>
          <div class="name">
            <span class="seller-name">书名:{{ product.name }}</span>
          </div>
          <!-- 新旧程度 -->
          <div class="info-row old-level">
            <span class="value">新旧程度:{{ product.oldLevel }}成新</span>
          </div>

          <!-- 库存和分类 -->
          <div class="inventory-row">
            <span class="inventory">库存:{{ product.inventory }}本</span>
            <span class="separator"></span>
          </div>
        </div>

        <div >
          <p style="font-size: 16px; font-weight: 600; color: #333; margin-bottom: 8px;">下单数量</p>

          <el-input-number
            v-model="buyNumber"
            :min="1"
            :max="product.inventory"
            label="请选择"
          ></el-input-number>
        </div>

        <div>
          <p style="font-size: 16px; font-weight: 600; color: #333; margin-bottom: 8px;">备注信息</p>

          <el-input
            type="textarea"
            :rows="3"
            placeholder="补充备注"
            v-model="detail"
          >
          </el-input>
        </div>
      </div>

      <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
        <span class="channel-button" @click="cancelBuy()"  style="
      display: inline-block;
      padding: 10px 24px;
      font-size: 16px;
      border-radius: 4px;
      cursor: pointer;
      margin-right: 12px;
    ">
          取消下单
        </span>

        <span class="edit-button" @click="buyConfirm()"  style="
      display: inline-block;
      padding: 10px 24px;
      font-size: 16px;
      border-radius: 4px;
      cursor: pointer;
      margin-right: 12px;
    ">
          确定下单
        </span>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { getUserInfo } from "@/utils/storage";
import Evaluations from "@/components/Evaluations"
import BuyOrders from "@/views/user/BuyOrders.vue";
import MyOrders from "@/views/user/MyOrders.vue";
import ProductDetInfo from "@/views/user/ProductDetInfo.vue";
export default {
  name: "ProductDetail",
  components: {ProductDetInfo, MyOrders, BuyOrders,Evaluations },
  data() {
    return {
      productId: null,
      product: {},
      coverList: [],
      coverIndex: 0,
      coverItem: null,
      keyInterval: null,
      saveFlag: false, // 判断用户是否已经收藏
      dialogProductOperaion: false, // 控制对话框显示
      buyNumber: 1, // 购买数量
      detail: "", // 备注信息
      activeName: 'first',
      userInfo: null
    };
  },
  created() {
    this.getParam();
    this.viewOperation();
  },
  beforeDestroy() {
    this.clearBanner(); // 清除定时器
  },
  methods: {
    viewOperation() {
      const userInfo = getUserInfo();
      if (userInfo === null) { // 没登录不用记录
        return;
      }
      this.userInfo = userInfo;
      // 对于用户这是无感的
      this.$axios.post(`/interaction/view/${this.productId}`).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          console.log("用户浏览已经处理");
        }
      }).catch(error => {
        console.log("浏览记录异常：", error);
      })
    },
    handleClick(index,tab){

    },
    buyConfirm() {
      const ordersDTO = {
        productId: this.product.id,
        buyNumber: this.buyNumber,
        detail: this.detail,
      };
      this.$axios
        .post(`/product/buyProduct`, ordersDTO)
        .then((res) => {
          const { data } = res; // 解构
          if (data.code === 200) {
            this.$notify({
              duration: 1000,
              title: "下单操作",
              message: data.msg,
              type: "success",
            });
            this.fetchProduct(this.product.id);
            this.cancelBuy();
          } else {
            this.$notify({
              duration: 2000,
              title: "下单操作",
              message: data.msg,
              type: "error",
            });
          }
        })
        .catch((error) => {
          this.$notify({
            duration: 2000,
            title: "下单操作",
            message: error,
            type: "error",
          });
          console.log("商品下单异常：", error);
        });
    },
    cancelBuy() {
      this.dialogProductOperaion = false;
      this.buyNumber = 1;
    },
    buyProduct() {
      this.dialogProductOperaion = true;
    },
    /**
     * 想要 按钮点击事件
     */
    likeProduct() {
      this.$axios
        .post(`/interaction/likeProduct/${this.product.id}`)
        .then((res) => {
          const { data } = res;
          if (data.code === 200) {
            this.$notify({
              duration: 1000,
              title: "想要操作通知",
              message: data.msg,
              type: "success",
            });
          } else {
            this.$notify({
              duration: 2000,
              title: "想要操作通知",
              message: data.msg,
              type: "info",
            });
          }
        })
        .catch((error) => {
          console.log("商品---想要---异常：", error);
        });
    },
    /**
    查询收藏状态 */
    querySaveStatus() {
      const userInfo = getUserInfo();
      if (userInfo === null) {
        console.log("用户没登录");
        return;
      }
      const interactionQueryDto = {
        userId: userInfo.id,
        productId: this.product.id,
        type: 1, // 1 代表收藏
      };
      this.$axios
        .post("/interaction/query", interactionQueryDto)
        .then((res) => {
          if (res.data.code === 200) {
            this.saveFlag = res.data.total !== 0;
          }
        })
        .catch(console.log);
    },
    /**
     * 收藏 取消收藏操作
     */
    saveOperation() {
      this.$axios
        .post(`/interaction/saveOperation/${this.product.id}`)
        .then((res) => {
          if (res.data.code === 200) {
            this.saveFlag = res.data.data; // 更新状态
            this.$notify({
              // 弹出提示
              duration: 1000,
              title: "收藏操作成功",
              message: res.data.msg,
              type: "success",
            });
          }
        })
        .catch(console.log);
    },
    clearBanner() {
      if (this.keyInterval) {
        clearInterval(this.keyInterval);
        this.keyInterval = null; // 重置定时器引用
      }
    },
    startBanner() {
      this.keyInterval = setInterval(() => {
        if (this.coverIndex === this.coverList.length - 1) {
          this.coverIndex = 0;
        } else {
          this.coverIndex = this.coverIndex + 1;
        }
        this.coverItem = this.coverList[this.coverIndex];
      }, 5000);
    },
    coverToLeft() {
      if (this.coverIndex === 0) {
        this.coverIndex = this.coverList.length - 1;
      } else {
        this.coverIndex = this.coverIndex - 1;
      }
      this.coverItem = this.coverList[this.coverIndex];
    },
    coverToRight() {
      if (this.coverIndex === this.coverList.length - 1) {
        this.coverIndex = 0;
      } else {
        this.coverIndex = this.coverIndex + 1;
      }
      this.coverItem = this.coverList[this.coverIndex];
    },
    coverSelected(coverItem, index) {
      this.coverItem = coverItem;
      this.coverIndex = index;
    },
    /**
     * 从路径上取得商品ID
     */
    getParam() {
      const param = this.$route.query;
      this.productId = Number(param.productId);
      this.fetchProduct(this.productId);
    },
    coverListParse(product) {
      if (product.coverList === null) {
        return;
      }
      this.coverList = product.coverList.split(",");
      // 默认选中第一张封面
      this.coverItem = this.coverList[0];
      // 启动定时器，定时轮播
      this.startBanner();
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
  },
};
</script>

<style scoped lang="scss">
.left-section {
  flex: 1;
  min-width: 300px;
  display: flex;
  flex-direction: column;
  gap: 80px;
}

.detail-container {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
/* 卖家信息在左侧图集上方 */
.seller-info-top {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.seller-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.seller-details {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.seller-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}
/* 左右两侧容器 */
.cover-section,
.info-section {
  flex: 1;
  min-width: 300px;
}

/* 左侧图集部分 */
.cover-section {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.cover-thumbnails {
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-height: 400px;
  overflow-y: auto;
}

.cover-item {
  width: 60px;
  height: 60px;
  cursor: pointer;
  border-radius: 4px;
  overflow: hidden;
  transition: border 0.3s;
}

.cover-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.large-cover {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
}

.large-cover img {
  width: 100%;
  max-width: 400px;
  height: auto;
  border-radius: 8px;
}

.large-cover i {
  font-size: 24px;
  cursor: pointer;
  color: #666;
  transition: color 0.3s;
}

.large-cover i:hover {
  color: #333;
}

/* 右侧信息部分 */
.info-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.book-name {
  font-size: 36px;
  font-weight: bold;
  color: #333;
  line-height: 1.4;
  margin-bottom: 5px;
}

.info-row {
  font-size: 20px;
  color: #eee8e8;
}

.info-row .label {
  color: #ba1e1e;
  margin-right: 0px;
  font-weight: bold;
}

.info-row .value {
  color: #200505;
  font-weight: bold;
}

.seller-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  margin: 5px 0;
}

.seller-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.seller-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}

.seller-name {
  font-size: 20px;
  color: #333;
  font-weight: bold;
}

.bargain-row {
  margin: 3px 0;
  display: flex;
  justify-content: flex-start;
}

.bargain-tag {
  padding: 8px 20px;
  background-color: #2458c1;
  color: #f3eaec;
  border-radius: 20px;
  font-size: 16px;
  border: 1px solid #e0e0e0;
  /* 如果需要更大的尺寸可以调整以下值 */
  /* font-size: 18px; */
  /* padding: 10px 25px; */
}
.old-level {
  margin: 5px 0;
}

.old-level .value {
  font-weight: bold;
  color: #333;
}

.inventory-row {
  display: flex;
  align-items: center;
  gap: 15px;
  margin: 5px 0;
}

.inventory {
  font-size: 20px;
  color: #333;
  font-weight: bold;
}

.separator {
  width: 4px;
  height: 4px;
  background-color: #d6d6d6;
  border-radius: 50%;
}

.category {
  font-size: 20px;
  color: #333;
  font-weight: bold;
}

.price-section {
  margin: 15px 0;
  padding: 10px 0;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.price {
  font-size: 28px;
  color: #ff6b6b;
  font-weight: bold;
}

.price .symbol {
  font-size: 20px;
}

/* 操作按钮（保持原有样式） */
.operation {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.operation .left {
  display: flex;
  gap: 15px;
}

.want-btn,
.buy-btn,
.collect-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 10px 25px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 18px;
  transition: all 0.3s;
  border: none;
}

.want-btn {
  background-color: #ff6b6b;
  color: white;
}

.want-btn:hover {
  background-color: #ff5252;
}

.buy-btn {
  background-color: #1890ff;
  color: white;
}

.buy-btn:hover {
  background-color: #096dd9;
}

.collect-btn {
  background-color: #fff;
  color: #666;
  border: 1px solid #d9d9d9 !important;
}

.collect-btn:hover {
  color: #1890ff;
  border-color: #1890ff !important;
}

.operation i {
  margin-right: 5px;
}

/* 商品详细信息（放在左右两侧下方） */
.detail-section {
  font-size: 20px;
  width: 100%;
  margin-top: -5px;
  padding-top: -5px;
  border-top: 1px solid #eee;
  /* 直接针对标签内的文字 */
  ::v-deep .el-tabs__item span {
    font-size: 18px !important;
    font-weight: 500 !important;
  }

  ::v-deep .el-tabs__item.is-active span {
    font-size: 20px !important;
    font-weight: 600 !important;
  }
}


.detail-content {
  line-height: 1.6;
  color: #333;
  font-size: 15px;
}

.detail-content >>> * {
  max-width: 100%;
}
.detail {
  font-size: 30px;
  color: #333;
  font-weight: bold;
}

.info {
  width: 500px;

  .operation {
    display: flex;
    justify-content: left;
    gap: 20px;
    font-size: 14px;
    cursor: pointer;

    .right {
      span:hover {
        background-color: rgb(241, 241, 241);
      }

      span {
        display: inline-block;
        width: 100px;
        text-align: center;
        background-color: rgb(246, 246, 246);
        border-radius: 20px;
      }
    }

    .left {
      display: flex;
      justify-content: space-evenly;

      span {
        display: inline-block;
        width: 160px;
        text-align: center;
      }

      span:first-child {
        background-color: rgb(255, 230, 15);
        border-top-left-radius: 20px;
        border-bottom-left-radius: 20px;
      }

      span:last-child {
        background-color: rgb(59, 59, 59);
        color: rgb(245, 245, 245);
        border-top-right-radius: 20px;
        border-bottom-right-radius: 20px;
      }
    }

    div {
      line-height: 40px;
    }
  }

  .name {
    margin-block: 10px;
    font-size: 24px;
  }

  .decimal {
    display: flex;
    justify-content: left;
    align-items: center;
    gap: 10px;
    font-size: 14px;
    margin-block: 6px;
    width: 500px;

    .price {
      .symbol {
        font-size: 16px;
      }

      font-size: 32px;
      font-weight: 800;
      color: rgb(255, 68, 0);
    }

    .bargain {
      font-size: 10px;
      color: rgb(51, 51, 51);
      background-color: rgb(246, 228, 24);
      padding: 2px 4px;
      border-radius: 5px;
    }
  }
}

</style>
