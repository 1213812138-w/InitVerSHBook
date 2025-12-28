<template>
  <div>
    <div class="condition">
      <span class="trade">
        <span
          :style="{
            color:
              tradeStatusSelectedItem.name === tradeStatus.name
                ? 'rgb(51,51,51)'
                : '',
            backgroundColor:
              tradeStatusSelectedItem.name === tradeStatus.name
                ? 'rgb(255,255,255)'
                : '',
          }"
          @click="tradeStatusSelected(tradeStatus)"
          v-for="(tradeStatus, index) in tradeStatusList"
          :key="index"
          >{{ tradeStatus.name }}</span
        >
      </span>

      <el-input
        size="small"
        style="width: 166px;"
        v-model="ordersQueryDto.code"
        placeholder="订单号"
        clearable
        @clear="handleFilterClear"
      >
        <el-button
          slot="append"
          @click="fetchOrders"
          icon="el-icon-search"
        ></el-button>
      </el-input>
    </div>

    <div v-if="ordersList.length === 0">
      <el-empty description="未找到符合条件的订单数据"></el-empty>
    </div>

    <div
      v-else
      class="item-order"
      v-for="(orderInfo, index) in ordersList"
      :key="index"
    >
      <div class="orders-base-info">
        <div class="code">订单号：{{ orderInfo.code }}</div>

        <div class="code" v-if="orderInfo.tradeStatus">
          支付时间：{{ orderInfo.tradeTime }}
        </div>

        <div class="code" v-if="orderInfo.isRefundConfirm">
          退款时间：{{ orderInfo.refundTime }}
        </div>
      </div>

      <div class="info">
        <div>
          <img :src="orderInfo.cover" alt="" srcset="" />
        </div>

        <div>
          <div>
            <span class="title">{{ orderInfo.productTitle }}</span>

            <span class="number">x{{ orderInfo.buyNumber }}</span>
          </div>

          <div class="detail">备注：{{ orderInfo.detail }}</div>

          <div>
            <span class="symbol">￥</span>

            <span class="price">{{ totalPrice(orderInfo) }}</span>
          </div>

          <div class="orders-base-info">
            创建时间：{{ orderInfo.createTime }}
          </div>

          <div style="margin-block: 10px;">
            <span
              v-if="orderInfo.refundStatus"
              style="color: rgb(0, 121, 186);"
            >
              {{
                orderInfo.isRefundConfirm
                  ? "钱款已原路返回"
                  : "申请退款中，待卖家确认..."
              }}
            </span>

            <span v-else>
              <span
                @click="placeAnOrder(orderInfo)"
                v-if="!orderInfo.tradeStatus"
                class="edit-button"
                >下单</span
              >

              <span @click="refund(orderInfo)" v-else class="edit-button"
                >申请退款</span
              >

              <span
                style="margin-left: 5px;"
                @click="del(orderInfo)"
                v-if="orderInfo.tradeStatus"
                class="channel-button"
                >删除</span
              >
            </span>

            <!-- 申诉按钮和状态显示 -->
            <div style="margin-top: 10px;">
              <template v-if="getComplainStatusText(orderInfo)">
                <el-tag type="info" size="small">
                  {{ getComplainStatusText(orderInfo) }}
                </el-tag>
              </template>
              <template v-else>
                <el-button
                  type="danger"
                  size="small"
                  v-if="orderInfo.tradeStatus && !orderInfo.refundStatus"
                  @click="openComplainDialog(orderInfo)"
                >
                  发起申诉
                </el-button>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 申诉对话框 -->
    <el-dialog title="发起申诉" :visible.sync="complainDialogVisible" width="500px">
      <el-form :model="complainForm" :rules="complainRules" ref="complainForm" label-width="80px">
        <el-form-item label="申诉理由" prop="detail">
          <el-input
            type="textarea"
            :rows="5"
            placeholder="请详细描述问题（如质量问题、物流问题等）"
            v-model="complainForm.detail"
          ></el-input>
        </el-form-item>

        <el-form-item label="上传图片">
          <el-upload
            action="http://localhost:21090/api/campus-product-sys/v1.0/file/upload"
            list-type="picture-card"
            :on-success="handleUploadSuccess"
            :on-remove="handleUploadRemove"
            :file-list="complainForm.pictureList"
            :limit="6"
            multiple
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <div class="el-upload__tip">最多上传6张图片，支持jpg/png</div>
        </el-form-item>
      </el-form>

      <span slot="footer">
        <el-button @click="complainDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitComplain">提交申诉</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "BuyOrders",
  data() {
    return {
      ordersList: [],
      ordersQueryDto: {},
      tradeStatusSelectedItem: {},
      tradeStatusList: [
        { tradeStatus: null, name: "全部" },
        { tradeStatus: true, name: "已支付" },
        { tradeStatus: false, name: "未支付" },
      ],

      // 申诉相关
      complainDialogVisible: false,
      currentOrder: null,
      complainForm: {
        detail: '',
        pictureList: []
      },
      complainRules: {
        detail: [
          { required: true, message: '请填写申诉理由', trigger: 'blur' },
          { min: 10, message: '理由至少10个字', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.fetchOrders();
    this.tradeStatusSelected(this.tradeStatusList[0]);
  },
  methods: {
    tradeStatusSelected(tradeStatusItem) {
      this.tradeStatusSelectedItem = tradeStatusItem;
      this.ordersQueryDto.tradeStatus = tradeStatusItem.tradeStatus;
      this.fetchOrders();
    },
    handleFilterClear() {
      this.ordersQueryDto.code = "";
      this.fetchOrders();
    },
    async del(orderInfo) {
      const confirmed = await this.$swalConfirm({
        title: "删除订单数据",
        text: `删除后不可恢复，是否继续？`,
        icon: "warning",
      });
      if (confirmed) {
        try {
          let ids = [orderInfo.id];
          const response = await this.$axios.post(`/orders/batchDelete`, ids);
          if (response.data.code === 200) {
            this.$notify({
              duration: 1000,
              title: "信息删除",
              message: "删除成功",
              type: "success",
            });
            this.fetchOrders();
            return;
          }
        } catch (error) {
          this.$message.error("订单信息删除异常：", error);
          console.error(`订单信息删除异常：`, error);
        }
      }
    },
    totalPrice(orderInfo) {
      const totalPrice = orderInfo.buyNumber * orderInfo.buyPrice;
      return parseFloat(totalPrice).toFixed(2);
    },
    refund(orders) {
      this.$confirm('确定要申请退款吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.post(`/product/refund/${orders.id}`)
          .then((res) => {
            const { data } = res;
            if (data.code === 200) {
              orders.customerRefundStatus = true;
              orders.refundStatus = true;
              orders.isRefundConfirm = false;
              this.$notify.success({
                title: '成功',
                message: '退款申请已提交',
                duration: 1500
              });
              this.fetchOrders();
            }
          }).catch(error => {
            console.log("退款异常：", error);
            this.$notify.error('网络错误，请重试');
          });
      }).catch(() => {});
    },
    placeAnOrder(orders) {
      this.$axios
        .post(`/product/placeAnOrder/${orders.id}`)
        .then((res) => {
          const { data } = res;
          if (data.code === 200) {
            this.$notify({
              duration: 1000,
              title: "下单",
              message: "下单成功",
              type: "success",
            });
            this.fetchOrders();
          }
        })
        .catch((error) => {
          console.log("下单异常：", error);
        });
    },
    coverParse(coverList) {
      if (coverList.productCover === null) {
        return '';
      }
      return coverList.productCover.split(",")[0];
    },
    fetchOrders() {
      this.$axios
        .post("/orders/queryUser", this.ordersQueryDto)
        .then((res) => {
          const { data } = res;
          if (data.code === 200) {
            this.ordersList = data.data.map((order) => {
              return {
                id: order.id,
                code: order.code,
                productTitle: order.productTitle,
                detail: order.detail,
                buyPrice: order.buyPrice,
                buyNumber: order.buyNumber,
                tradeStatus: order.tradeStatus,
                tradeTime: order.tradeTime,
                refundStatus: order.refundStatus,
                refundTime: order.refundTime,
                isRefundConfirm: order.isRefundConfirm,
                createTime: order.createTime,
                cover: this.coverParse(order),
                customerRefundStatus: order.customerRefundStatus,
                adminComment: order.adminComment || null // 关键：映射 adminComment
              };
            });
          }
        })
        .catch((error) => {
          console.log("订单数据查询异常：", error);
        });
    },

    // 关键：申诉状态判断
    getComplainStatusText(orderInfo) {
      if (orderInfo.adminComment) {
        if (orderInfo.adminComment.startsWith('同意：')) {
          return '申诉已通过';
        }
        if (orderInfo.adminComment.startsWith('拒绝：')) {
          return '申诉被拒绝';
        }
        if (orderInfo.adminComment.trim() !== '') {
          return '申诉处理中';
        }
      }
      return null; // 未申诉
    },

    // 申诉相关方法
    openComplainDialog(orderInfo) {
      this.currentOrder = orderInfo;
      this.complainForm = {
        detail: '',
        pictureList: []
      };
      this.complainDialogVisible = true;
    },

    handleUploadSuccess(response) {
      if (response.code === 200) {
        this.complainForm.pictureList.push(response.data);
        this.$message.success('图片上传成功');
      } else {
        this.$message.error('上传失败');
      }
    },

    handleUploadRemove(file) {
      this.complainForm.pictureList = this.complainForm.pictureList.filter(url => url !== file.url);
    },

    submitComplain() {
      this.$refs.complainForm.validate(valid => {
        if (!valid) return;

        const complainData = {
          orderId: this.currentOrder.id,
          detail: this.complainForm.detail,
          pictureList: this.complainForm.pictureList.join(',') || null
        };

        this.$axios.post('/api/campus-product-sys/v1.0/complain/save', complainData)
          .then(res => {
            if (res.data.code === 200) {
              this.$message.success('申诉提交成功，等待管理员处理');
              this.complainDialogVisible = false;
              this.fetchOrders();
            } else {
              this.$message.error(res.data.msg || '提交失败');
            }
          })
          .catch(err => {
            this.$message.error('申诉提交失败');
            console.error(err);
          });
      });
    }
  },
};
</script>

<style scoped lang="scss">
.condition {
  display: flex;
  justify-content: right;

  .trade {
    display: inline-block;
    font-size: 12px;
    background-color: rgb(246, 246, 246);
    line-height: 24px;
    padding-inline: 10px;
    padding-block: 4px;
    margin-right: 5px;
    border-radius: 5px;
    cursor: pointer;

    span {
      display: inline-block;
      padding-inline: 10px;
      border-radius: 5px;
    }
  }
}

.item-order {
  font-size: 14px;
  margin-block: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgb(246, 246, 246);

  .orders-base-info {
    font-size: 14px;
    margin-block: 6px;
    color: rgb(117, 117, 117);
    .code {
      margin-block: 4px;
    }
  }

  .info {
    .detail {
      margin-block: 4px;
      font-size: 14px;
    }

    img {
      width: 140px;
    }

    display: flex;
    justify-content: left;
    gap: 20px;

    .symbol {
      font-size: 12px;
      margin-right: 4px;
      color: rgb(255, 79, 36);
    }

    .price {
      font-size: 18px;
      font-weight: 800;
      color: rgb(255, 79, 36);
    }

    .title {
      font-size: 20px;
      font-weight: 800;
    }

    .number {
      margin-left: 6px;
    }
  }
}
</style>