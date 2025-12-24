<template>
    <div class="product-list">
  
      <div v-if="!initialLoaded">
      <div class="loading-placeholder">
         <div class="loading-content">
        <i class="el-icon-loading loading-icon"></i>
        
      </div>
      </div>
    </div>
    
    <!-- 已经加载过，但无数据 -->
    <el-row v-else-if="initialLoaded && productList.length === 0">
        <el-empty description="暂无商品信息"></el-empty>
      </el-row>

        <el-row v-else>
            <el-col :span="6" @click.native="route(product)" v-for="(product, index) in productList" :key="index">
                <div class="item-product">
                    <div class="cover">
                        <img :src="coverListParse(product)" alt="" srcset="">
                    </div>

                    <div style="display: flex;justify-content: left;gap: 4px;align-items: center;">
                        <span class="bargain-hover">{{ product.isBargain ? '支持砍价' : '不支持砍价' }}</span>

                        <span class="title">
                            {{ product.name }}
                        </span>

                    </div>

                    <div style="padding-block: 15px;">
                        <span class="decimel-symbol">¥</span>

                        <span class="price">{{ product.price }}</span>

                        <span class="love">4人想要</span>

                    </div>

                    <div class="info">
                        <img :src="product.userAvatar" alt="" srcset="">
                        <span>{{ product.userName }}</span>

                    </div>

                </div>

            </el-col>

        </el-row>
         </div>


</template>

<script>
import { getSearchKey } from "@/utils/storage";
export default {
    data() {
        return {
            searchKey: '',
            keyInterval: null, // 用于存储定时器的引用
            productQueryDto: {},
            productList: [],
              initialLoaded: false, // 新增：加载状态
            keywordKey:'',
            authorKey:''  
        };
    },
    created() {
        this.startKeyLoader(); // 启动定时器
    },
    beforeDestroy() {
        this.clearKeyLoader(); // 清除定时器
    },
    methods: {
        route(product) {
            // 跳转商品详情
            this.$router.push('/product-detail?productId=' + product.id);
        },
        coverListParse(product) {
            if (product.coverList === null) {
                return;
            }
            const newCoverList = product.coverList.split(',');
            return newCoverList[0];
        },
        fetchProduct() {
           
              this.loading = true;  // 开始加载
            this.productQueryDto.name = this.searchKey;
            this.$axios.post('/product/query', this.productQueryDto).then(res => {
                const { data } = res; // 解构
                if (data.code === 200) {
                    this.productList = data.data;
                     this.initialLoaded = true; 
                }
            }).catch(error => {
                console.log("商品查询异常：", error);
            }).finally(() => {
        this.loading = false;  // 结束加载
      });
        },
        loadKey() {
           const searchType = sessionStorage.getItem('searchType') || 'product';
           console.log(searchType);
            if (searchType === 'product') {
            const key = getSearchKey();
            // 使用原有函数
            if (key !== this.searchKey) {
                this.searchKey = key;
                this.productQueryDto.name = key;
                this.fetchProduct();
            }
        } else if (searchType === 'author') {
             const key = getSearchKey();
              console.log(key,"info");
            if (key !== this.authorKey) {
                this.authorKey = key;
                this.productQueryDto.author = key;
                this.fetchProduct();
            }
        } else if (searchType === 'keyword') {
              const key = getSearchKey();
            if (key !== this.keywordKey) {
                this.keywordKey = key;
                this.productQueryDto.subject = key;
                this.fetchProduct();
            }
        }
        },
        startKeyLoader() {
            // 每隔一定时间调用 loadKey 方法
            this.keyInterval = setInterval(() => {
                this.loadKey();
            }, 1000); // 每1000毫秒（1秒）调用一次
        },
        clearKeyLoader() {
            // 清除定时器
            if (this.keyInterval) {
                clearInterval(this.keyInterval);
                this.keyInterval = null; // 重置定时器引用
            }
        }
    }
};
</script>

<style lang="scss" scope>
.product-list {
  padding-block: 20px;
  position: relative; /* 为 loading 定位做准备 */
  
  /* 新增：加载中样式 */
  .loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(255, 255, 255, 0.9);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
    
    .loading-content {
      text-align: center;
      padding: 30px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      
      .loading-icon {
        font-size: 40px;
        color: #409EFF;
        margin-bottom: 15px;
        animation: rotating 2s linear infinite;
      }
      
      .loading-text {
        color: #606266;
        font-size: 16px;
        margin: 0;
      }
    }
  }

  /* 旋转动画 */
  @keyframes rotating {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }

    .item-product {
        padding: 10px 10px 16px 10px;
        box-sizing: border-box;
        border-radius: 15px;
        transition: all .5s;
        cursor: pointer;

        .cover {
            img {
                width: 100%;
                height: 240px;
                border-radius: 10px;
            }
        }

        .bargain-hover {
            font-size: 12px;
            font-weight: 800;
            background-color: rgb(255, 230, 15);
            color: rgb(51, 51, 51);
            border-radius: 2px;
            padding: 2px 6px;
        }

        .title {
            font-size: 20px;
            color: #1f1f1f;
        }

        .decimel-symbol {
            font-size: 14px;
            color: #ff4f24;
            font-weight: 800;
        }

        .price {
            font-size: 24px;
            color: #ff4f24;
            font-weight: 800;
            margin-right: 6px;
        }

        .love {
            font-size: 14px;
            color: #999;
        }

        .info {
            display: flex;
            justify-content: left;
            align-items: center;
            gap: 4px;

            img {
                width: 20px;
                height: 20px;
                border-radius: 50%;
            }

            span {
                font-size: 14px;
                color: #999;
            }
        }


    }

    .item-product:hover {
        box-shadow: 1px 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.06);
    }
}
</style>
