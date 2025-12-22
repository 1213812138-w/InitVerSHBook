<template>
  <div class="feedback-container">
    <!-- 顶部刷新按钮 -->
    <div class="refresh-feedback">
      <span @click="fetchFeedback">
        <i class="el-icon-refresh"></i>
      </span>
    </div>

    <!-- 反馈列表 -->
    <div class="item" v-for="(fb, index) in feedbackList" :key="index">
    

      <!-- 内容 -->
      <div class="content-box">
        <div class="header-line">
          <div class="title"><strong>标题：</strong>{{ fb.title }}</div>
          <!-- 标签区 -->
          
          <div class="tags">
            <el-tag
              size="mini"
              type="info"
              v-if="fb.type === 'bug'"
              >Bug</el-tag
            >
            <el-tag
              size="mini"
              type="success"
              v-else-if="fb.type === 'suggestion'"
              >建议</el-tag
            >
            <el-tag
              size="mini"
              type="warning"
              v-else-if="fb.type === 'complaint'"
              >投诉</el-tag
            >
            <el-tag size="mini" v-else>其他</el-tag>

            <el-tag
              size="mini"
              type="danger"
              v-if="fb.isReply === 0"
              style="margin-left: 6px;"
              >未回复</el-tag
            >
            <el-tag
              size="mini"
              type="success"
              v-else
              style="margin-left: 6px;"
              >已回复</el-tag
            >
          </div>
        </div>

        <div class="content"><strong>内容：</strong>{{ fb.content }}</div>
 <!-- 图片展示区 -->
        <div
          v-if="parseImageList(fb.imageList).length > 0"
          class="image-list"
        >
         <strong>图片：</strong> <el-image
            v-for="(img, i) in parseImageList(fb.imageList)"
            :key="i"
            :src="img"
            :preview-src-list="parseImageList(fb.imageList)"
            fit="cover"
            style="width: 90px; height: 90px; margin-right: 6px; border-radius: 6px;"
          />
        </div>
        <div v-if="fb.replyContent" class="reply">
          <strong>管理员回复：</strong> {{ fb.replyContent }}
        </div>

        <div class="time">
          提交时间：{{ fb.createTime }}
          <span v-if="fb.replyTime"> ｜ 回复时间：{{ fb.replyTime }}</span>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="feedbackList.length === 0" class="empty">暂无反馈记录</div>
  </div>
</template>

<script>
import { getUserInfo } from "@/utils/storage";

export default {
  name: "MyFeedback",
  data() {
    return {
      feedbackList: []
    };
  },
  created() {
    this.fetchFeedback();
  },
  methods: {
    /** 解析图片列表字符串为数组 */
     parseImageList(imageList) {
      try {
        if (Array.isArray(imageList)) return imageList;
        if (!imageList) return [];
        return JSON.parse(imageList);
      } catch {
        return imageList.split(",");
      }
    },
    /** 查询用户自己的反馈 */
    fetchFeedback() {
      const user = getUserInfo();
      if (!user || !user.id) {
        this.$message.warning("请先登录");
        return;
      }

      this.$axios
        .post(`/feedback/query`, { userId: user.id })
        .then((res) => {
          const { data } = res;
          if (data.code === 200) {
            this.feedbackList = data.data || [];
          } else {
            this.$message.error(data.message || "加载反馈失败");
          }
        })
        .catch((err) => {
          console.error("查询反馈异常：", err);
          this.$message.error("无法加载反馈信息");
        });
    }
  }
};
</script>

<style scoped lang="scss">
.feedback-container {
  .refresh-feedback {
    margin-block: 10px;
    display: flex;
    justify-content: right;

    span:hover {
      background-color: rgb(241, 241, 241);
    }

    span {
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      width: 30px;
      height: 30px;
      border-radius: 50%;
      background-color: rgb(246, 246, 246);
    }
  }

  .item {
    padding: 20px 10px;
    display: flex;
    gap: 20px;
    align-items: flex-start;
    border-bottom: 1px solid #f0f0f0;

    .status {
      width: 50px;
      height: 50px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 2px solid rgb(252, 232, 210);
      cursor: pointer;
      transition: all 0.5s;

      i {
        font-size: 12px;
      }
    }

    .status:hover {
      border: 2px solid rgb(220, 118, 44);
    }

    .content-box {
      flex: 1;
      .header-line {
        display: flex;
        align-items: center;
        justify-content: space-between;
        flex-wrap: wrap;

        .title {
          font-weight: bold;
          font-size: 18px;
          margin-bottom: 5px;
        }

        .tags {
          display: flex;
          align-items: center;
        }
      }

      .content {
        margin-block: 8px;
        font-size: 15px;
        color: #333;
        white-space: pre-wrap;
      }

      .reply {
        background: rgb(248, 248, 248);
        padding: 8px 10px;
        border-radius: 6px;
        font-size: 14px;
        color: rgb(80, 80, 80);
        margin-top: 8px;
        line-height: 1.5;
      }

      .time {
        margin-top: 6px;
        font-size: 12px;
        color: gray;
      }
    }
  }

  .empty {
    text-align: center;
    padding: 40px;
    color: #888;
  }
}
</style>
