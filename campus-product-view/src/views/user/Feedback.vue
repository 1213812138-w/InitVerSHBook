<template>
  <div class="post-product-container">
    <div class="post-product-wrapper">
      <div class="post-product">
        <!-- 左侧：填写反馈信息 -->
        <div class="form-section left-section">
          <div class="form-item">
            <p class="form-label">反馈标题</p>
            <input
              class="dialog-input"
              v-model="feedback.title"
              placeholder="请输入反馈标题"
            />
          </div>

          <div class="form-item">
            <p class="form-label">反馈类型</p>
            <el-select
              v-model="feedback.type"
              placeholder="请选择反馈类型"
              size="small"
              style="width: 100%"
            >
              <el-option
                v-for="item in typeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </div>

          <div class="form-item">
            <p class="form-label">反馈内容</p>
            <textarea
              class="dialog-input"
              v-model="feedback.content"
              placeholder="请详细描述问题或建议"
              rows="6"
            ></textarea>
          </div>

          <div class="form-item submit-item">
            <span class="post" @click="submitFeedback">提交反馈</span>
          </div>
        </div>

        <!-- 右侧：图片上传与提示 -->
        <div class="form-section right-section">
          <div class="form-item">
            <p class="form-label">上传截图（可选）</p>
            <el-upload
              ref="uploadRef"
              action="http://localhost:21090/api/campus-product-sys/v1.0/file/upload"
              list-type="picture-card"
              :on-success="handleUploadSuccess"
              :on-remove="handleRemove"
              :on-preview="handlePreview"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="" />
            </el-dialog>
          </div>

          <div class="form-item">
            <p class="form-label">反馈说明</p>
            <p style="font-size: 13px; color: gray; line-height: 1.6;">
              请填写您在使用系统时遇到的问题或建议。您可以上传相关截图帮助我们更快定位问题。
              管理员处理后，您可在“通知”页面查看回复结果。
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FeedbackPost",
  data() {
    return {
      feedback: {
        userId: null, // 登录用户ID（后端可从token获取时，可不传）
        title: "",
        content: "",
        type: "other",
        imageList: ""
      },
      dialogImageUrl: "",
      dialogVisible: false,
      uploadList: [],
      typeList: [
        { label: "Bug问题", value: "bug" },
        { label: "功能建议", value: "suggestion" },
        { label: "投诉反馈", value: "complaint" },
        { label: "其他", value: "other" }
      ]
    };
  },
  methods: {
    /** 上传成功 */
    handleUploadSuccess(response) {
      if (response.code === 200) {
        this.uploadList.push(response.data);
      } else {
        this.$message.error("图片上传失败");
      }
    },
    /** 删除图片 */  
    handleRemove(file, fileList) {
      this.coverList = fileList;
      console.log(file, fileList);
    },
    /** 预览图片 */
     handlePreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    /** 提交反馈 */
    submitFeedback() {
      if (!this.feedback.title) {
        this.$notify.info({ title: "提示", message: "请填写反馈标题" });
        return;
      }
      if (!this.feedback.content) {
        this.$notify.info({ title: "提示", message: "请填写反馈内容" });
        return;
      }

      // 将上传的图片路径拼接成字符串
      this.feedback.imageList = this.uploadList.join(",");

      this.$axios
        .post("/feedback/submit", this.feedback)
        .then((res) => {
          const { data } = res;
          if (data.code === 200) {
            this.$notify.success({
              title: "反馈提交",
              message: "您的反馈已成功提交，感谢支持！"
            });
            this.resetForm();
          } else {
            this.$notify.error({
              title: "反馈提交失败",
              message: data.message || "请稍后再试"
            });
          }
        })
        .catch((error) => {
          this.$notify.error({
            title: "系统错误",
            message: "提交失败：" + error
          });
        });
    },
    /** 重置表单 */
    resetForm() {
      this.feedback = {
        title: "",
        content: "",
        type: "other",
        imageList: ""
      };
      this.uploadList = [];
      if (this.$refs.uploadRef) {
        this.$refs.uploadRef.clearFiles();
      }
    }
  }
};
</script>

<style scoped>
/* 直接复用发布商品页样式结构 */
.post-product-container {
  padding: 30px 0;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.post-product-wrapper {
  max-width: 900px;
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
  resize: none;
}

.dialog-input:focus {
  outline: none;
  border-color: #409eff;
}

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
</style>
