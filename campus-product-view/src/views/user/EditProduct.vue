<template>
  <div class="post-product-container">
    <div class="post-product-wrapper">
      <div class="post-product">
        <div class="form-section left-section">
          <div class="form-item">
            <p class="form-label">商品名</p>
            <input
              class="dialog-input"
              v-model="product.name"
              placeholder="请填写商品名"
            />
          </div>
          <div class="form-item">
            <p class="form-label">作者</p>
            <input
              class="dialog-input"
              v-model="product.author"
              placeholder="请填写作者"
            />
          </div>
          <div class="form-item">
            <p class="form-label">主题</p>
            <input
              class="dialog-input"
              v-model="product.subject"
              placeholder="请填写主题"
            />
          </div>
          <div class="form-item">
            <p class="form-label">新旧程度</p>
            <el-input-number
              v-model="product.oldLevel"
              @change="handleChange"
              :min="1"
              :max="10"
              label="新旧程度"
            ></el-input-number>
          </div>

          <div class="form-item">
            <p class="form-label">价格</p>
            <input
              class="dialog-input"
              v-model="product.price"
              placeholder="请填写价格"
            />
          </div>

          <div class="form-item">
            <p class="form-label">是否支持砍价</p>
            <el-switch
              v-model="product.isBargain"
              active-text="支持砍价"
              inactive-text="不支持砍价"
            >
            </el-switch>
          </div>

          <div class="form-item submit-item">
            <span class="post" @click="editProduct">修改商品</span>
          </div>
        </div>

        <div class="form-section right-section">
          <div class="form-item">
            <p class="form-label">产品图</p>
            <el-upload
              action="http://localhost:21090/api/campus-product-sys/v1.0/file/upload"
              list-type="picture-card"
              :on-success="handlePictureCardSuccess"
              :file-list="coverList"
              :on-preview="handlePictureCardPreview"
              :on-remove="handleRemove"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="" />
            </el-dialog>
          </div>

          <div class="form-item">
            <p class="form-label">所属商品类别</p>
            <div class="category-container">
              <span
                :style="{
                  backgroundColor:
                    categorySelected.id === category.id
                      ? 'rgb(76, 147, 209)'
                      : '',
                  color:
                    categorySelected.id === category.id
                      ? 'rgb(244,244,244)'
                      : '',
                }"
                class="category-item"
                @click="categoryClick(category)"
                v-for="(category, index) in categoryList"
                :key="index"
              >
                {{ category.name }}
              </span>
            </div>
          </div>

          <div class="form-item">
            <p class="form-label">库存</p>
            <el-input-number
              v-model="product.inventory"
              @change="handleInventoryChange"
              :min="1"
              :max="10000"
              label="新旧程度"
            ></el-input-number>
          </div>

          <div class="form-item editor-item">
            <p class="form-label">详细信息</p>
            <Editor
              height="calc(100vh - 700px)"
              :receiveContent="product.detail"
              @on-receive="onReceive"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Editor from "@/components/Editor"
import { getProductInfo } from "@/utils/storage"
export default {
    components: { Editor },
    name: 'PostProduct',
    data() {
        return {
            product: {},
            categorySelected: {}, // 当前选中的商品类别
            dialogImageUrl: '',
            dialogVisible: false,
            coverList: [],
            categoryList: []
        };
    },
    created() {
        this.getStorageProductInfo();
        this.fetchCategoryList();
    },
    methods: {
        getStorageProductInfo() {
            this.product = getProductInfo();
            this.coverListParse(this.product)
        },
        /**
         * 商品封面图处理
         * 从字符串转成可用数组
         * @param {*} product 待处理商品信息
         */
        coverListParse(product) {
            if (product.coverList === null) {
                return;
            }
            this.coverList = product.coverList.split(',').map(coverEntity => {
                return {
                    uid: Date.now() + Math.floor(Math.random() * 10001),
                    name: Date.now() + Math.floor(Math.random() * 10001),
                    status: 'success',
                    url: coverEntity
                }
            })
        },
        /**
         * 修改商品信息
         */
        editProduct() {
            if (this.product.name === undefined) {
                this.$notify.info({
                    duration: 1000,
                    title: '填写提醒',
                    message: '商品标题不能为空'
                });
                return;
            }
            if (this.product.price === undefined) {
                this.$notify.info({
                    duration: 1000,
                    title: '填写提醒',
                    message: '商品价格不能为空'
                });
                return;
            }
            if (this.coverList.length === 0) {
                this.$notify.info({
                    duration: 1000,
                    title: '上传提醒',
                    message: '请上传封面'
                });
                return;
            }
            const coverUrlList = this.coverList.map(entity => entity.url);
            this.product.coverList = coverUrlList.join(',');
            // 商品封面需要处理
            this.$axios.put('/product/update', this.product).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.$notify.success({
                        title: '修改操作',
                        message: data.msg
                    });
                    // 后续清除之后，跳转我的商品页面
                    this.$router.push('/myProduct');
                }
            }).catch(error => {
                this.$notify.error({
                    title: '修改操作',
                    message: error
                });
                console.log("修改商品异常:", error);
            });
        },
        /**
         * 富文本编辑器回调内容
         * @param {*} detail 
         */
        onReceive(detail) {
            this.product.detail = detail;
        },
        /**
         * 商品分类选中事件
         * @param {*} category 商品分类
         */
        categoryClick(category) {
            this.categorySelected = category;
            this.product.categoryId = category.id;
        },
        /**
         * 查询商品类别
         */
        fetchCategoryList() {
            this.$axios.post('/category/query', {}).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.categoryList = data.data;
                    // 默认选中第一项
                    this.categoryClick(this.categoryList[0]);
                }
            }).catch(error => {
                console.log("查询商品类别信息异常:", error);
            });
        },
        /**
         * 封面上传成功响应事件
         * @param {*} file 
         * @param {*} fileList 
         */
        handlePictureCardSuccess(file, fileList) {
            this.coverList.push({
                uid: Date.now() + Math.floor(Math.random() * 10001),
                name: Date.now() + Math.floor(Math.random() * 10001),
                status: 'success',
                url: file.data
            });
        },
        handleRemove(file, fileList) {
            this.coverList = fileList
            console.log(file, fileList);
        },
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;
            this.dialogVisible = true;
        }
    }
};
</script>

<style scoped lang="scss">
.post {
    display: inline-block;
    background-color: rgb(51, 51, 51);
    margin-block: 30px;
    width: 100%;
    text-align: center;
    padding: 15px;
    box-sizing: border-box;
    color: rgb(245, 245, 245);
    border-radius: 5px;
    font-size: 14px;
    cursor: pointer;
}

.post:hover {
    background-color: rgb(31, 31, 31);
}

.category-item {
    display: inline-block;
    padding: 6px 20px;
    background-color: rgb(246, 246, 246);
    border-radius: 5px;
    font-size: 14px;
    cursor: pointer;
    margin: 4px;
}

.category-item:hover {
    background-color: rgb(241, 241, 241);
}

.post-product {
    display: flex;
    justify-content: left;
    gap: 30px;
}
/* 容器布局 */
.post-product-container {
  padding: 30px 0;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.post-product-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.post-product {
  display: flex;
  gap: 30px;
  background-color: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 左右分栏 */
.form-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.left-section {
  border-right: 1px solid #e4e7ed;
  padding-right: 30px;
}

.right-section {
  padding-left: 30px;
}

/* 表单项样式 */
.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  margin: 0;
  font-weight: 600;
  color: #303133;
  font-size: 14px;
}

.dialog-input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.3s;
  box-sizing: border-box;
}

.dialog-input:focus {
  outline: none;
  border-color: #409eff;
}

/* 分类标签容器 */
.category-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 5px;
}

.category-item {
  padding: 6px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.3s;
}

.category-item:hover {
  border-color: #c0c4cc;
}

/* 提交按钮 */
.submit-item {
  margin-top: auto;
  padding-top: 20px;
}

.post {
  display: inline-block;
  width: 100%;
  padding: 12px;
  background-color: #409eff;
  color: white;
  text-align: center;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s;
}

.post:hover {
  background-color: #66b1ff;
}

/* 编辑器区域 */
.editor-item {
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* ============= 响应式设计 ============= */

/* 大屏幕设备 (1200px 以上) */
@media (min-width: 1200px) {
  .post-product-wrapper {
    max-width: 1140px;
  }
}

/* 中等屏幕设备 (992px - 1199px) */
@media (max-width: 1199px) {
  .post-product-wrapper {
    max-width: 960px;
    padding: 0 15px;
  }

  .post-product {
    gap: 25px;
    padding: 25px;
  }

  .left-section {
    padding-right: 25px;
  }

  .right-section {
    padding-left: 25px;
  }
}

/* 平板设备 (768px - 991px) */
@media (max-width: 991px) {
  .post-product-wrapper {
    max-width: 720px;
  }

  .post-product {
    flex-direction: column;
    gap: 20px;
  }

  .left-section,
  .right-section {
    padding: 0;
    border: none;
  }

  .left-section {
    border-bottom: 1px solid #e4e7ed;
    padding-bottom: 20px;
    margin-bottom: 20px;
  }

  .form-item {
    gap: 10px;
  }
}

/* 小屏幕设备 (576px - 767px) */
@media (max-width: 767px) {
  .post-product-container {
    padding: 15px 0;
  }

  .post-product-wrapper {
    max-width: 540px;
    padding: 0 12px;
  }

  .post-product {
    padding: 20px 15px;
    gap: 15px;
  }

  .form-section {
    gap: 15px;
  }

  .form-label {
    font-size: 13px;
  }

  .dialog-input {
    padding: 8px 10px;
    font-size: 13px;
  }

  .category-item {
    padding: 5px 10px;
    font-size: 12px;
  }

  .post {
    padding: 10px;
    font-size: 14px;
  }
}

/* 超小屏幕设备 (575px 以下) */
@media (max-width: 575px) {
  .post-product-container {
    padding: 10px 0;
  }

  .post-product-wrapper {
    padding: 0 10px;
  }

  .post-product {
    padding: 15px 10px;
    border-radius: 6px;
  }

  .left-section {
    padding-bottom: 15px;
    margin-bottom: 15px;
  }

  .form-section {
    gap: 12px;
  }

  .form-item {
    gap: 6px;
  }

  .category-container {
    gap: 6px;
  }

  .category-item {
    padding: 4px 8px;
    font-size: 11px;
  }
}

/* 针对移动端触摸设备的优化 */
@media (hover: none) and (pointer: coarse) {
  .dialog-input {
    font-size: 16px; /* 防止iOS缩放 */
  }

  .category-item {
    padding: 8px 12px; /* 增加触摸目标大小 */
    margin: 2px;
  }

  .post {
    padding: 14px;
  }
}
</style>
