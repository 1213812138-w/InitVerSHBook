<template>
  <div>
    <div>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <!-- 我的反馈 -->
        <el-tab-pane label="我的反馈" name="first">
          <MyFeedback ref="myFeedbackRef" />
        </el-tab-pane>

        <!-- 提交反馈 -->
        <el-tab-pane label="提交反馈" name="second">
          <SubmitFeedback />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import MyFeedback from "@/views/user/MyFeedback"
import SubmitFeedback from "@/views/user/SubmitFeedback"

export default {
  components: { MyFeedback, SubmitFeedback },
  name: "Feedback",
  data() {
    return {
      activeName: "first"
    };
  },
  mounted() {
    // 初次进入页面时也调用一次（默认在“我的反馈”标签）
    this.$nextTick(() => {
      if (this.$refs.myFeedbackRef) {
        this.$refs.myFeedbackRef.fetchFeedback();
      }
    });
  },
  methods: {
    handleClick(tab) {
      // 当点击到“我的反馈”标签时，触发子组件刷新
      if (tab.name === "first" && this.$refs.myFeedbackRef) {
        this.$refs.myFeedbackRef.fetchFeedback();
      }
    }
  }
};
</script>

<style scoped lang="scss"></style>
