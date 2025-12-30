<template>
  <div class="my-complain-container">
    <h2 class="page-title">我的申诉</h2>

    <el-empty v-if="list.length === 0" description="您暂无申诉记录">
      <el-button type="primary" @click="$router.push('/orders')">去我的订单看看</el-button>
    </el-empty>

    <div v-else class="complain-list">
      <el-card v-for="item in list" :key="item.id" class="complain-card" shadow="hover">
        <div class="card-header">
          <span class="id">申诉单 #{{ item.id }}</span>
          <span class="time">{{ formatDate(item.createTime) }}</span>
        </div>
        <div class="card-body">
          <div class="info-line">
            <span class="label">订单号：</span>
            <span class="value">{{ item.code }}</span>
          </div>
          <div class="info-line">
            <span class="label">商品：</span>
            <span class="value">{{ item.productName }}</span>
          </div>
          <div class="info-line">
            <span class="label">申诉理由：</span>
            <span class="value">{{ item.detail }}</span>
          </div>
          <div class="info-line" v-if="item.pictureList">
            <span class="label">图片：</span>
            <el-image
              v-for="(url, index) in item.pictureList.split(',')"
              :key="index"
              :src="url"
              :preview-src-list="item.pictureList.split(',')"
              style="width: 80px; height: 80px; margin-right: 10px;"
            />
          </div>
          <div class="status-line">
            <el-tag :type="item.adminComplainStatus ? 'success' : 'warning'">
              {{ item.adminComplainStatus ? '已处理' : '处理中' }}
            </el-tag>
            <div v-if="item.adminComplainStatus" class="comment">
              管理员回复：{{ item.adminComment || '无' }}
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'complain',
  data() {
    return {
      list: []
    }
  },
  created() {
    this.loadMyComplains()
  },
  methods: {
    loadMyComplains() {
      this.$axios.post('/api/campus-product-sys/v1.0/complain/queryUser', {
        current: 0,
        size: 100  // 取足够多，简单实现
      }).then(res => {
        if (res.data.code === 200) {
          this.list = res.data.data || []
        } else {
          this.$message.error(res.data.msg || '加载失败')
        }
      }).catch(err => {
        this.$message.error('加载申诉记录失败')
        console.error(err)
      })
    },
    formatDate(val) {
      if (!val) return '-'
      const parts = val.split('T')
      const date = parts[0]
      const timePart = parts[1] || ''
      const time = timePart.substring(0, 8) // 取 HH:mm:ss
      return `${date} ${time}`
    }
  }
}
</script>

<style scoped>
.my-complain-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.page-title {
  margin-bottom: 30px;
  color: #303133;
  font-weight: 600;
}

.complain-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.complain-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px dashed #ebeef5;
  font-size: 14px;
  color: #909399;
}

.card-header .id {
  font-weight: 500;
  color: #303133;
}

.card-body {
  padding-top: 15px;
}

.info-line {
  margin-bottom: 12px;
  font-size: 14px;
  color: #606266;
}

.info-line .label {
  color: #909399;
  margin-right: 10px;
}

.status-line {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px dashed #ebeef5;
}

.comment {
  margin-top: 10px;
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}
</style>