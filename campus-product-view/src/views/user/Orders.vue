<template>
    <div>
        <div>
            <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="购物的订单" name="first">
                    <BuyOrders ref="BuyOrdersRef" />
                </el-tab-pane>

                <el-tab-pane label="收到的订单" name="second">
                    <MyOrders ref="MyOrdersRef" />
                </el-tab-pane>

            </el-tabs>

        </div>

    </div>

</template>

<script>
import BuyOrders from "@/views/user/BuyOrders"
import MyOrders from "@/views/user/MyOrders"
export default {
    components: { BuyOrders, MyOrders },
    name: 'Orders',
    data() {
        return {
            activeName: 'first'
        };
    },
    created() {

    },
    methods: {
        handleClick(tab){
            if(tab.name === "first"&& this.$refs.BuyOrdersRef){
                this.$refs.BuyOrdersRef.fetchOrders();
            }
            if(tab.name === "second"&& this.$refs.MyOrdersRef){
                this.$refs.MyOrdersRef.fetchOrders();
            }
        }
    }
};
</script>

<style scoped lang="scss"></style>
