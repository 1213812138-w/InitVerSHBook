<template>
  <el-row
    style="background-color: #FFFFFF; padding: 5px 0; border-radius: 5px;"
  >
    <!-- 筛选区域 -->
    <el-row style="padding: 10px; margin-left: 5px;">
      <el-row>
        <el-select
          style="width: 120px; margin-right: 5px;"
          v-model="feedbackQueryDto.type"
          placeholder="反馈类型"
          size="small"
          @change="fetchFeedback"
        >
          <el-option
            v-for="item in typeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>

        <el-select
          style="width: 120px; margin-right: 5px;"
          v-model="feedbackQueryDto.isReply"
          placeholder="回复状态"
          size="small"
          @change="fetchFeedback"
        >
          <el-option
            v-for="item in replyStatusList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>

        <el-input
          size="small"
          style="width: 200px;"
          v-model="feedbackQueryDto.userName"
          placeholder="用户名"
          clearable
          @clear="handleFilterClear"
        >
          <el-button
            slot="append"
            @click="handleFilter"
            icon="el-icon-search"
          ></el-button>
        </el-input>
      </el-row>
    </el-row>

    <!-- 表格区域 -->
    <el-row style="margin: 0 22px; border-top: 1px solid rgb(245,245,245);">
      <el-table :stripe="true" :data="tableData" style="width: 100%">
        <el-table-column prop="id" width="80" label="编号"></el-table-column>
        <el-table-column prop="userAvatar" width="68" label="头像">
          <template slot-scope="scope">
            <el-avatar
              :size="25"
              :src="scope.row.userAvatar"
              style="margin-top: 10px;"
            ></el-avatar>
          </template>
        </el-table-column>
        <el-table-column
          prop="userName"
          label="用户名"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="title"
          label="标题"
          min-width="150"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag type="info" v-if="scope.row.type === 'bug'">Bug</el-tag>
            <el-tag type="success" v-else-if="scope.row.type === 'suggestion'"
              >建议</el-tag
            >
            <el-tag type="warning" v-else-if="scope.row.type === 'complaint'"
              >投诉</el-tag
            >
            <el-tag v-else>其他</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="isReply" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag type="danger" v-if="scope.row.isReply === 0">未回复</el-tag>
            <el-tag type="success" v-else>已回复</el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="createTime"
          label="创建时间"
          width="160"
        ></el-table-column>
        <el-table-column
          prop="replyTime"
          label="回复时间"
          width="160"
        ></el-table-column>

        <el-table-column label="操作" width="140">
          <template slot-scope="scope">
            <span class="text-button" @click="handleView(scope.row)">查看</span>
            <span class="text-button" @click="handleReply(scope.row)"
              >回复</span
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        style="margin: 10px 0; float: right;"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalItems"
      ></el-pagination>
    </el-row>

    <!-- 查看详情对话框 -->
    <el-dialog :show-close="false" :visible.sync="dialogView" width="40%">
      <div style="padding: 20px;">
        <h3>{{ viewData.title }}</h3>
        <p style="color: gray; margin-bottom: 10px;">
          类型：{{ viewData.type }} | 用户：{{ viewData.userName }}
        </p>
        <p style="white-space: pre-wrap;">{{ viewData.content }}</p>

        <div v-if="viewData.imageList" style="margin-top: 10px;">
          <el-image
            v-for="(img, index) in parseImageList(viewData.imageList)"
            :key="index"
            :src="img"
            style="width: 80px; height: 80px; margin: 5px;"
            fit="cover"
          />
        </div>

        <div
          v-if="viewData.isReply === 1"
          style="margin-top: 10px; border-top: 1px solid #eee; padding-top: 10px;"
        >
          <h4>管理员回复：</h4>
          <p style="white-space: pre-wrap;">{{ viewData.replyContent }}</p>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <span class="edit-button" @click="dialogView = false">关闭</span>
      </span>
    </el-dialog>

    <!-- 回复对话框 -->
    <el-dialog :show-close="false" :visible.sync="dialogReply" width="30%">
      <div style="padding: 20px;">
        <p>反馈标题：{{ replyData.title }}</p>
        <p>用户：{{ replyData.userName }}</p>
        <el-input
          type="textarea"
          v-model="replyData.replyContent"
          placeholder="请输入回复内容"
          rows="4"
        ></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <span class="channel-button" @click="dialogReply = false">取消</span>
        <span class="edit-button" @click="submitReply">确定回复</span>
      </span>
    </el-dialog>
  </el-row>
</template>

<script>
export default {
  data() {
    return {
      // 表格数据
      tableData: [],
      totalItems: 0,
      currentPage: 1,
      pageSize: 10,

      // 查询条件
      feedbackQueryDto: {
        userName: "",
        type: "",
        isReply: "",
        pageNo: 1,
        pageSize: 10
      },

      // 筛选选项
      typeList: [
        { label: "全部类型", value: "" },
        { label: "Bug", value: "bug" },
        { label: "建议", value: "suggestion" },
        { label: "投诉", value: "complaint" },
        { label: "其他", value: "other" }
      ],

      replyStatusList: [
        { label: "全部", value: "" },
        { label: "未回复", value: 0 },
        { label: "已回复", value: 1 }
      ],

      // 查看详情对话框
      dialogView: false,
      viewData: {},

      // 回复对话框
      dialogReply: false,
      replyData: {}
    };
  },
  created() {
    this.fetchFeedback();
  },

  methods: {
    /** 获取反馈列表 */
    fetchFeedback() {
      this.feedbackQueryDto.pageNo = this.currentPage;
      this.feedbackQueryDto.pageSize = this.pageSize;
      this.$axios
        .post("/feedback/query", this.feedbackQueryDto)
        .then(res => {
          if (res.data.code === 200) {
            this.tableData = res.data.data || [];
            this.totalItems = res.data.totalCount || 0;
          } else {
            this.$message.error(res.data.message || "加载失败");
          }
        })
        .catch(() => {
          this.$message.error("获取反馈数据失败");
        });
    },

    /** 搜索触发 */
    handleFilter() {
      this.currentPage = 1;
      this.fetchFeedback();
    },

    /** 清空搜索 */
    handleFilterClear() {
      this.feedbackQueryDto.userName = "";
      this.fetchFeedback();
    },

    /** 分页变化 */
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchFeedback();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchFeedback();
    },

    /** 查看详情 */
    handleView(row) {
      this.viewData = { ...row };
      this.dialogView = true;
    },

    /** 回复操作 */
    handleReply(row) {
      this.replyData = {
        id: row.id,
        title: row.title,
        userName: row.userName,
        replyContent: row.replyContent || ""
      };
      this.dialogReply = true;
    },

    /** 提交回复 */
    submitReply() {
      if (!this.replyData.replyContent) {
        this.$message.warning("回复内容不能为空");
        return;
      }
      this.$axios
        .post("/feedback/reply", {
          id: this.replyData.id,
          replyContent: this.replyData.replyContent
        })
        .then(res => {
          if (res.data.code === 200) {
            this.$message.success("回复成功");
            this.dialogReply = false;
            this.fetchFeedback();
          } else {
            this.$message.error(res.data.message || "回复失败");
          }
        })
        .catch(() => {
          this.$message.error("回复请求失败");
        });
    },

    /** 图片解析（后端存的是字符串） */
    parseImageList(imageList) {
      try {
        if (Array.isArray(imageList)) return imageList;
        if (!imageList) return [];
        return JSON.parse(imageList);
      } catch {
        return imageList.split(",");
      }
    }
  }
};
</script>
<style scoped lang="scss"></style>
