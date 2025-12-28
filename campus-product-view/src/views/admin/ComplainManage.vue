<template>
  <div class="card-container">
    <!-- 筛选卡片 -->
    <el-card class="filter-card" shadow="never">
      <div class="filter-content">
        <div class="filter-tabs">
          <el-radio-group v-model="adminStatusItem" @change="handleAdminStatusChange" size="small">
            <el-radio-button v-for="(item, index) in adminStatusList" :key="index" :label="item">
              {{ item.name }}
            </el-radio-button>
          </el-radio-group>
        </div>

        <div class="filter-controls">
          <el-input
            class="filter-item"
            v-model="queryDto.orderCode"
            placeholder="订单号"
            clearable
            @clear="handleFilter"
            @keyup.enter.native="handleFilter"
            size="small"
          >
            <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
          </el-input>
        </div>
      </div>
    </el-card>

    <!-- 申诉列表卡片 -->
    <el-card class="order-card" shadow="never">
      <div v-for="item in tableData" :key="item.id" class="order-item">
        <div class="order-header">
          <div class="order-code">申诉ID: {{ item.id }} | 订单号: {{ item.code }}</div>
          <div class="order-time">发起时间: {{ item.createTime }}</div>
        </div>

        <div class="order-content">
          <div class="order-user">
            <el-avatar :size="40" :src="item.customerAvatar || defaultAvatar"></el-avatar>
            <div class="user-name">{{ item.customerName }}（买家）</div>
          </div>

          <div class="order-product">
            <el-image
              v-if="item.coverList"
              :src="item.coverList.split(',')[0]"
              :preview-src-list="item.coverList.split(',')"
              fit="cover"
              class="product-image"
            />
            <div class="product-info">
              <div class="product-title">{{ item.productName }}</div>
              <div class="product-meta">
                <span class="price">成交价: ¥{{ item.totalPrice }}</span>
              </div>
            </div>
          </div>

          <div class="order-status">
            <el-tag :type="item.adminComplainStatus ? 'success' : 'warning'" size="small">
              {{ item.adminComplainStatus ? '已处理' : '待处理' }}
            </el-tag>
            <div v-if="item.adminComplainStatus" class="process-time">
              处理时间: {{ item.processTime }}
            </div>
          </div>

          <div class="order-actions">
            <el-button size="mini" @click="handleDetail(item)">查看详情</el-button>
            <el-button
              v-if="!item.adminComplainStatus"
              size="mini"
              type="primary"
              @click="handleProcess(item)"
            >
              去处理
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(item)">删除</el-button>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-container" v-if="totalItems > 0">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalItems"
        />
      </div>
    </el-card>

    <!-- 处理申诉抽屉（新增同意/拒绝单选 + 理由） -->
    <el-drawer title="处理申诉" :visible.sync="processDrawer" direction="rtl" size="40%">
      <div class="drawer-content" v-if="currentComplain">
        <div class="complain-info">
          <p><strong>申诉描述：</strong>{{ currentComplain.detail }}</p>
          <div v-if="currentComplain.pictureList" class="images">
            <el-image
              v-for="(img, i) in pictureList"
              :key="i"
              :src="img"
              :preview-src-list="pictureList"
              style="width: 100px; height: 100px; margin: 5px;"
            />
          </div>
        </div>

        <el-form :model="processForm" label-width="100px">
          <el-form-item label="处理结果" required>
            <el-radio-group v-model="processForm.action">
              <el-radio label="approve">同意申诉</el-radio>
              <el-radio label="reject">拒绝申诉</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="处理理由" required>
            <el-input
              type="textarea"
              :rows="5"
              placeholder="请填写处理理由"
              v-model="processForm.reason"
            />
          </el-form-item>
        </el-form>

        <div class="drawer-footer">
          <el-button @click="processDrawer = false">取消</el-button>
          <el-button type="primary" @click="submitProcess">提交处理</el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 详情抽屉 -->
    <el-drawer title="申诉详情" :visible.sync="detailDrawer" direction="rtl" size="40%">
      <div class="drawer-content" v-if="currentComplain">
        <div class="detail-item">
          <span class="label">申诉描述：</span>
          <span>{{ currentComplain.detail }}</span>
        </div>
        <div class="detail-item" v-if="currentComplain.pictureList">
          <span class="label">申诉图片：</span>
          <el-image
            v-for="(img, i) in pictureList"
            :key="i"
            :src="img"
            :preview-src-list="pictureList"
            style="width: 100px; height: 100px; margin: 0 10px 10px 0;"
          />
        </div>
        <div class="detail-item">
          <span class="label">管理员意见：</span>
          <span>{{ currentComplain.adminComment || '暂无' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">处理时间：</span>
          <span>{{ currentComplain.processTime || '未处理' }}</span>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
export default {
  data() {
    return {
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      tableData: [],
      totalItems: 0,
      currentPage: 1,
      pageSize: 10,
      queryDto: {
        current: 1,
        size: 10
      },
      adminStatusList: [
        { adminComplainStatus: null, name: '全部' },
        { adminComplainStatus: false, name: '待处理' },
        { adminComplainStatus: true, name: '已处理' }
      ],
      adminStatusItem: { adminComplainStatus: null, name: '全部' },

      processDrawer: false,
      detailDrawer: false,
      currentComplain: null,
      pictureList: [],

      // 新增：处理表单
      processForm: {
        action: 'approve', // 默认同意
        reason: ''
      }
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.queryDto.current = this.currentPage;
      this.queryDto.size = this.pageSize;

      this.$axios.post('/api/campus-product-sys/v1.0/complain/query', this.queryDto)
        .then(res => {
          if (res.data.code === 200) {
            this.tableData = res.data.data || [];
            this.totalItems = res.data.total || 0;
          } else {
            this.$message.error(res.data.msg || '加载失败');
          }
        })
        .catch(err => {
          this.$message.error('加载申诉列表失败');
          console.error(err);
        });
    },

    handleAdminStatusChange(item) {
      this.adminStatusItem = item;
      this.queryDto.adminComplainStatus = item.adminComplainStatus;
      this.currentPage = 1;
      this.fetchData();
    },

    handleFilter() {
      this.currentPage = 1;
      this.fetchData();
    },

    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
      this.fetchData();
    },

    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchData();
    },

    handleDetail(row) {
      this.currentComplain = row;
      this.pictureList = row.pictureList ? row.pictureList.split(',') : [];
      this.detailDrawer = true;
    },

    handleProcess(row) {
      this.currentComplain = row;
      this.pictureList = row.pictureList ? row.pictureList.split(',') : [];
      this.processForm = {
        action: 'approve',
        reason: ''
      };
      this.processDrawer = true;
    },

    // 新增：提交处理（构造 "同意：" 或 "拒绝：" 前缀写入 adminComment）
    submitProcess() {
  if (!this.processForm.reason.trim()) {
    this.$message.warning('请填写处理理由');
    return;
  }

  const prefix = this.processForm.action === 'approve' ? '同意：' : '拒绝：';
  const adminComment = prefix + this.processForm.reason.trim();

  // 构造更新数据，同时更新 adminComplainStatus
  const updateData = {
    id: this.currentComplain.id,
    adminComment: adminComment,
    adminComplainStatus: this.processForm.action === 'approve' ? true : false
  };

  // 使用 /update 接口更新
  this.$axios.put('/api/campus-product-sys/v1.0/complain/update', updateData)
    .then(res => {
      if (res.data.code === 200) {
        this.$message.success('处理成功');
        this.processDrawer = false;
        this.fetchData();
      } else {
        this.$message.error(res.data.msg || '处理失败');
      }
    })
    .catch(err => {
      this.$message.error('处理失败');
      console.error(err);
    });
},

    handleDelete(row) {
      this.$confirm('删除后不可恢复，确认删除该申诉？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios.post('/api/campus-product-sys/v1.0/complain/delete', null, {
          params: { id: row.id }
        }).then(res => {
          if (res.data.code === 200) {
            this.$message.success('删除成功');
            this.fetchData();
          } else {
            this.$message.error(res.data.msg || '删除失败');
          }
        }).catch(err => {
          this.$message.error('删除失败');
          console.error(err);
        });
      }).catch(() => {});
    }
  }
};
</script>

<style scoped lang="scss">
.card-container { padding: 20px 0; }

.filter-card {
  margin-bottom: 20px;
  border-radius: 8px;
  .filter-content { display: flex; flex-direction: column; gap: 15px; }
  .filter-tabs { display: flex; flex-wrap: wrap; gap: 10px; }
  .filter-controls { display: flex; gap: 10px; }
  .filter-item { flex: 1; min-width: 200px; }
}

.order-card {
  border-radius: 8px;
  .order-item {
    padding: 20px;
    border-bottom: 1px solid #ebeef5;
    &:last-child { border-bottom: none; }
    .order-header {
      display: flex; justify-content: space-between; margin-bottom: 15px;
      padding-bottom: 10px; border-bottom: 1px dashed #ebeef5;
      .order-code { font-weight: 500; color: #303133; }
      .order-time { font-size: 12px; color: #909399; }
    }
    .order-content {
      display: flex; gap: 20px;
      .order-user {
        flex: 0 0 100px; display: flex; flex-direction: column; align-items: center;
        .user-name { margin-top: 5px; font-size: 13px; }
      }
      .order-product {
        flex: 1; display: flex; gap: 15px;
        .product-image { width: 80px; height: 80px; border-radius: 4px; }
        .product-info {
          flex: 1;
          .product-title { margin: 0 0 8px 0; font-size: 15px; color: #303133; }
          .product-meta { margin-bottom: 8px; font-size: 13px;
            .price { margin-right: 15px; color: #f56c6c; font-weight: 500; }
          }
        }
      }
      .order-status {
        flex: 0 0 100px; display: flex; flex-direction: column; align-items: center;
        .process-time { margin-top: 5px; font-size: 12px; color: #909399; }
      }
      .order-actions {
        flex: 0 0 140px; display: flex; flex-direction: column; justify-content: center;
        align-items: flex-end; gap: 8px;
      }
    }
  }
}

.pagination-container { padding: 20px 0; text-align: center; }

.drawer-content {
  padding: 20px;
  .complain-info {
    margin-bottom: 30px;
    p { margin: 10px 0; line-height: 1.6; }
  }
  .drawer-footer {
    margin-top: 30px;
    text-align: right;
  }
}

.detail-item {
  margin-bottom: 20px;
  .label {
    font-weight: 500;
    color: #606266;
    width: 100px;
    display: inline-block;
  }
}
</style>